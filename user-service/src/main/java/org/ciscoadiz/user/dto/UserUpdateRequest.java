package org.ciscoadiz.user.dto;

import java.time.LocalDate;

public record UserUpdateRequest(
   String name,
   String surname,
   LocalDate birthdate
) {}
