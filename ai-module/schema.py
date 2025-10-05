from pydantic import BaseModel

class PredictionMessage(BaseModel):
    portfolio_id: str
    transaction_id: str
    trace_id: str
