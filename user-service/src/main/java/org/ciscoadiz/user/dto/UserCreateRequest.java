package org.ciscoadiz.user.dto;

import java.time.LocalDate;

public record UserCreateRequest(
        String email,
        String password,
        String name,
        String surname,
        LocalDate birthdate,
        String status
) { }
