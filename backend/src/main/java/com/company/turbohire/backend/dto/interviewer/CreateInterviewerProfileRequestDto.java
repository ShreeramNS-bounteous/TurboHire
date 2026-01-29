package com.company.turbohire.backend.dto.interviewer;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInterviewerProfileRequestDto {
    private Long userId;
    private String expertise;
    private String timezone;
    private Long actorUserId; // the user performing the action
}
