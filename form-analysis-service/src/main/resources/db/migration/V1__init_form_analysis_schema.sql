CREATE SCHEMA IF NOT EXISTS form_analysis;

CREATE SEQUENCE form_analysis."form_analyses_SEQ" START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE form_analysis."form_flags_SEQ"    START WITH 1 INCREMENT BY 50;

CREATE TABLE form_analysis.form_analyses (
    id                  BIGINT       PRIMARY KEY DEFAULT nextval('form_analysis."form_analyses_SEQ"'),
    adoption_request_id BIGINT       NOT NULL,
    decision            VARCHAR(50)  NOT NULL,
    rejection_reason    VARCHAR(255),
    critical_flags      INTEGER      NOT NULL,
    warning_flags       INTEGER      NOT NULL,
    notice_flags        INTEGER      NOT NULL,
    created_at          TIMESTAMP    NOT NULL
);

CREATE TABLE form_analysis.form_flags (
    id               BIGINT       PRIMARY KEY DEFAULT nextval('form_analysis."form_flags_SEQ"'),
    form_analysis_id BIGINT       NOT NULL,
    severity         VARCHAR(50)  NOT NULL,
    code             VARCHAR(255) NOT NULL,
    description      VARCHAR(255) NOT NULL
);
