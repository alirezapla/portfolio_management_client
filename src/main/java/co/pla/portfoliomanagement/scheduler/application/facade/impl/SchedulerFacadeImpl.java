package co.pla.portfoliomanagement.scheduler.application.facade.impl;

import co.pla.portfoliomanagement.common.dto.SchedulerDto;
import co.pla.portfoliomanagement.scheduler.application.dto.JobDetailDto;
import co.pla.portfoliomanagement.scheduler.application.dto.JobSummaryDto;
import co.pla.portfoliomanagement.scheduler.application.facade.SchedulerFacade;
import co.pla.portfoliomanagement.scheduler.infrastrucrue.job.JobEnum;
import co.pla.portfoliomanagement.scheduler.application.service.SchedulerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchedulerFacadeImpl implements SchedulerFacade {
    private final SchedulerService schedulerService;

    public SchedulerFacadeImpl(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @Override
    public String createScheduler(SchedulerDto schedulerDto) {
        return schedulerService.scheduleJob(schedulerDto);
    }

    @Override
    public String updateScheduler(SchedulerDto schedulerDto, String id) {
        return schedulerService.updateScheduler(schedulerDto, id);
    }

    @Override
    public Optional<JobDetailDto> getJob(String id) {
        return schedulerService.getJobDetail(id);
    }

    @Override
    public List<JobSummaryDto> getAllJobs() {
        return schedulerService.getAllJobs();
    }

    @Override
    public Object removeJob(String id) {
        return schedulerService.killJob(id);
    }

    @Override
    public JobEnum[] getEnum() {
        return JobEnum.values();
    }

}
