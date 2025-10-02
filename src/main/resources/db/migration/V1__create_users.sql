CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       uid UUID NOT NULL UNIQUE,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       email VARCHAR(200) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT now(),
                       updated_at TIMESTAMP DEFAULT now()
);
