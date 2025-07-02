CREATE TABLE oauth2_authorized_clients (
    id SERIAL,
    client_registration_id VARCHAR(255) NOT NULL,
    principal_name VARCHAR(255) NOT NULL,
    access_token_type VARCHAR(50),
    access_token_value TEXT,
    access_token_issued_at TIMESTAMP,
    access_token_expires_at TIMESTAMP,
    access_token_scopes TEXT,
    refresh_token_value TEXT,
    refresh_token_issued_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    UNIQUE (client_registration_id, principal_name)
);

CREATE INDEX IF NOT EXISTS idx_principal_name ON oauth2_authorized_clients(principal_name);
CREATE INDEX IF NOT EXISTS idx_expires_at ON oauth2_authorized_clients(access_token_expires_at);