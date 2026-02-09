package com.company.turbohire.backend.dto.interviewer;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddInterviewerSlotRequestDto {
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long actorUserId; // the user performing the action
}
