package com.company.turbohire.backend.dto.interviewer;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewerProfileResponseDto {
    private Long id;
    private String expertise;
    private String timezone;
    private String status;
}
