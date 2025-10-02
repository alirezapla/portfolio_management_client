package co.pla.portfoliomanagement.scheduler.facade.impl;

import co.pla.portfoliomanagement.common.dto.SchedulerDto;
import co.pla.portfoliomanagement.scheduler.dto.JobDetailDto;
import co.pla.portfoliomanagement.scheduler.dto.JobSummaryDto;
import co.pla.portfoliomanagement.scheduler.exceptions.JobDetailException;
import co.pla.portfoliomanagement.scheduler.facade.SchedulerFacade;
import co.pla.portfoliomanagement.scheduler.job.JobEnum;
import co.pla.portfoliomanagement.scheduler.service.QuartzSchedulerService;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SchedulerFacadeImpl implements SchedulerFacade {
    private final QuartzSchedulerService schedulerService;

    public SchedulerFacadeImpl(QuartzSchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @Override
    public String createScheduler(SchedulerDto schedulerDto) {
        try {
            JobDataMap jobDataMap = getJobDataMap(schedulerDto);
            ZonedDateTime dateTime = ZonedDateTime.of(schedulerDto.getDateTime(), ZoneId.of("Asia/Tehran"));
            return schedulerService.scheduleJob(dateTime, jobDataMap, schedulerDto.getJobType());
        } catch (SchedulerException | ClassNotFoundException se) {
            throw new JobDetailException(se.getMessage());
        }
    }

    @Override
    public String updateScheduler(SchedulerDto schedulerDto ,String id) throws SchedulerException {
       return "updateScheduler";
    }

    @Override
    public Optional<JobDetailDto> getJob(String id) throws SchedulerException {
        return schedulerService.getJobDetail(id);
    }

    @Override
    public List<JobSummaryDto> getAllJobs() throws SchedulerException {
        return schedulerService.getAllJobs();
    }

    @Override
    public Object removeJob(String id) throws SchedulerException {
        return schedulerService.killJob(id);
    }

    @Override
    public JobEnum[] getEnum() {
        return JobEnum.values();
    }

    private JobDataMap getJobDataMap(SchedulerDto schedulerDto) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("name", schedulerDto.getJobType());
        jobDataMap.put("interval", schedulerDto.getInterval());
        return jobDataMap;
    }
}
