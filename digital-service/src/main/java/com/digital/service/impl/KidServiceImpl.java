package com.digital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.enums.ResultErrorEnum;
import com.digital.mapper.KidMapper;
import com.digital.model.entity.Kid;
import com.digital.result.Result;
import com.digital.service.KidService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;



@Service
public class KidServiceImpl extends ServiceImpl<KidMapper, Kid>
        implements KidService {
    //添加儿童的具体逻辑
    @Override
    public Result add(Long user_id, String avatar, String nickname, Integer old) {
        if (avatar == null || avatar.isEmpty()) {
            return Result.error("头像不能为空");
        }
        if (nickname == null || nickname.isEmpty()) {
            return Result.error("昵称不能为空");
        }
        if (old == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        // 创建 Kid 对象
        Kid kid = new Kid();
        kid.setUserId(user_id);
        kid.setAvatar(avatar);
        kid.setNickname(nickname);
        kid.setOld(old);
/*
        后期处理
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
    //修改儿童的具体逻辑
    @Override
    public Result update(Long id, String avatar, String nickname, Integer old) {
        // 参数基础校验

        if (avatar == null || avatar.isEmpty()) {
            return Result.error("头像不能为空");
        }
        if (nickname == null || nickname.isEmpty()) {
            return Result.error("昵称不能为空");
        }
        if (old == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        // 构建更新对象
        Kid updatedKid = new Kid();
        updatedKid.setId(id);
        updatedKid.setAvatar(avatar);
        updatedKid.setNickname(nickname);
        updatedKid.setOld(old);

        // 执行更新
        boolean isUpdated = updateById(updatedKid);
        if (!isUpdated) {
            return Result.error("更新失败");
        }

        return Result.success("更新成功");
    }

}

