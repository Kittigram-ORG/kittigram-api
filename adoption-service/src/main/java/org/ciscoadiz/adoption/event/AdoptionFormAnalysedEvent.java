package org.ciscoadiz.adoption.event;

public record AdoptionFormAnalysedEvent(
        Long adoptionRequestId,
        String decision,
        String rejectionReason,
        String adopterEmail,
        int criticalFlags,
        int warningFlags,
        int noticeFlags
) {}