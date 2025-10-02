CREATE TABLE predictions (
     id BIGSERIAL PRIMARY KEY,
     uid UUID NOT NULL UNIQUE,
     portfolio_id BIGINT NOT NULL,
     status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
         CHECK (status IN ('PENDING','COMPLETED','FAILED')),
     created_at TIMESTAMP NOT NULL DEFAULT NOW(),
     CONSTRAINT fk_predictions_portfolio FOREIGN KEY (portfolio_id)
         REFERENCES portfolios (id) ON DELETE CASCADE
);

CREATE TABLE prediction_actions (
    id BIGSERIAL PRIMARY KEY,
    uid UUID NOT NULL UNIQUE,
    prediction_id BIGINT NOT NULL,
    symbol VARCHAR(20) NOT NULL,
    action VARCHAR(10) NOT NULL CHECK (action IN ('BUY', 'SELL', 'HOLD')),
    quantity INT NOT NULL,
    weight NUMERIC(10,4),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_prediction_actions_prediction FOREIGN KEY (prediction_id)
        REFERENCES predictions (id) ON DELETE CASCADE
);
