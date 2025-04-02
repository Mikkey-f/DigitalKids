from dotenv import load_dotenv
from autogenstudio import AutoGenWorkFlowManager, AgentWorkFlowConfig
from fastapi import FastAPI
from utils import load_agent_specs
from fastapi.middleware.cors import CORSMiddleware
from params import WorkflowParameters
import subprocess
from functools import partial
subprocess.Popen = partial(subprocess.Popen, encoding="utf-8")
load_dotenv()

app = FastAPI()

origins = ["*"]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.post("/run_workflow")
async def run_workflow(workflow_params: WorkflowParameters):
    agent_spec = load_agent_specs(agent_spec_path="./workflow_doctor_with_local_konwledge_and_web_search.json")

    # Create an AutoGen Workflow Configuration from the agent specification
    agent_work_flow_config = AgentWorkFlowConfig(**agent_spec)

    # Create a Workflow from the configuration
    agent_work_flow = AutoGenWorkFlowManager(agent_work_flow_config)

    # Run the workflow on a task
    # task_query = f"根据这个地址帮我生成文章: '{workflow_params.youtube_video_id}'"
    task_query = workflow_params.question
    agent_work_flow.run(message=task_query)

    response = agent_work_flow.agent_history[-1]["message"]["content"]
    if response == 'TERMINATE':
        response = agent_work_flow.agent_history[-2]["message"]["content"]
    return {"response": response}


if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="0.0.0.0", port=8000)
