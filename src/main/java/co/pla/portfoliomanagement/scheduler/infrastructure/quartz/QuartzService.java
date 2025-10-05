package co.pla.portfoliomanagement.scheduler.infrastructure.quartz;

import co.pla.portfoliomanagement.common.dto.SchedulerDto;
import co.pla.portfoliomanagement.scheduler.application.dto.JobDetailDto;
import co.pla.portfoliomanagement.scheduler.application.dto.JobSummaryDto;
import co.pla.portfoliomanagement.scheduler.application.exceptions.JobDetailException;
import co.pla.portfoliomanagement.scheduler.application.service.SchedulerService;
import co.pla.portfoliomanagement.scheduler.domain.SchedulerContract;
import io.micrometer.common.util.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.quartz.TriggerKey.triggerKey;

@Service
@Slf4j
public class QuartzService implements SchedulerContract {

    public static final String JOB_GROUP_ID = "PORTFOLIO_JOBS";
    public static final String TRIGGER_GROUP_ID = "PORTFOLIO_TRIGGERS";
    public static final String CRON_TRIGGER_GROUP_ID = "PORTFOLIO_CRON_TRIGGERS";
    public static final String JOB_DETAIL_GROUP_ID = "QUARTZ_JOB_GROUP";
    public static final String JOB_DETAIL_DESCRIPTION = "QUARTZ_JOB_DESCRIPTION";
    public static final String TRIGGER_DESCRIPTION = "QUARTZ_TRIGGER_DESCRIPTION";
    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);
    private static final String JOB_PACKAGE_BASE = "co.pla.portfoliomanagement.scheduler.infrastructure.job.";
    private final Scheduler scheduler;

    public QuartzService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public JobDetail buildJobDetail(Class<? extends Job> jobClass, JobDataMap jobDataMap) {
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

    public Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt, int interval) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getJobDataMap().get("name").toString(), TRIGGER_GROUP_ID)
                .withDescription(TRIGGER_DESCRIPTION)
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(
                        interval).repeatForever())
                .build();
    }

    public CronTrigger buildJobTrigger(JobDetail jobDetail, String cronExpression) {
        /**
         * cronExpression -> e.g: "0 0/2 8-17 * * ?"
         */
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), CRON_TRIGGER_GROUP_ID)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
    }


    @Override
    @SneakyThrows
    public List<JobSummaryDto> getAllJobs() {
        return scheduler.getJobKeys(GroupMatcher.jobGroupEquals(JOB_GROUP_ID)).stream()
                .map(this::toJobSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public String updateScheduler(SchedulerDto schedulerDto, String jobId) {
        JobKey jobKey = new JobKey(jobId, JOB_DETAIL_GROUP_ID);
        var jobDataMap = getJobDataMap(schedulerDto);
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

    @Override
    @SneakyThrows
    public String scheduleJob(ZonedDateTime startTime, SchedulerDto schedulerDto, String jobClassName) {
        var jobDataMap = getJobDataMap(schedulerDto);
        JobDetail jobDetail = buildJobDetail(getJobClass(jobClassName), jobDataMap);
        Trigger trigger = buildJobTrigger(jobDetail, startTime, getInterval(jobDataMap));

        Date scheduledTime = scheduler.scheduleJob(jobDetail, trigger);
        log.info("Job scheduled successfully: {} at {}", jobDetail.getKey(), scheduledTime);

        return jobDetail.getKey().getName();
    }

    @Override
    @SneakyThrows
    public boolean isJobRunning(String jobId) {
        JobKey jobKey = new JobKey(jobId, JOB_GROUP_ID);
        List<JobExecutionContext> currentlyExecutingJobs = scheduler.getCurrentlyExecutingJobs();

        return currentlyExecutingJobs.stream()
                .anyMatch(context -> context.getJobDetail().getKey().equals(jobKey));
    }

    @Override
    @SneakyThrows
    public boolean resumeJob(String jobId) {
        JobKey jobKey = new JobKey(jobId, JOB_GROUP_ID);
        if (scheduler.checkExists(jobKey)) {
            scheduler.resumeJob(jobKey);
            log.info("Job resumed: {}", jobId);
            return true;
        }
        return false;
    }

    @Override
    @SneakyThrows
    public boolean pauseJob(String jobId) {
        JobKey jobKey = new JobKey(jobId, JOB_GROUP_ID);
        if (scheduler.checkExists(jobKey)) {
            scheduler.pauseJob(jobKey);
            log.info("Job paused: {}", jobId);
            return true;
        }
        return false;
    }

    @Override
    @SneakyThrows
    public Optional<JobDetailDto> getJobDetail(String jobId) {
        JobKey jobKey = new JobKey(jobId, JOB_GROUP_ID);

        if (!scheduler.checkExists(jobKey)) {
            return Optional.empty();
        }

        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        Trigger trigger = scheduler.getTriggersOfJob(jobKey).stream().findFirst().orElse(null);

        return Optional.of(JobDetailDto.from(jobDetail, trigger));
    }

    @Override
    @SneakyThrows
    public void shutSchedulerDown() {
        scheduler.shutdown();
    }

    @Override
    @SneakyThrows
    public boolean killJob(String id) {
        var jobKey = new JobKey(id, JOB_DETAIL_GROUP_ID);
        return scheduler.deleteJob(jobKey);
    }

    private Class<? extends Job> getJobClass(String className) throws ClassNotFoundException {
        return (Class<? extends Job>) Class.forName(JOB_PACKAGE_BASE + className);
    }

    private int getInterval(JobDataMap jobDataMap) {
        Object interval = jobDataMap.get("interval");
        if (interval instanceof Integer) {
            return (Integer) interval;
        }
        throw new JobDetailException("Interval must be an integer");
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

    private JobDataMap getJobDataMap(SchedulerDto schedulerDto) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("name", schedulerDto.getJobType());
        jobDataMap.put("interval", schedulerDto.getInterval());
        return jobDataMap;
    }
}
