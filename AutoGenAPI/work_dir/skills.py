

##### Begin of web_search_script #####

from zhipuai import ZhipuAI

def web_search(query):
    OPENAI_API_KEY = "8bbcc26ac5e24479a941b958da786911.Ql6UlT4G10SNOAPP"
    search_prompt = """

    # 以下是来自互联网的信息：
    {search_result}

    # 当前日期: 2025-02-21

    # 要求：
    根据最新发布的信息回答用户问题，当回答引用了参考信息时，必须在句末使用对应的[ref_序号]来标明参考信息来源
    """

    client = ZhipuAI(api_key=OPENAI_API_KEY)
    tools = [{
        "type": "web_search",
        "web_search": {
            "enable": True,
            "search_result": True,
            "search_prompt": search_prompt
        }
    }]

    response = client.chat.completions.create(
        model="glm-4",
        messages=[
            {"role": "user", "content": f"问：{query}，答："}
        ],
        top_p=0.7,
        temperature=0.1,
        tools=tools
    )
    return response

if __name__ == '__main__':
    # 问题query示例：
    query = "哪吒2电影票房是多少"
    response = web_search(query)

    # response.choices[0].message.content获取最终的联网结果
    print(response.choices[0].message.content)
    # response.web_search是一个字典，每个元素是一个来源网页，包括来源网站的图标、标题、链接、来源名称以及引用的文本内容，即'content'、'icon'、'link'、'refer'、'title'
    # 获取对应引用的链接
    for web in response.web_search:
        print(web['refer'], web['link'])




#### End of web_search_script ####

        

##### Begin of get_current_time #####

# Import the datetime class from the datetime module
# datetime module provides classes for manipulating dates and times
from datetime import datetime

# Get the current local date and time using datetime.now() method
# Returns a datetime object containing both date and time information
current_time = datetime.now()

# Format the datetime object into a human-readable string
# strftime() method converts datetime to string using format codes:
# %Y - 4-digit year (e.g., 2023)
# %m - 2-digit month (01-12)
# %d - 2-digit day of month (01-31)
# %H - 2-digit hour in 24-hour format (00-23)
# %M - 2-digit minute (00-59)
# %S - 2-digit second (00-59)
formatted_time = current_time.strftime("%Y-%m-%d %H:%M:%S")

# Print the formatted time string with a label
# The output will display in console with Chinese text prefix followed by the time
print("当前系统时间：", formatted_time)

#### End of get_current_time ####

        

##### Begin of local_konwledge_search #####

import json
import os
import pickle
import argparse

try:
    import tiktoken
    from langchain_community.embeddings import HuggingFaceEmbeddings
    from langchain_community.vectorstores import FAISS
except ImportError:
    raise ImportError("Please install langchain-community first.")


class DocumentRetriever:
    def __init__(self, index_folder):
        self.index_folder = index_folder
        self.vectorstore = None
        self.chunk_id_to_index = None
        self.embeddings = HuggingFaceEmbeddings(model_name="all-MiniLM-L6-v2")
        self._init()
        self.enc = tiktoken.encoding_for_model("gpt-3.5-turbo")

    def _init(self):
        self.vectorstore = FAISS.load_local(
            folder_path=self.index_folder,
            embeddings=self.embeddings,
            allow_dangerous_deserialization=True
        )
        with open(os.path.join(self.index_folder, "chunk_id_to_index.pkl"), "rb") as f:
            self.chunk_id_to_index = pickle.load(f)

    def __call__(self, query: str, size: int = 5, target_length: int = 256):
        if self.vectorstore is None:
            raise Exception("Vectorstore not initialized")

        result = self.vectorstore.similarity_search(query=query, k=size)
        expanded_chunks = self.do_expand(result, target_length)

        return json.dumps(expanded_chunks, indent=4)

    def do_expand(self, result, target_length):
        expanded_chunks = []
        # do expansion
        for r in result:
            source = r.metadata["source"]
            chunk_id = r.metadata["chunk_id"]
            content = r.page_content

            expanded_result = content
            left_chunk_id, right_chunk_id = chunk_id - 1, chunk_id + 1
            left_valid, right_valid = True, True
            chunk_ids = [chunk_id]
            while True:
                current_length = len(self.enc.encode(expanded_result))
                if f"{source}_{left_chunk_id}" in self.chunk_id_to_index:
                    chunk_ids.append(left_chunk_id)
                    left_chunk_index = self.vectorstore.index_to_docstore_id[
                        self.chunk_id_to_index[f"{source}_{left_chunk_id}"]
                    ]
                    left_chunk = self.vectorstore.docstore.search(left_chunk_index)
                    encoded_left_chunk = self.enc.encode(left_chunk.page_content)
                    if len(encoded_left_chunk) + current_length < target_length:
                        expanded_result = left_chunk.page_content + expanded_result
                        left_chunk_id -= 1
                        current_length += len(encoded_left_chunk)
                    else:
                        expanded_result += self.enc.decode(
                            encoded_left_chunk[-(target_length - current_length) :],
                        )
                        current_length = target_length
                        break
                else:
                    left_valid = False

                if f"{source}_{right_chunk_id}" in self.chunk_id_to_index:
                    chunk_ids.append(right_chunk_id)
                    right_chunk_index = self.vectorstore.index_to_docstore_id[
                        self.chunk_id_to_index[f"{source}_{right_chunk_id}"]
                    ]
                    right_chunk = self.vectorstore.docstore.search(right_chunk_index)
                    encoded_right_chunk = self.enc.encode(right_chunk.page_content)
                    if len(encoded_right_chunk) + current_length < target_length:
                        expanded_result += right_chunk.page_content
                        right_chunk_id += 1
                        current_length += len(encoded_right_chunk)
                    else:
                        expanded_result += self.enc.decode(
                            encoded_right_chunk[: target_length - current_length],
                        )
                        current_length = target_length
                        break
                else:
                    right_valid = False

                if not left_valid and not right_valid:
                    break

            expanded_chunks.append(
                {
                    "chunk": expanded_result,
                    "metadata": r.metadata,
                    # "length": current_length,
                    # "chunk_ids": chunk_ids
                },
            )
        return expanded_chunks

def search_knowledge(data):
    """
    根据输入的data搜索本地数据中的信息
    Parameters:
    data (str): 搜索关键词.
    """
    if not data:
        print("Error: No query provided.")
        exit(1)
    # Initialize with the path to your index folder
    index_folder = "D:/CodeProjectAll/LLMs/AutoGenAPI/knowledge"
    retriever = DocumentRetriever(index_folder)

    # Use the query from the command line arguments
    query = data
    size = 5  # Number of results to retrieve
    target_length = 256  # Target length of expanded content

    # Retrieve documents based on the query
    results = retriever(query, size, target_length)

    return results
    
# Example usage
if __name__ == "__main__":
    # 这里的'系统概述'只是一个示例，请不要按照这个词，要检索的词主要来源于用户的问题
    results = search_knowledge('系统概述')
    # 最后需要打印出results
    print(results)

#### End of local_konwledge_search ####

        