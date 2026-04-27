CREATE SCHEMA IF NOT EXISTS adoption;

CREATE SEQUENCE adoption."adoption_requests_SEQ" START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE adoption."adoption_forms_SEQ"    START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE adoption."expenses_SEQ"          START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE adoption."interviews_SEQ"        START WITH 1 INCREMENT BY 50;

CREATE TABLE adoption.adoption_requests (
    id               BIGINT       PRIMARY KEY DEFAULT nextval('adoption."adoption_requests_SEQ"'),
    cat_id           BIGINT       NOT NULL,
    adopter_email    VARCHAR(255) NOT NULL,
    adopter_id       BIGINT       NOT NULL,
    organization_id  BIGINT       NOT NULL,
    status           VARCHAR(50)  NOT NULL,
    notes            VARCHAR(255),
    rejection_reason VARCHAR(255),
    created_at       TIMESTAMP    NOT NULL,
    updated_at       TIMESTAMP    NOT NULL
);

CREATE TABLE adoption.adoption_forms (
    id                           BIGINT       PRIMARY KEY DEFAULT nextval('adoption."adoption_forms_SEQ"'),
    adoption_request_id          BIGINT       NOT NULL,
    full_name                    VARCHAR(255) NOT NULL,
    id_number                    VARCHAR(255) NOT NULL,
    phone                        VARCHAR(255) NOT NULL,
    address                      VARCHAR(255) NOT NULL,
    city                         VARCHAR(255) NOT NULL,
    postal_code                  VARCHAR(255) NOT NULL,
    accepts_vet_visits           BOOLEAN      NOT NULL,
    accepts_follow_up_contact    BOOLEAN      NOT NULL,
    accepts_return_if_needed     BOOLEAN      NOT NULL,
    accepts_terms_and_conditions BOOLEAN      NOT NULL,
    additional_notes             TEXT,
    signed_adoption_contract     BOOLEAN      NOT NULL,
    contract_signed_at           TIMESTAMP,
    created_at                   TIMESTAMP    NOT NULL,
    updated_at                   TIMESTAMP    NOT NULL
);

CREATE TABLE adoption.expenses (
    id                  BIGINT         PRIMARY KEY DEFAULT nextval('adoption."expenses_SEQ"'),
    adoption_request_id BIGINT         NOT NULL,
    concept             VARCHAR(255)   NOT NULL,
    amount              NUMERIC(10, 2) NOT NULL,
    recipient           VARCHAR(50)    NOT NULL,
    paid                BOOLEAN        NOT NULL DEFAULT FALSE,
    paid_at             TIMESTAMP,
    created_at          TIMESTAMP      NOT NULL,
    updated_at          TIMESTAMP      NOT NULL
);

CREATE TABLE adoption.interviews (
    id                  BIGINT       PRIMARY KEY DEFAULT nextval('adoption."interviews_SEQ"'),
    adoption_request_id BIGINT       NOT NULL,
    scheduled_at        TIMESTAMP    NOT NULL,
    notes               VARCHAR(255),
    passed              BOOLEAN,
    created_at          TIMESTAMP    NOT NULL,
    updated_at          TIMESTAMP    NOT NULL
);
