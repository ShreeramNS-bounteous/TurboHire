package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.dto.candidateJob.AddCandidateToPipelineRequest;
import com.company.turbohire.backend.dto.candidateJob.MoveStageRequest;
import com.company.turbohire.backend.dto.candidateJob.PipelineResponse;
import com.company.turbohire.backend.entity.CandidateJob;
import com.company.turbohire.backend.repository.CandidateJobRepository;
import com.company.turbohire.backend.services.CandidateJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pipeline")
@RequiredArgsConstructor
public class CandidateJobController {

    private final CandidateJobService candidateJobService;
    private final CandidateJobRepository candidateJobRepository;

    // ==============================
    // 1️⃣ ADD CANDIDATE TO PIPELINE
    // POST /api/pipeline
    // ==============================
    @PostMapping
    public PipelineResponse addCandidateToPipeline(
            @RequestBody AddCandidateToPipelineRequest request
    ) {

        Long actorUserId = 1L; // TODO: Replace with JWT user later

        Long candidateJobId = candidateJobService.addCandidateToPipeline(
                request.getCandidateId(),
                request.getJobId(),
                request.getBuId(),
                actorUserId
        );

        CandidateJob cj = candidateJobRepository.findById(candidateJobId)
                .orElseThrow(() -> new RuntimeException("CandidateJob not found"));

        return mapToResponse(cj);
    }

    // ==============================
    // 2️⃣ MOVE STAGE
    // PUT /api/pipeline/{id}/stage
    // ==============================
    @PutMapping("/{id}/stage")
    public PipelineResponse moveStage(
            @PathVariable Long id,
            @RequestBody MoveStageRequest request
    ) {

        Long actorUserId = 1L;

        candidateJobService.moveStage(id, request.getNextStage(), actorUserId);

        CandidateJob updated = candidateJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CandidateJob not found"));

        return mapToResponse(updated);
    }

    // ==============================
    // 3️⃣ REJECT CANDIDATE
    // PUT /api/pipeline/{id}/reject
    // ==============================
    @PutMapping("/{id}/reject")
    public PipelineResponse rejectCandidate(@PathVariable Long id) {

        Long actorUserId = 1L;

        candidateJobService.reject(id, actorUserId);

        CandidateJob updated = candidateJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CandidateJob not found"));

        return mapToResponse(updated);
    }

    // ==============================
    // 4️⃣ GET PIPELINE DETAILS
    // GET /api/pipeline/{id}
    // ==============================
    @GetMapping("/{id}")
    public PipelineResponse getPipeline(@PathVariable Long id) {

        CandidateJob cj = candidateJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CandidateJob not found"));

        return mapToResponse(cj);
    }

    // ==============================
    // 5️⃣ GET PIPELINE BY CANDIDATE
    // GET /api/pipeline/candidate/{candidateId}
    // ==============================
    @GetMapping("/candidate/{candidateId}")
    public List<PipelineResponse> getPipelineByCandidate(@PathVariable Long candidateId) {

        return candidateJobRepository.findByCandidate_Id(candidateId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ==============================
    // 6️⃣ GET PIPELINE BY JOB
    // GET /api/pipeline/job/{jobId}
    // ==============================
    @GetMapping("/job/{jobId}")
    public List<PipelineResponse> getPipelineByJob(@PathVariable Long jobId) {

        return candidateJobRepository.findByJob_Id(jobId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ==============================
    // 🔥 ENTITY → DTO MAPPING
    // ==============================
    private PipelineResponse mapToResponse(CandidateJob cj) {

        PipelineResponse dto = new PipelineResponse();

        dto.setCandidateJobId(cj.getId());

        dto.setCandidateId(cj.getCandidate().getId());
        dto.setCandidateName(cj.getCandidate().getFullName());

        dto.setJobId(cj.getJob().getId());
        dto.setJobTitle(cj.getJob().getTitle());

        dto.setBuId(cj.getBusinessUnit().getId());
        dto.setBusinessUnitName(cj.getBusinessUnit().getName());

        dto.setCurrentStage(cj.getCurrentStage());
        dto.setStatus(cj.getStatus());

        return dto;
    }
}
