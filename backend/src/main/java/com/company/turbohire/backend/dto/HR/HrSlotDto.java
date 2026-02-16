package com.company.turbohire.backend.dto.HR;


import java.time.LocalTime;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HrSlotDto {
    private Long slotId;
    private LocalTime startTime;
    private LocalTime endTime;
}


