package com.company.turbohire.backend.dto.interviewer;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AddInterviewerSlotRequestDto {

    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
