package co.pla.portfoliomanagement.scheduler.application.facade;

import co.pla.portfoliomanagement.scheduler.application.dto.JobDetailDto;
import co.pla.portfoliomanagement.scheduler.application.dto.JobSummaryDto;
import co.pla.portfoliomanagement.scheduler.infrastructure.job.JobEnum;
import co.pla.portfoliomanagement.common.dto.SchedulerDto;

import java.util.List;
import java.util.Optional;

public interface SchedulerFacade {
    String createScheduler(SchedulerDto schedulerDto);

    String updateScheduler(SchedulerDto schedulerDto, String id);

    Optional<JobDetailDto> getJob(String id);

    List<JobSummaryDto> getAllJobs();

    Object removeJob(String id);

    JobEnum[] getEnum();
}
