package co.pla.portfoliomanagement.scheduler.application.dto;

import lombok.Builder;
import lombok.Data;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Data
@Builder
public class JobDetailDto {
    private String jobId;
    private String jobGroup;
    private String jobName;
    private String jobClassName;
    private Map<String, Object> jobData;
    private LocalDateTime nextFireTime;
    private LocalDateTime previousFireTime;

    public static JobDetailDto from(JobDetail jobDetail, Trigger trigger) {
        JobDetailDto dto = JobDetailDto.builder()
                .jobId(jobDetail.getKey().getName())
                .jobGroup(jobDetail.getKey().getGroup())
                .jobName(jobDetail.getJobDataMap().getString("name"))
                .jobClassName(jobDetail.getJobClass().getSimpleName())
                .jobData(jobDetail.getJobDataMap().getWrappedMap())
                .build();

        if (trigger != null) {
            dto.setNextFireTime(trigger.getNextFireTime().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
            dto.setPreviousFireTime(trigger.getPreviousFireTime() != null ?
                    trigger.getPreviousFireTime().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDateTime() : null);
        }

        return dto;
    }
}
