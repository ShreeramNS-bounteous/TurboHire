package com.company.turbohire.backend.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewAssignmentId implements Serializable {

    private Long interviewId;
    private Long interviewerUserId;
}
