package co.pla.portfoliomanagement.scheduler.facade;

import co.pla.portfoliomanagement.scheduler.dto.JobDetailDto;
import co.pla.portfoliomanagement.scheduler.dto.JobSummaryDto;
import co.pla.portfoliomanagement.scheduler.job.JobEnum;
import co.pla.portfoliomanagement.common.dto.SchedulerDto;
import org.quartz.SchedulerException;

import java.util.List;
import java.util.Optional;

public interface SchedulerFacade {
    String createScheduler(SchedulerDto schedulerDto);

    String updateScheduler(SchedulerDto schedulerDto, String id) throws SchedulerException;

    Optional<JobDetailDto> getJob(String id) throws SchedulerException;

    List<JobSummaryDto> getAllJobs() throws SchedulerException;

    Object removeJob(String id) throws SchedulerException;

    JobEnum[] getEnum();
}
