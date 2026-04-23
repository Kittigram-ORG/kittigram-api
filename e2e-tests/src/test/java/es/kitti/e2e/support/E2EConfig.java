package es.kitti.e2e.support;

import io.restassured.RestAssured;
import org.awaitility.Awaitility;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class E2EConfig {

    public static final String GATEWAY_URL = System.getenv().getOrDefault("GATEWAY_URL", "http://localhost:8080");
    public static final String MAILHOG_URL = System.getenv().getOrDefault("MAILHOG_URL", "http://localhost:8025");
    public static final String FRONTEND_URL = System.getenv().getOrDefault("FRONTEND_URL", "http://localhost:5173");

    // Set -De2e.skip.wait=true in your IDE run config to skip stack polling (assumes stack is already up)
    private static final boolean SKIP_WAIT = Boolean.getBoolean("e2e.skip.wait");
    // Override with -De2e.wait.timeout=<seconds> (default 120 for CI)
    private static final int WAIT_TIMEOUT_SEC = Integer.getInteger("e2e.wait.timeout", 120);

    // 4-second per-request timeout — avoids hanging when the downstream service isn't ready
    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(4))
            .build();

    private static final AtomicBoolean STACK_READY = new AtomicBoolean(false);

    /**
     * Polls the gateway with a hard per-request timeout so that a hanging upstream
     * (cat-service initialising its DB schema) doesn't block the Awaitility loop.
     * Two phases: (1) cat-service → 200, (2) auth-service → 401 on bad creds.
     * Idempotent: the second and subsequent callers return immediately.
     *
     * Fast-path: if both services respond on the first attempt the Awaitility loops
     * are skipped entirely — typical latency when the stack is already up is ~1 s.
     * Use -De2e.skip.wait=true to bypass all polling (IDE debugging with stack running).
     */
    public static void waitForStack() {
        RestAssured.baseURI = GATEWAY_URL;
        if (STACK_READY.get()) return;

        if (SKIP_WAIT) {
            STACK_READY.set(true);
            return;
        }

        // Fast-path: single attempt before starting the full polling loop
        boolean catsUp = httpGet(GATEWAY_URL + "/api/cats") == 200;
        if (catsUp) {
            int authStatus = httpPost(GATEWAY_URL + "/api/auth/login",
                    "{\"email\":\"probe@e2e.ready\",\"password\":\"probe\"}");
            if (authStatus == 401 || authStatus == 400) {
                STACK_READY.set(true);
                return;
            }
        }

        // Phase 1: GET /api/cats → 200 (gateway + cat-service + DB ready)
        Awaitility.await("cat-service ready")
                .atMost(WAIT_TIMEOUT_SEC, TimeUnit.SECONDS)
                .pollInterval(3, TimeUnit.SECONDS)
                .until(() -> httpGet(GATEWAY_URL + "/api/cats") == 200);

        // Phase 2: POST /api/auth/login with bad creds → 401 (auth + user-service + Kafka ready)
        Awaitility.await("auth-service ready")
                .atMost(60, TimeUnit.SECONDS)
                .pollInterval(3, TimeUnit.SECONDS)
                .until(() -> {
                    int s = httpPost(GATEWAY_URL + "/api/auth/login",
                            "{\"email\":\"probe@e2e.ready\",\"password\":\"probe\"}");
                    return s == 401 || s == 400;
                });

        STACK_READY.set(true);
    }

    /** GET with a 4-second read timeout; returns -1 on any error. */
    private static int httpGet(String url) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(4))
                    .GET()
                    .build();
            return HTTP.send(req, HttpResponse.BodyHandlers.discarding()).statusCode();
        } catch (Exception e) {
            return -1;
        }
    }

    /** POST JSON with a 4-second read timeout; returns -1 on any error. */
    private static int httpPost(String url, String json) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(4))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            return HTTP.send(req, HttpResponse.BodyHandlers.discarding()).statusCode();
        } catch (Exception e) {
            return -1;
        }
    }

    private E2EConfig() {}
}
