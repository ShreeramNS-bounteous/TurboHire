package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.dto.interviewer.*;
import com.company.turbohire.backend.entity.InterviewerProfile;
import com.company.turbohire.backend.entity.InterviewerSlot;
import com.company.turbohire.backend.enums.SlotStatus;
import com.company.turbohire.backend.security.util.SecurityUtils;
import com.company.turbohire.backend.services.InterviewerService;
import com.company.turbohire.backend.repository.InterviewerSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/interviewers")
@RequiredArgsConstructor
public class InterviewerController {

    private final InterviewerService interviewerService;
    private final InterviewerSlotRepository interviewerSlotRepository;

    // ================= HR VIEW SLOTS =================
    @GetMapping("/hr/slots")
    @PreAuthorize("hasRole('RECRUITER')")
    public List<InterviewerSlotResponseDto> slotsForHr() {

        Long hrId = SecurityUtils.getCurrentUserId();

        return interviewerSlotRepository
                .findByVisibleToHrIdAndStatus(hrId, SlotStatus.AVAILABLE)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // ================= INTERVIEWER POST SLOT =================
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{id}/slots")
    public ResponseEntity<InterviewerSlotResponseDto> addSlot(
            @PathVariable Long id,
            @RequestBody AddInterviewerSlotRequestDto request
    ) {

        InterviewerSlot slot = interviewerService.addInterviewerSlot(
                id,
                request.getSlotDate(),
                request.getStartTime(),
                request.getEndTime(),
                SecurityUtils.getCurrentUserId(),
                request.getHrId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapToDto(slot));
    }

    // ================= PROFILE APIS =================

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping
    public ResponseEntity<List<InterviewerProfileResponseDto>> listInterviewers() {

        List<InterviewerProfileResponseDto> list =
                interviewerService.getAllInterviewerProfiles()
                        .stream()
                        .map(this::mapToDto)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasAnyRole('RECRUITER','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<InterviewerProfileResponseDto> getInterviewerProfile(
            @PathVariable Long id) {

        InterviewerProfile profile =
                interviewerService.getInterviewerProfile(id);

        return ResponseEntity.ok(mapToDto(profile));
    }

    // ================= MAPPERS =================

    private InterviewerSlotResponseDto mapToDto(InterviewerSlot s) {
        return InterviewerSlotResponseDto.builder()
                .id(s.getId())
                .slotDate(s.getSlotDate())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .status(s.getStatus())
                .build();
    }

    private InterviewerProfileResponseDto mapToDto(InterviewerProfile p) {
        return InterviewerProfileResponseDto.builder()
                .id(p.getId())
                .expertise(p.getExpertise())
                .timezone(p.getTimezone())
                .status(p.getStatus())
                .build();
    }
}
