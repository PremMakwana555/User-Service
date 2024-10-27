CREATE TABLE "authorization"
(
    id                            VARCHAR(255) NOT NULL,
    access_token_expires_at       TIMESTAMP WITHOUT TIME ZONE,
    access_token_issued_at        TIMESTAMP WITHOUT TIME ZONE,
    access_token_metadata         VARCHAR(2000),
    access_token_scopes           VARCHAR(1000),
    access_token_type             VARCHAR(255),
    access_token_value            VARCHAR(4000),
    attributes                    VARCHAR(4000),
    authorization_code_expires_at TIMESTAMP WITHOUT TIME ZONE,
    authorization_code_issued_at  TIMESTAMP WITHOUT TIME ZONE,
    authorization_code_metadata   VARCHAR(255),
    authorization_code_value      VARCHAR(4000),
    authorization_grant_type      VARCHAR(255),
    authorized_scopes             VARCHAR(1000),
    device_code_expires_at        TIMESTAMP WITHOUT TIME ZONE,
    device_code_issued_at         TIMESTAMP WITHOUT TIME ZONE,
    device_code_metadata          VARCHAR(2000),
    device_code_value             VARCHAR(4000),
    oidc_id_token_claims          VARCHAR(2000),
    oidc_id_token_expires_at      TIMESTAMP WITHOUT TIME ZONE,
    oidc_id_token_issued_at       TIMESTAMP WITHOUT TIME ZONE,
    oidc_id_token_metadata        VARCHAR(2000),
    oidc_id_token_value           VARCHAR(4000),
    principal_name                VARCHAR(255),
    refresh_token_expires_at      TIMESTAMP WITHOUT TIME ZONE,
    refresh_token_issued_at       TIMESTAMP WITHOUT TIME ZONE,
    refresh_token_metadata        VARCHAR(2000),
    refresh_token_value           VARCHAR(4000),
    registered_client_id          VARCHAR(255),
    state                         VARCHAR(500),
    user_code_expires_at          TIMESTAMP WITHOUT TIME ZONE,
    user_code_issued_at           TIMESTAMP WITHOUT TIME ZONE,
    user_code_metadata            VARCHAR(2000),
    user_code_value               VARCHAR(4000),
    CONSTRAINT authorization_pkey PRIMARY KEY (id)
);

CREATE TABLE authorization_consent
(
    principal_name       VARCHAR(255) NOT NULL,
    registered_client_id VARCHAR(255) NOT NULL,
    authorities          VARCHAR(1000),
    CONSTRAINT authorization_consent_pkey PRIMARY KEY (principal_name, registered_client_id)
);

CREATE TABLE client
(
    id                            VARCHAR(255) NOT NULL,
    authorization_grant_types     VARCHAR(1000),
    client_authentication_methods VARCHAR(1000),
    client_id                     VARCHAR(255),
    client_id_issued_at           TIMESTAMP WITHOUT TIME ZONE,
    client_name                   VARCHAR(255),
    client_secret                 VARCHAR(255),
    client_secret_expires_at      TIMESTAMP WITHOUT TIME ZONE,
    client_settings               VARCHAR(2000),
    post_logout_redirect_uris     VARCHAR(1000),
    redirect_uris                 VARCHAR(1000),
    scopes                        VARCHAR(1000),
    token_settings                VARCHAR(2000),
    CONSTRAINT client_pkey PRIMARY KEY (id)
);

CREATE TABLE flyway_schema_history
(
    installed_rank INTEGER                                   NOT NULL,
    version        VARCHAR(50),
    description    VARCHAR(200)                              NOT NULL,
    type           VARCHAR(20)                               NOT NULL,
    script         VARCHAR(1000)                             NOT NULL,
    checksum       INTEGER,
    installed_by   VARCHAR(100)                              NOT NULL,
    installed_on   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    execution_time INTEGER                                   NOT NULL,
    success        BOOLEAN                                   NOT NULL,
    CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank)
);

CREATE TABLE token
(
    id          CHAR(36)                    NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    created_by  VARCHAR(255),
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_by  VARCHAR(255),
    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status      VARCHAR(255),
    token_value VARCHAR(255)                NOT NULL,
    user_id     CHAR(36),
    CONSTRAINT token_pkey PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    user_id CHAR(36) NOT NULL,
    roles   VARCHAR(255)
);

CREATE TABLE users
(
    id         CHAR(36)     NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(255),
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    updated_by VARCHAR(255),
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    status     VARCHAR(255) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE users_tokens
(
    user_id   CHAR(36) NOT NULL,
    tokens_id CHAR(36) NOT NULL
);

ALTER TABLE users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);

ALTER TABLE users_tokens
    ADD CONSTRAINT ukf4rxms009ktbqt31o6p4y8vhw UNIQUE (tokens_id);

ALTER TABLE users
    ADD CONSTRAINT ukr53o2ojjw4fikudfnsuuga336 UNIQUE (password);

CREATE INDEX flyway_schema_history_s_idx ON flyway_schema_history (success);

ALTER TABLE users_tokens
    ADD CONSTRAINT fk1xl95grn90nu35htsi9o5kbxw FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION;

ALTER TABLE users_tokens
    ADD CONSTRAINT fk2b23nqo552bhfydxn9plx4vod FOREIGN KEY (tokens_id) REFERENCES token (id) ON DELETE NO ACTION;

ALTER TABLE user_roles
    ADD CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION;

ALTER TABLE token
    ADD CONSTRAINT fkj8rfw4x0wjjyibfqq566j4qng FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION;