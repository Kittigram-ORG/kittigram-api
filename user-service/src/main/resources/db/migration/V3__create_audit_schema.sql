CREATE SCHEMA IF NOT EXISTS audit;

CREATE SEQUENCE audit."erasure_requests_SEQ" START WITH 1 INCREMENT BY 50;

-- Registro inmutable de solicitudes de borrado (Art. 5.2 RGPD — accountability).
-- Ni el usuario ni un admin pueden eliminarlo; sirve como evidencia forense.
CREATE TABLE audit.erasure_requests
(
    id                  BIGINT    PRIMARY KEY DEFAULT nextval('audit."erasure_requests_SEQ"'),
    user_id             BIGINT    NOT NULL,
    requested_at        TIMESTAMP NOT NULL,
    requested_ip        VARCHAR(45),
    scheduled_purge_at  TIMESTAMP NOT NULL,
    purged_at           TIMESTAMP,
    blocked_by_hold     BOOLEAN   NOT NULL DEFAULT FALSE,
    hold_lifted_at      TIMESTAMP
);

CREATE INDEX idx_erasure_requests_user_id      ON audit.erasure_requests (user_id);
CREATE INDEX idx_erasure_requests_purge_pending ON audit.erasure_requests (scheduled_purge_at)
    WHERE purged_at IS NULL;
