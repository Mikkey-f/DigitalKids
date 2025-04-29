# from flask import Flask, request, jsonify
# import os
# import sys
# import logging
# from werkzeug.utils import secure_filename  # 新增安全文件名处理
# from effcient_Net.tongue_crack.crack_predict import main as crack_main
# from effcient_Net.tongue_coated.coated_predict import main as coated_main
# from effcient_Net.tongue_color.color_predict import main as color_main
# from effcient_Net.tongue_indentation.indent_predict import main as indent_main
#
# # 配置日志
# logging.basicConfig(level=logging.DEBUG)
# logger = logging.getLogger(__name__)
#
# app = Flask(__name__)
#
# # 设置相对路径
# BASE_DIR = os.path.dirname(os.path.abspath(__file__))  # 获取当前文件所在目录
# app.config['UPLOAD_FOLDER'] = os.path.join(BASE_DIR, '..', 'data', 'test')  # 向上回一级到Tongue_diagnosis目录
#
# # 确保上传目录存在
# try:
#     os.makedirs(app.config['UPLOAD_FOLDER'], exist_ok=True)
#     logger.info(f"Upload directory initialized at: {os.path.abspath(app.config['UPLOAD_FOLDER'])}")
# except Exception as e:
#     logger.error(f"Failed to create upload directory: {str(e)}")
#     sys.exit(1)
#
#
# @app.route('/predict', methods=['POST'])
# def predict():
#     if 'file' not in request.files:
#         return jsonify({"error": "No file part"}), 400
#     file = request.files['file']
#     if file.filename == '':
#         return jsonify({"error": "No selected file"}), 400
#
#     temp_path = None
#     try:
#         # 生成安全文件名
#         filename = secure_filename(file.filename)
#         temp_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
#
#         # 保存文件（自动覆盖同名文件）
#         file.save(temp_path)
#         logger.debug(f"File saved to: {os.path.abspath(temp_path)}")
#
#         # 执行预测（保持路径统一性）
#         predict_path = os.path.abspath(temp_path).replace('\\', '/')
#
#         # 各预测模块调用
#         crack_class, crack_prob = crack_main(predict_path)
#         coated_class, coated_prob = coated_main(predict_path)
#         color_class, color_prob = color_main(predict_path)
#         indent_class, indent_prob = indent_main(predict_path)
#
#         # 处理预测结果
#         if crack_class == 'crack':
#             crack_res = "裂纹舌"
#         else:
#             crack_res = "无裂纹"
#
#         if indent_class == 'normal':
#             indent_res = "无齿痕"
#         else:
#             indent_res = "齿痕舌"
#
#         if coated_class == "white":
#             coated_res = "白苔"
#         elif coated_class == "yellow":
#             coated_res = "黄苔"
#         else:
#             coated_res = "无苔"
#
#         if color_class == "red":
#             color_res = "淡红舌"
#         elif color_class == "white":
#             color_res = "淡白舌"
#         else:
#             color_res = "深红舌"
#
#         # 生成中医诊断建议
#         if color_class == 'white' and crack_class == 'crack' and indent_class == 'indentation' and coated_class == "white":
#             sugtext = "舌淡白，白舌苔，有齿痕，有裂纹:\n燥热伤津，阴液亏损，脾虚湿侵，脾失健运，湿邪内侵，精微不能濡养舌体。"
#         # 其他情况可以按照 home.py 中的逻辑继续添加
#
#         # 返回预测结果和诊断建议
#         result = {
#             "crack_result": crack_res,
#             "indent_result": indent_res,
#             "coated_result": coated_res,
#             "color_result": color_res,
#             "suggestion": sugtext
#         }
#
#     except Exception as e:
#         logger.error(f"Processing error: {str(e)}", exc_info=True)
#         return jsonify({"error": f"Internal processing error: {str(e)}"}), 500
#
#     finally:
#         # 如果要保留文件可以注释掉这个部分
#         if temp_path and os.path.exists(temp_path):
#             try:
#                 os.remove(temp_path)
#                 logger.debug(f"Removed temporary file: {temp_path}")
#             except Exception as e:
#                 logger.warning(f"Failed to remove temp file: {str(e)}")
#
#     return jsonify(result)
#
#
# if __name__ == '__main__':
#     app.run(host='0.0.0.0', port=8080)
#
#
#
from flask import Flask, request, jsonify
import os
import sys
import logging
from werkzeug.utils import secure_filename
from datetime import datetime  # 新增时间戳
from effcient_Net.tongue_crack.crack_predict import main as crack_main
from effcient_Net.tongue_coated.coated_predict import main as coated_main
from effcient_Net.tongue_color.color_predict import main as color_main
from effcient_Net.tongue_indentation.indent_predict import main as indent_main

