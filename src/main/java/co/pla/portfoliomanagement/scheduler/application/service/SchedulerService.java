package co.pla.portfoliomanagement.scheduler.application.service;

import co.pla.portfoliomanagement.common.dto.SchedulerDto;
import co.pla.portfoliomanagement.scheduler.application.dto.JobDetailDto;
import co.pla.portfoliomanagement.scheduler.application.dto.JobSummaryDto;
import co.pla.portfoliomanagement.scheduler.application.exceptions.JobDetailException;
import co.pla.portfoliomanagement.scheduler.domain.SchedulerContract;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;


@Service
@Slf4j
public class SchedulerService {
    private final SchedulerContract scheduler;

    public SchedulerService(SchedulerContract scheduler) {
        this.scheduler = scheduler;
    }

    public String scheduleJob(SchedulerDto schedulerDto) {
        LocalDateTime startDateTime = schedulerDto.getDateTime();
        if (Objects.isNull(startDateTime)){
            startDateTime = LocalDateTime.now().plusMinutes(1);
        }
        ZonedDateTime startTime = ZonedDateTime.of(startDateTime, ZoneId.of("Asia/Tehran"));
        validateScheduleRequest(startTime, schedulerDto, schedulerDto.getJobType());
        return scheduler.scheduleJob(startTime, schedulerDto, schedulerDto.getJobType());
    }

    public String updateScheduler(SchedulerDto schedulerDto, String jobId) {
        // validation (TODO)
        return scheduler.updateScheduler(schedulerDto, jobId);
    }

    public List<JobSummaryDto> getAllJobs(){
        return scheduler.getAllJobs();
    }

    public boolean killJob(String jobId) {
        return scheduler.killJob(jobId);
    }

    public Optional<JobDetailDto> getJobDetail(String jobId) {
        return scheduler.getJobDetail(jobId);
    }

    private void validateScheduleRequest(ZonedDateTime startTime, SchedulerDto schedulerDto, String jobClassName) {
        if (startTime == null) {
            throw new JobDetailException("Start time cannot be null");
        }
        if (Objects.isNull(schedulerDto)) {
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
}
