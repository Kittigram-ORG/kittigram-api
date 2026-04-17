package org.ciscoadiz.e2e.support;

public class E2EConfig {

    public static final String GATEWAY_URL = System.getenv().getOrDefault("GATEWAY_URL", "http://localhost:8080");
    public static final String MAILHOG_URL = System.getenv().getOrDefault("MAILHOG_URL", "http://localhost:8025");
    public static final String FRONTEND_URL = System.getenv().getOrDefault("FRONTEND_URL", "http://localhost:5173");

    private E2EConfig() {}
}
