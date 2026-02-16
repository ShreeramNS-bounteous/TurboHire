package com.company.turbohire.backend.dto.candidate;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CandidateSimpleDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
}
