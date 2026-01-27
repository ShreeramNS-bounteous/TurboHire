package com.company.turbohire.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "interview_assignment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewAssignment {

    @EmbeddedId
    private InterviewAssignmentId id;

    @ManyToOne(optional = false)
    @MapsId("interviewId")
    @JoinColumn(name = "interview_id")
    private Interview interview;

    @ManyToOne(optional = false)
    @MapsId("interviewerUserId")
    @JoinColumn(name = "interviewer_user_id")
    private User interviewer;
}
