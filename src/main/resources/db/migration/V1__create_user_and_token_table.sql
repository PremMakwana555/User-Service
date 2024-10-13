-- Create the 'token' table
CREATE TABLE token
(
    id          UUID                        NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255),
    token_value VARCHAR(255)                      NOT NULL, -- Changed to UUID to match expected usage
    status      VARCHAR(255),
    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id     UUID,
    CONSTRAINT pk_token PRIMARY KEY (id)
);

-- Create the 'users' table
CREATE TABLE users
(
    id         UUID         NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    status     VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

-- Create the 'user_roles' table
CREATE TABLE user_roles
(
    user_id UUID NOT NULL,
    roles   VARCHAR(255),
    CONSTRAINT fk_user_roles_on_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Add unique constraint on email column (email should be unique)
ALTER TABLE users
    ADD CONSTRAINT uc_user_email UNIQUE (email);

-- Ensure foreign key relationship between 'token' and 'users'
ALTER TABLE token
    ADD CONSTRAINT FK_TOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

