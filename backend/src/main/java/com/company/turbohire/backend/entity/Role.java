package com.company.turbohire.backend.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "role",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "role_name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(name = "role_name",nullable = false,unique = true)
    private String name;
}
