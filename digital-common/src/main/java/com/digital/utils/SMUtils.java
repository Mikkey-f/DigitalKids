package com.digital.utils;

import com.aliyun.oss.ClientException;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/12 17:41
 * 短信发送工具
 */
@Slf4j
public class SMUtils {

    public static final String ACCESS_KEY_ID = "LTAI5tJR33sru2pTxJW4PC2G";
    public static final String ACCESS_KEY_SECRET = "BZZN79l193oiLfyljqMg1dLO470hMK";
    /**
     * 发送短信
     * @param signName 阿里云设置的签名
     * @param templateCode 阿里云设置的模板
     * @param phoneNumbers 发送目标的手机号
     * @param param 参数
     */
    public static void sendMessage(String signName, String templateCode,String phoneNumbers,String param){

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);

        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();

        request.setSysRegionId("cn-hangzhou");
//	    要发送给那个人的电话号码
        request.setPhoneNumbers(phoneNumbers);
//      我们在阿里云设置的签名
        request.setSignName(signName);
//	    我们在阿里云设置的模板
        request.setTemplateCode(templateCode);
//	    在设置模板的时候有一个占位符
        request.setTemplateParam("{\"code\":\""+param+"\"}");

//		request.setPhoneNumbers("1368846****");//接收短信的手机号码
//		request.setSignName("阿里云");//短信签名名称
//		request.setTemplateCode("SMS_20933****");//短信模板CODE
//		request.setTemplateParam("张三");//短信模板变量对应的实际值

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            log.info(response.getCode() + " " + response.getMessage());
            log.info("发送验证码成功");
        }catch (ClientException | com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }
    }
}
