package com.company.turbohire.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "business_unit",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "bu_name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessUnit {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(name = "bu_name",nullable = false,unique = true)
private String name;

@ManyToOne
@JoinColumn(name = "bu_head_user_id")
private User buHead;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
