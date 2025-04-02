from pydantic import BaseModel


class WorkflowParameters(BaseModel):
    question: str
