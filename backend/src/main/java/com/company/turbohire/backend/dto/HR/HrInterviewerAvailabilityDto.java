package com.company.turbohire.backend.dto.HR;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HrInterviewerAvailabilityDto {
    private Long userId;
    private String userName;
    private String expertise;
    private Integer experienceYears;
    private List<HrSlotDto> slots;
}
