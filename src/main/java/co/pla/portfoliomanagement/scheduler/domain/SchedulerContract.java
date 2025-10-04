package co.pla.portfoliomanagement.scheduler.domain;

import co.pla.portfoliomanagement.scheduler.application.dto.JobDetailDto;
import co.pla.portfoliomanagement.scheduler.application.dto.JobSummaryDto;
import org.quartz.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface SchedulerContract {

    JobDetail buildJobDetail(Class<? extends Job> jobClass, JobDataMap jobDataMap);

    Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt, int interval);

    CronTrigger buildJobTrigger(JobDetail jobDetail, String cronExpression);

    List<JobSummaryDto> getAllJobs();

    String updateScheduler(JobDataMap jobDataMap, String jobId);

    String scheduleJob(ZonedDateTime startTime, JobDataMap jobDataMap, String jobClassName);

    boolean isJobRunning(String jobId);

    boolean resumeJob(String jobId);

    boolean pauseJob(String jobId);

    Optional<JobDetailDto> getJobDetail(String jobId);

    void shutSchedulerDown();

    boolean killJob(String id);
}
