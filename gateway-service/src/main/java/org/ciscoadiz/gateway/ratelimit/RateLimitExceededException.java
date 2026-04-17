package org.ciscoadiz.gateway.ratelimit;

public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException() {
        super("Too many requests");
    }
}
