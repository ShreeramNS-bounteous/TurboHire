package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.dto.interviewer.*;
import com.company.turbohire.backend.entity.InterviewerProfile;
import com.company.turbohire.backend.entity.InterviewerSlot;
import com.company.turbohire.backend.services.InterviewerService;
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

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping
    public ResponseEntity<InterviewerProfileResponseDto> createInterviewerProfile(
            @RequestBody CreateInterviewerProfileRequestDto request
    ) {
        InterviewerProfile profile = interviewerService.createInterviewerProfile(
                request.getUserId(),
                request.getExpertise(),
                request.getTimezone(),
                request.getActorUserId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(profile));
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping
    public ResponseEntity<List<InterviewerProfileResponseDto>> listInterviewers() {
        List<InterviewerProfileResponseDto> list = interviewerService.getAllInterviewerProfiles().stream()
                .map(this::mapToDto).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasAnyRole('RECRUITER','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<InterviewerProfileResponseDto> getInterviewerProfile(@PathVariable Long id) {
        InterviewerProfile profile = interviewerService.getInterviewerProfile(id);
        return ResponseEntity.ok(mapToDto(profile));
    }

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
                request.getActorUserId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(slot));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/slots/{slotId}")
    public ResponseEntity<Void> removeSlot(
            @PathVariable Long slotId,
            @RequestParam Long actorUserId
    ) {
        interviewerService.removeInterviewerSlot(slotId, actorUserId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('RECRUITER','USER')")
    @GetMapping("/{id}/slots")
    public ResponseEntity<List<InterviewerSlotResponseDto>> getAllSlots(@PathVariable Long id) {
        List<InterviewerSlotResponseDto> list = interviewerService.getAllSlots(id).stream()
                .map(this::mapToDto).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/{id}/slots/available")
    public ResponseEntity<List<InterviewerSlotResponseDto>> getAvailableSlots(@PathVariable Long id) {
        List<InterviewerSlotResponseDto> list = interviewerService.getAvailableSlots(id).stream()
                .map(this::mapToDto).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    private InterviewerProfileResponseDto mapToDto(InterviewerProfile p) {
        return InterviewerProfileResponseDto.builder()
                .id(p.getId())
                .expertise(p.getExpertise())
                .timezone(p.getTimezone())
                .status(p.getStatus())
                .build();
    }

    private InterviewerSlotResponseDto mapToDto(InterviewerSlot s) {
        return InterviewerSlotResponseDto.builder()
                .id(s.getId())
                .slotDate(s.getSlotDate())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .status(s.getStatus())
                .build();
    }
}
