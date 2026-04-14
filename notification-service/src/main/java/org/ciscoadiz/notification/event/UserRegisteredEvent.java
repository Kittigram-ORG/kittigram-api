package org.ciscoadiz.notification.event;

public record UserRegisteredEvent(
        Long userId,
        String email,
        String name,
        String activationToken
) {}