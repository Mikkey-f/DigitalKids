import os
import json

import torch
from PIL import Image
from torchvision import transforms
import matplotlib.pyplot as plt

from Tongue_diagnosis.effcient_Net.efficientNet.model import efficientnet_b0 as create_model


def main(path):
    device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")

    img_size = {"B0": 224,
                "B1": 240,
                "B2": 260,
                "B3": 300,
                "B4": 380,
                "B5": 456,
                "B6": 528,
                "B7": 600}
    num_model = "B0"

    data_transform = transforms.Compose(
        [transforms.Resize(img_size[num_model]),
         transforms.CenterCrop(img_size[num_model]),
         transforms.ToTensor()])

    # load image
    img_path = path
    assert os.path.exists(img_path), "file: '{}' dose not exist.".format(img_path)
    img = Image.open(img_path)
    plt.imshow(img)
    # [N, C, H, W]
    img = data_transform(img)
    # expand batch dimension
    img = torch.unsqueeze(img, dim=0)

    # read class_indict
    # json_path = '../effcient_Net/tongue_color/class_indices.json'
    # 获取当前文件所在目录
    current_dir = os.path.dirname(os.path.abspath(__file__))
    json_path = os.path.join(current_dir, 'class_indices.json')
    assert os.path.exists(json_path), "file: '{}' dose not exist.".format(json_path)

    json_file = open(json_path, "r")
    class_indict = json.load(json_file)

    # create model
    model = create_model(num_classes=3).to(device)
    # load model weights
    # model_weight_path = "../effcient_Net/tongue_color/weights/model-29.pth"
    model_weight_path = os.path.join(current_dir, 'weights/model-29.pth')
    assert os.path.exists(model_weight_path), "file: '{}' dose not exist.".format(model_weight_path)
    model.load_state_dict(torch.load(model_weight_path, map_location=device))
    model.eval()
    with torch.no_grad():
        # predict class
        output = torch.squeeze(model(img.to(device))).cpu()
        predict = torch.softmax(output, dim=0)
        predict_cla = torch.argmax(predict).numpy()

    for i in range(len(predict)):
        print("class: {:10}   prob: {:.3}".format(class_indict[str(i)],
                                                  predict[i].numpy()))

    return class_indict[str(predict_cla)], predict[predict_cla].numpy()

if __name__ == '__main__':
    main()
