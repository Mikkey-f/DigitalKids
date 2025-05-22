package com.digital.utils;

import com.digital.enums.ResultErrorEnum;
import com.digital.exception.BusinessException;

import java.util.Random;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/12 17:52
 * 随机生成验证码工具类
 */
public class ValidateCodeUtil {
    /**
     * 随机生成验证码
     * @param length 长度为4位或者6位
     * @return
     */
    public static Integer generateValidateCode(int length){
        Integer code =null;

//      长度为4
        if(length == 4){
            code = new Random().nextInt(9999);//生成随机数，最大为9999
            if(code < 1000){
                code = code + 1000;//保证随机数为4位数字
            }

//      长度为6
        }else if(length == 6){
            code = new Random().nextInt(999999);//生成随机数，最大为999999
            if(code < 100000){
                code = code + 100000;//保证随机数为6位数字
            }
//       其他情况
        }else{
            throw new BusinessException(ResultErrorEnum.ONLY_GENERATE_4OR6_CODE);
        }
        return code;
    }

    /**
     * 随机生成指定长度字符串验证码
     * @param length 长度
     * @return
     */
    public static String generateValidateCode4String(int length){
        Random rdm = new Random();
        String hash1 = Integer.toHexString(rdm.nextInt());
        String capstr = hash1.substring(0, length);
        return capstr;
    }
}
