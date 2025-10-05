from pydantic import BaseModel


class PredictionMessage(BaseModel):
    portfolio_id: str
    message: str
    trace_id: str
