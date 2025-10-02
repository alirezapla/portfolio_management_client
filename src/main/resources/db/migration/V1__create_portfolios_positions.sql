CREATE TABLE portfolios (
                            id BIGSERIAL PRIMARY KEY,
                            uid UUID NOT NULL UNIQUE,
                            user_id BIGINT NOT NULL,
                            name VARCHAR(200) NOT NULL,
                            balance NUMERIC(18,2) DEFAULT 0,
                            currency VARCHAR(10) DEFAULT 'USD',
                            created_at TIMESTAMP DEFAULT now(),
                            updated_at TIMESTAMP DEFAULT now(),
                            CONSTRAINT fk_portfolio_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE positions (
                           id BIGSERIAL PRIMARY KEY,
                           uid UUID NOT NULL UNIQUE,
                           portfolio_id BIGINT NOT NULL,
                           ticker VARCHAR(50) NOT NULL,
                           quantity NUMERIC(18,4) NOT NULL,
                           avg_price NUMERIC(18,4) NOT NULL,
                           weight NUMERIC(5,2),
                           created_at TIMESTAMP DEFAULT now(),
                           updated_at TIMESTAMP DEFAULT now(),
                           CONSTRAINT fk_position_portfolio FOREIGN KEY (portfolio_id) REFERENCES portfolios(id)
);