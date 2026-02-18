package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.dto.HR.HrInterviewerAvailabilityDto;
import com.company.turbohire.backend.dto.interviewer.*;
import com.company.turbohire.backend.entity.InterviewerProfile;
import com.company.turbohire.backend.entity.InterviewerSlot;
import com.company.turbohire.backend.services.InterviewerService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/interviewers")
@RequiredArgsConstructor
public class InterviewerController {

    private final InterviewerService interviewerService;

    // -------- EMPLOYEE / INTERVIEWER PROFILE --------

    @PreAuthorize("hasRole('RECRUITER')")
    @PostMapping
    public ResponseEntity<InterviewerProfileResponseDto> createProfile(
            @RequestBody CreateInterviewerProfileRequestDto request
    ) {
        InterviewerProfile profile = interviewerService.createOrUpdateProfile(
                request.getUserId(),
                request.getExpertise(),
                request.getExperienceYears(),
                request.getDepartment(),
                request.isInterviewer()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mapProfile(profile));
    }

    @PreAuthorize("hasAnyRole('RECRUITER','USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<InterviewerProfileResponseDto> getProfile(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                mapProfile(interviewerService.getProfileByUserId(userId))
        );
    }

    // -------- AVAILABILITY (EMPLOYEE BASED) --------

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{userId}/slots")
    public ResponseEntity<InterviewerSlotResponseDto> addSlot(
            @PathVariable Long userId,
            @RequestBody AddInterviewerSlotRequestDto request
    ) {
        InterviewerSlot slot = interviewerService.addSlot(
                userId,
                request.getSlotDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mapSlot(slot));
    }

    @PreAuthorize("hasAnyRole('RECRUITER','USER')")
    @GetMapping("/{userId}/slots")
    public ResponseEntity<List<InterviewerSlotResponseDto>> getAllSlots(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                interviewerService.getAllSlots(userId)
                        .stream()
                        .map(this::mapSlot)
                        .collect(Collectors.toList())
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-interviews")
    public ResponseEntity<List<MyInterviewDto>> getMyInterviews() {
        return ResponseEntity.ok(
                interviewerService.getMyInterviews()
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<InterviewerNavbarDto> getMyNavbarDetails() {

        return ResponseEntity.ok(
                interviewerService.getNavbarDetails()
        );
    }



    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/availability")
    public ResponseEntity<List<HrInterviewerAvailabilityDto>> getInterviewerAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime to
    ) {
        return ResponseEntity.ok(
                interviewerService.getAvailableInterviewersForHr(date, from, to)
        );
    }


    @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/{userId}/slots/available")
    public ResponseEntity<List<InterviewerSlotResponseDto>> getAvailableSlots(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                interviewerService.getAvailableSlots(userId)
                        .stream()
                        .map(this::mapSlot)
                        .collect(Collectors.toList())
        );
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/slots/{slotId}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long slotId) {
        interviewerService.deleteSlot(slotId);
        return ResponseEntity.ok().build();
    }

    // -------- MAPPERS --------

    private InterviewerProfileResponseDto mapProfile(InterviewerProfile p) {
        return InterviewerProfileResponseDto.builder()
                .id(p.getId())
                .userId(p.getUserId())
                .expertise(p.getExpertise())
                .experienceYears(p.getExperienceYears())
                .isInterviewer(p.isInterviewer())
                .build();
    }

    private InterviewerSlotResponseDto mapSlot(InterviewerSlot s) {
        return InterviewerSlotResponseDto.builder()
                .id(s.getId())
                .slotDate(s.getSlotDate())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .status(s.getStatus())
                .build();
    }
}
