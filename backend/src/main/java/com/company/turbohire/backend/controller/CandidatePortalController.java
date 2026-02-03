package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.dto.candidatePortal.CandidatePortalResponse;
import com.company.turbohire.backend.services.CandidatePortalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/candidate-portal")
@RequiredArgsConstructor
public class CandidatePortalController {

    private final CandidatePortalService portalService;

    @GetMapping
    public CandidatePortalResponse getPortal(
            @RequestParam String token
    ) {
        return portalService.getPortalByToken(token);
    }
}
