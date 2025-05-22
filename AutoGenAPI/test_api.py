import requests

# 连接成功
response = requests.post(
    "http://localhost:8000/run_workflow/",
    json={"question": "一位35岁女性患者出现了关节疼痛和糖耐量异常，请问她可能患了什么疾病？请检索本地知识库"}
    # json={"question": "这个月四川成都的天气"}
)
print(response.json())