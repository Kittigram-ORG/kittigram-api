package org.ciscoadiz.user.dto;

import org.ciscoadiz.user.entity.UserRole;

import java.time.LocalDate;

public record UserCreateRequest(
        String email,
        String password,
        String name,
        String surname,
        LocalDate birthdate,
        String status,
        UserRole role
) { }