# 配置结构化日志
logging.basicConfig(
    level=logging.DEBUG,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger('TongueDiagnosisAPI')

app = Flask(__name__)

# 动态路径配置
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
UPLOAD_ROOT = os.path.join(BASE_DIR, '..', 'data', 'test')  # 项目根目录的data/test

# 确保目录存在
os.makedirs(UPLOAD_ROOT, exist_ok=True)
logger.info(f"Upload root initialized at: {os.path.abspath(UPLOAD_ROOT)}")


def generate_safe_path(original_filename):
    """生成带时间戳的安全存储路径"""
    timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
    base_name = secure_filename(os.path.splitext(original_filename)[0])
    extension = secure_filename(os.path.splitext(original_filename)[1])
    return os.path.join(
        UPLOAD_ROOT,
        f"{base_name}_{timestamp}{extension}"
    )


@app.route('/predict', methods=['POST'])
def predict():
    if 'file' not in request.files:
        return jsonify({"error": "No file part"}), 400

    file = request.files['file']
    if not file or file.filename == '':
        return jsonify({"error": "No selected file"}), 400

    save_path = None
    try:
        # 生成带时间戳的唯一文件名
        save_path = generate_safe_path(file.filename)
        logger.debug(f"Attempting to save to: {save_path}")

        # 确保目录存在（二次验证）
        os.makedirs(os.path.dirname(save_path), exist_ok=True)

        # 保存文件
        file.save(save_path)
        logger.info(f"File successfully saved to: {os.path.abspath(save_path)}")

        # 转换为绝对路径并标准化
        predict_path = os.path.abspath(save_path)
        logger.debug(f"Passing to models: {predict_path}")

        # 调用预测模块（添加异常捕获）
        crack_result = crack_main(predict_path)
        coated_result = coated_main(predict_path)
        color_result = color_main(predict_path)
        indent_result = indent_main(predict_path)

        # 结果处理函数（建议抽离为独立模块）
        def process_crack(result):
            return "裂纹舌" if result[0] == 'crack' else "无裂纹"

        def process_indent(result):
            return "齿痕舌" if result[0] != 'normal' else "无齿痕"

        def process_coated(result):
            mapping = {"white": "白苔", "yellow": "黄苔"}
            return mapping.get(result[0], "无苔")

        def process_color(result):
            mapping = {"red": "淡红舌", "white": "淡白舌"}
            return mapping.get(result[0], "深红舌")

        # 构建响应
        return jsonify({
            "crack_result": process_crack(crack_result),
            "indent_result": process_indent(indent_result),
            "coated_result": process_coated(coated_result),
            "color_result": process_color(color_result),
            "diagnosis_image": os.path.basename(save_path)  # 返回存储的文件名
        })

    except Exception as e:
        logger.error(f"Pipeline failure: {str(e)}", exc_info=True)
        # 清理不完整文件
        if save_path and os.path.exists(save_path):
            os.remove(save_path)
            logger.warning(f"Cleaned incomplete file: {save_path}")
        return jsonify({"error": "Diagnosis failed", "detail": str(e)}), 500


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=6666, debug=False)  # 生产环境应关闭debug