CREATE TABLE roles
(
    id BINARY (16) NOT NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    created_by VARCHAR(255) NULL,
    updated_by VARCHAR(255) NULL,
    name       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE tokens
(
    id BINARY (16) NOT NULL,
    created_at  datetime     NULL,
    updated_at  datetime     NULL,
    created_by  VARCHAR(255) NULL,
    updated_by  VARCHAR(255) NULL,
    token_value VARCHAR(255) NOT NULL,
    status      VARCHAR(255) NULL,
    expiry_date datetime     NOT NULL,
    user_id BINARY (16) NULL,
    CONSTRAINT pk_tokens PRIMARY KEY (id)
);

CREATE TABLE users
(
    id BINARY (16) NOT NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    created_by VARCHAR(255) NULL,
    updated_by VARCHAR(255) NULL,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    status     VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_roles
(
    user_id BINARY (16) NOT NULL,
    roles_id BINARY (16) NOT NULL
);

ALTER TABLE roles
    ADD CONSTRAINT uc_roles_name UNIQUE (name);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_password UNIQUE (password);

ALTER TABLE tokens
    ADD CONSTRAINT FK_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_user_role FOREIGN KEY (roles_id) REFERENCES roles (id);