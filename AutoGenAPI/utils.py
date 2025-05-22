from dotenv import load_dotenv
import os
import json

load_dotenv()
replacement_array = [(f"<{key}>", value) for key, value in os.environ.items()]


def replace_api_key(s):
    for old, new in replacement_array:
        s = s.replace(old, new)
    return s


def load_agent_specs(agent_spec_path: str):
    with open(agent_spec_path, 'r', encoding="utf-8", errors='replace') as file:
        json_content = file.read()

    # json_content = replace_api_key(json_content)

    return json.loads(json_content)
