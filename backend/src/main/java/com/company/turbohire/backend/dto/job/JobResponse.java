package com.company.turbohire.backend.dto.job;

import com.company.turbohire.backend.entity.Job;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobResponse {

    private Long id;
    private String title;
    private Long buId;
    private String department;
    private Integer experienceMin;
    private Integer experienceMax;
    private String location;
    private String status;
    private LocalDateTime createdAt;

    public static JobResponse from(Job job) {
        JobResponse res = new JobResponse();
        res.setId(job.getId());
        res.setTitle(job.getTitle());
        res.setBuId(job.getBusinessUnit().getId());
        res.setDepartment(job.getDepartment());
        res.setExperienceMin(job.getExperienceMin());
        res.setExperienceMax(job.getExperienceMax());
        res.setLocation(job.getLocation());
        res.setStatus(job.getStatus());
        res.setCreatedAt(job.getCreatedAt());
        return res;
    }
}
