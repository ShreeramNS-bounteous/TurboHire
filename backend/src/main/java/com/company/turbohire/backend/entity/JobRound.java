package com.company.turbohire.backend.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_round")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(name = "round_name",nullable = false)
    private String roundName;

    @Column(name="round_order",nullable = false)
    private Integer roundOrder;

<<<<<<< HEAD
=======
    @Column(name = "evaluation_template_code")
    private String evaluationTemplateCode;


>>>>>>> f83d421 (Recovered local changes after accidental .git deletion)

}
