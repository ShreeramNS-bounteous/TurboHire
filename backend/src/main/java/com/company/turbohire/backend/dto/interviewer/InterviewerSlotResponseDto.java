package com.company.turbohire.backend.dto.interviewer;

import com.company.turbohire.backend.enums.SlotStatus;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewerSlotResponseDto {
    private Long id;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private SlotStatus status;
}
