package co.pla.portfoliomanagement.gateway.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}