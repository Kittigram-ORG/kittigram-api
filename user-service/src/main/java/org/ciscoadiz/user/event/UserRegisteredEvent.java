package org.ciscoadiz.user.event;

public record UserRegisteredEvent(
        Long userId,
        String email,
        String name,
        String activationToken
) {}