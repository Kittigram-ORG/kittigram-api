CREATE SCHEMA IF NOT EXISTS cats;

CREATE SEQUENCE cats."cats_SEQ"       START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE cats."cat_images_SEQ" START WITH 1 INCREMENT BY 50;

CREATE TABLE cats.cats (
    id                BIGINT           PRIMARY KEY DEFAULT nextval('cats."cats_SEQ"'),
    name              VARCHAR(255)     NOT NULL,
    age               INTEGER,
    sex               VARCHAR(50)      NOT NULL,
    description       VARCHAR(1000),
    neutered          BOOLEAN          NOT NULL DEFAULT FALSE,
    status            VARCHAR(50)      NOT NULL,
    profile_image_url VARCHAR(255),
    organization_id   BIGINT           NOT NULL,
    city              VARCHAR(255)     NOT NULL,
    region            VARCHAR(255),
    country           VARCHAR(255)     NOT NULL,
    latitude          DOUBLE PRECISION,
    longitude         DOUBLE PRECISION,
    created_at        TIMESTAMP        NOT NULL,
    updated_at        TIMESTAMP        NOT NULL
);

CREATE TABLE cats.cat_images (
    id          BIGINT       PRIMARY KEY DEFAULT nextval('cats."cat_images_SEQ"'),
    cat_id      BIGINT       NOT NULL,
    key         VARCHAR(255) NOT NULL,
    url         VARCHAR(255) NOT NULL,
    image_order INTEGER      NOT NULL,
    created_at  TIMESTAMP    NOT NULL
);
