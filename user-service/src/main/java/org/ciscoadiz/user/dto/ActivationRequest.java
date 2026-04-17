package org.ciscoadiz.user.dto;

import jakarta.validation.constraints.NotBlank;

public record ActivationRequest(@NotBlank String token) {
}
