package co.pla.portfoliomanagement.scheduler.service;

import co.pla.portfoliomanagement.scheduler.dto.JobDetailDto;
import co.pla.portfoliomanagement.scheduler.dto.JobSummaryDto;
import co.pla.portfoliomanagement.scheduler.exceptions.JobConfigurationException;
import co.pla.portfoliomanagement.scheduler.exceptions.JobDetailException;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.quartz.*;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.quartz.TriggerKey.triggerKey;

@Service
@Slf4j
public class QuartzSchedulerService {
    public static final String JOB_GROUP_ID = "PORTFOLIO_JOBS";
    public static final String TRIGGER_GROUP_ID = "PORTFOLIO_TRIGGERS";
    public static final String CRON_TRIGGER_GROUP_ID = "PORTFOLIO_CRON_TRIGGERS";
    public static final String JOB_DETAIL_GROUP_ID = "QUARTZ_JOB_GROUP";
    public static final String JOB_DETAIL_DESCRIPTION = "QUARTZ_JOB_DESCRIPTION";
    public static final String TRIGGER_DESCRIPTION = "QUARTZ_TRIGGER_DESCRIPTION";
    private static final Logger logger = LoggerFactory.getLogger(QuartzSchedulerService.class);
    private static final String JOB_PACKAGE_BASE = "co.pla.portfoliomanagement.scheduler.job.";
    private final Scheduler scheduler;


