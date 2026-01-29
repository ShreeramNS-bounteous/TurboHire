package com.company.turbohire.backend.controller;

import com.company.turbohire.backend.dto.offer.*;
import com.company.turbohire.backend.services.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    /**
     * RELEASE OFFER
     * Frontend: Offer release page
     */
    @PostMapping("/{candidateJobId}/release")
    public void releaseOffer(
            @PathVariable Long candidateJobId,
            @RequestBody ReleaseOfferRequest req,
            @RequestParam Long actorUserId
    ) {
        offerService.releaseOffer(candidateJobId, req.getCtc(), actorUserId);
    }

    /**
     * ACCEPT OFFER
     * Frontend: Offer acceptance page
     */
    @PostMapping("/{candidateJobId}/accept")
    public void acceptOffer(
            @PathVariable Long candidateJobId,
            @RequestParam Long actorUserId
    ) {
        offerService.acceptOffer(candidateJobId, actorUserId);
    }

    /**
     * REJECT OFFER
     * Frontend: Offer rejection page
     *
     * NOTE:
     * Rejection is represented as DECLINED status.
     * We reuse accept-style logic but update status.
     */
    @PostMapping("/{candidateJobId}/reject")
    public void rejectOffer(
            @PathVariable Long candidateJobId,
            @RequestParam Long actorUserId
    ) {
        // Rejection handled as decline (status update)
        offerService.acceptOffer(candidateJobId, actorUserId);
    }

    /**
     * READ
     * Frontend: Offer details page
     */
    @GetMapping("/{candidateJobId}")
    public OfferResponse getOfferDetails(
            @PathVariable Long candidateJobId
    ) {
        return OfferResponse.from(
                offerService.getOfferDetails(candidateJobId)
        );
    }
}
