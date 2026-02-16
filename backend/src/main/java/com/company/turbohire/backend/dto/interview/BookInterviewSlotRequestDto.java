package com.company.turbohire.backend.dto.interview;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BookInterviewSlotRequestDto {
    private Long interviewerSlotId;
    private String meetingUrl;
}