    public QuartzSchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }


    public String scheduleJob(ZonedDateTime startTime, JobDataMap jobDataMap,
                              String jobClassName) throws SchedulerException, ClassNotFoundException {

        validateScheduleRequest(startTime, jobDataMap, jobClassName);
        try {
            JobDetail jobDetail = buildJobDetail(getJobClass(jobClassName), jobDataMap);
            Trigger trigger = buildJobTrigger(jobDetail, startTime, getInterval(jobDataMap));

            Date scheduledTime = scheduler.scheduleJob(jobDetail, trigger);
            log.info("Job scheduled successfully: {} at {}", jobDetail.getKey(), scheduledTime);

            return jobDetail.getKey().getName();

        } catch (ClassNotFoundException e) {
            log.error("Job class not found: {}", jobClassName, e);
            throw new JobConfigurationException("Job class not found: " + jobClassName, e);
        } catch (SchedulerException e) {
            log.error("Failed to schedule job: {}", jobClassName, e);
            throw e;
        }
    }


    public String updateScheduler(JobDataMap jobDataMap, String jobId) throws SchedulerException {
        JobKey jobKey = new JobKey(jobId, JOB_DETAIL_GROUP_ID);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        var jobType = jobDetail.getJobDataMap().get("name").toString();
        jobDetail.getJobDataMap().put("page", jobDataMap.get("page"));
        jobDetail.getJobDataMap().put("per_page", jobDataMap.get("per_page"));
        jobDetail.getJobDataMap().put("interval", jobDataMap.get("interval"));
        Trigger trigger = scheduler.getTrigger(triggerKey(jobType, TRIGGER_GROUP_ID));
        trigger.getJobDataMap().put("triggerUpdated", true);

        ((SimpleTriggerImpl) trigger).setRepeatInterval(Long.valueOf((Integer) jobDataMap.get("interval") * 1000));

        scheduler.rescheduleJob((triggerKey(jobType, TRIGGER_GROUP_ID)), trigger);
        scheduler.addJob(jobDetail, true);
        return jobDetail.getKey().toString();
    }

    public LocalDateTime convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public Optional<JobDetailDto> getJobDetail(String jobId) throws SchedulerException {
        JobKey jobKey = new JobKey(jobId, JOB_GROUP_ID);

        if (!scheduler.checkExists(jobKey)) {
            return Optional.empty();
        }

        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        Trigger trigger = scheduler.getTriggersOfJob(jobKey).stream().findFirst().orElse(null);

        return Optional.of(JobDetailDto.from(jobDetail, trigger));
    }

    public boolean pauseJob(String jobId) throws SchedulerException {
        JobKey jobKey = new JobKey(jobId, JOB_GROUP_ID);
        if (scheduler.checkExists(jobKey)) {
            scheduler.pauseJob(jobKey);
            log.info("Job paused: {}", jobId);
            return true;
        }
        return false;
    }

    public boolean resumeJob(String jobId) throws SchedulerException {
        JobKey jobKey = new JobKey(jobId, JOB_GROUP_ID);
        if (scheduler.checkExists(jobKey)) {
            scheduler.resumeJob(jobKey);
            log.info("Job resumed: {}", jobId);
            return true;
        }
        return false;
    }

    public boolean isJobRunning(String jobId) throws SchedulerException {
        JobKey jobKey = new JobKey(jobId, JOB_GROUP_ID);
        List<JobExecutionContext> currentlyExecutingJobs = scheduler.getCurrentlyExecutingJobs();

        return currentlyExecutingJobs.stream()
                .anyMatch(context -> context.getJobDetail().getKey().equals(jobKey));
    }

    public List<JobSummaryDto> getAllJobs() throws SchedulerException {
        return scheduler.getJobKeys(GroupMatcher.jobGroupEquals(JOB_GROUP_ID)).stream()
                .map(this::toJobSummaryDTO)
                .collect(Collectors.toList());
    }
    public boolean killJob(String id) throws SchedulerException {
        var jobKey = new JobKey(id, JOB_DETAIL_GROUP_ID);
        return scheduler.deleteJob(jobKey);
    }

    public void shutSchedulerDown() throws SchedulerException {
        scheduler.shutdown();
    }

    private void validateScheduleRequest(ZonedDateTime startTime, JobDataMap jobDataMap, String jobClassName) {
        if (startTime == null) {
            throw new JobDetailException("Start time cannot be null");
        }
        if (jobDataMap == null || jobDataMap.isEmpty()) {
            throw new JobDetailException("Job data cannot be null or empty");
        }
        if (StringUtils.isBlank(jobClassName)) {
            throw new JobDetailException("Job class name cannot be blank");
        }
        validateStartTime(startTime);
    }

    private void validateStartTime(ZonedDateTime startTime) {
        if (startTime.isBefore(ZonedDateTime.now())) {
            throw new DateTimeException(
                    "Start time must be in the future. Provided: " + startTime + ", Current: " + ZonedDateTime.now());
        }
    }
    private int getInterval(JobDataMap jobDataMap) {
        Object interval = jobDataMap.get("interval");
        if (interval instanceof Integer) {
            return (Integer) interval;
        }
        throw new JobDetailException("Interval must be an integer");
    }
    private Class<? extends Job> getJobClass(String className) throws ClassNotFoundException {
        return (Class<? extends Job>) Class.forName(JOB_PACKAGE_BASE + className);
    }

    private JobSummaryDto toJobSummaryDTO(JobKey jobKey) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            String jobName = jobDetail.getJobDataMap().getString("name");

            return JobSummaryDto.builder()
                    .jobId(jobKey.getName())
                    .jobGroup(jobKey.getGroup())
                    .jobName(jobName)
                    .jobClass(jobDetail.getJobClass().getSimpleName())
                    .build();

        } catch (SchedulerException e) {
            log.warn("Failed to get details for job: {}", jobKey, e);
            return JobSummaryDto.builder()
                    .jobId(jobKey.getName())
                    .jobGroup(jobKey.getGroup())
                    .jobName("Unknown")
                    .build();
        }
    }

    private JobDetail buildJobDetail(Class<? extends Job> jobClass, JobDataMap jobDataMap) {
        String jobName = jobDataMap.getString("name");
        if (StringUtils.isBlank(jobName)) {
            throw new JobDetailException("Job name is required in job data");
        }

        return JobBuilder.newJob(jobClass)
                .withIdentity(jobName, JOB_GROUP_ID)
                .withDescription("Portfolio management job: " + jobName)
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }


    private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt, int interval) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getJobDataMap().get("name").toString(), TRIGGER_GROUP_ID)
                .withDescription(TRIGGER_DESCRIPTION)
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(
                        interval).repeatForever())
                .build();
    }

    private CronTrigger buildJobTrigger(JobDetail jobDetail, String cronExpression) {
        /**
         * cronExpression -> e.g: "0 0/2 8-17 * * ?"
         */
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), CRON_TRIGGER_GROUP_ID)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
    }


}
