package co.pla.portfoliomanagement.scheduler.domain;

import co.pla.portfoliomanagement.common.dto.SchedulerDto;
import co.pla.portfoliomanagement.scheduler.application.dto.JobDetailDto;
import co.pla.portfoliomanagement.scheduler.application.dto.JobSummaryDto;
import org.quartz.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface SchedulerContract {

    List<JobSummaryDto> getAllJobs();

    String updateScheduler(SchedulerDto schedulerDto, String jobId);

    String scheduleJob(ZonedDateTime startTime, SchedulerDto schedulerDto, String jobClassName);

    boolean isJobRunning(String jobId);

    boolean resumeJob(String jobId);

    boolean pauseJob(String jobId);

    Optional<JobDetailDto> getJobDetail(String jobId);

    void shutSchedulerDown();

    boolean killJob(String id);
}
