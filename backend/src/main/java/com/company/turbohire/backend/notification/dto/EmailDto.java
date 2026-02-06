package com.company.turbohire.backend.notification.dto;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class EmailDto {
    private String to;
    private String subject;
    private String body;
}
