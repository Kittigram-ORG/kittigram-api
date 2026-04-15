package org.ciscoadiz.adoption.dto;

import java.time.LocalDateTime;

public record InterviewCreateRequest(
        LocalDateTime scheduledAt,
        String notes
) {}