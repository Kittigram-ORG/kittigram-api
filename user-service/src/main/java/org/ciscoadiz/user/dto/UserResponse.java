package org.ciscoadiz.user.dto;

import org.ciscoadiz.user.entity.UserRole;
import org.ciscoadiz.user.entity.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String name,
        String surname,
        UserStatus status,
        UserRole role,
        LocalDate birthdate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }