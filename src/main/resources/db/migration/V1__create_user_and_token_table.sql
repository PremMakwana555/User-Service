CREATE TABLE "user"
(
    id         UUID         NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    roles      VARCHAR(255),
    status     VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE user_tokens
(
    user_id   UUID NOT NULL,
    tokens_id UUID NOT NULL
);

ALTER TABLE "user"
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE "user"
    ADD CONSTRAINT uc_user_password UNIQUE (password);

ALTER TABLE user_tokens
    ADD CONSTRAINT uc_user_tokens_tokens UNIQUE (tokens_id);

-- ALTER TABLE user_tokens
--     ADD CONSTRAINT fk_user_token_on_token FOREIGN KEY (tokens_id) REFERENCES token (id);
--
-- ALTER TABLE user_tokens
--     ADD CONSTRAINT fk_user_token_on_user FOREIGN KEY (user_id) REFERENCES "user" (id);