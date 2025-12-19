package co.pla.portfoliomanagement.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobSummaryDto {
    private String jobId;
    private String jobGroup;
    private String jobName;
    private String jobClass;
}