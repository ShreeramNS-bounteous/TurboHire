package com.company.turbohire.backend.dto.interviewer;

import com.company.turbohire.backend.enums.SlotStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class InterviewerSlotResponseDto {

    private Long id;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private SlotStatus status;
}
