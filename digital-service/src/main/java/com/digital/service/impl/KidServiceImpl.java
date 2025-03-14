package com.digital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.enums.ResultErrorEnum;
import com.digital.mapper.KidMapper;
import com.digital.model.entity.Kid;
import com.digital.result.Result;
import com.digital.service.KidService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class KidServiceImpl extends ServiceImpl<KidMapper, Kid>
        implements KidService {

    @Override
    public Result add(String avatar, String nickname, String birthdate, BigDecimal height, BigDecimal weight) {
        if (avatar == null || avatar.isEmpty()) {
            return Result.error("头像不能为空");
        }
        if (nickname == null || nickname.isEmpty()) {
            return Result.error("昵称不能为空");
        }
        if (height == null || height.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.error("身高必须大于0");
        }
        if (weight == null || weight.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.error("体重必须大于0");
        }

        // 创建 Kid 对象
        Kid kid = new Kid();
        kid.setAvatar(avatar);
        kid.setNickname(nickname);
        kid.setHeight(height);
        kid.setWeight(weight);
/*
        // 转换日期格式（假设 birthdate 是字符串，需转为 Date）
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(birthdate);
            kid.setBirthdate(date);
        } catch (ParseException e) {
            return Result.error("日期格式错误，应为 yyyy-MM-dd");
        }
*/
        // 保存到数据库
        boolean isSaved = save(kid);
        if (!isSaved) {
            return Result.error("添加儿童信息失败");
        }

        return Result.success("添加成功");
    }
}

