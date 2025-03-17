package com.digital.controller;


import com.digital.annotation.AuthCheck;
import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.Kid;
import com.digital.model.request.kid.KidAddReq;
import com.digital.model.request.kid.KidUpdateReq;
import com.digital.model.vo.kid.CreateKidVo;
import com.digital.result.Result;
import com.digital.service.KidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class KidController {

    @Autowired
    private KidService kidService;

    //返回所有儿童的相关信息
    @GetMapping("/kids")
    @AuthCheck(mustRole = "user")
    public Result<List<Kid>> getAllKids() {
        List<Kid> kids = kidService.list();
        return Result.success(kids);
    }

    //返回值为id的儿童的相关信息
    @GetMapping("/kids/{id}")
    @AuthCheck(mustRole = "user")
    public Result<CreateKidVo> getKidById(@PathVariable Long id) {
        Kid kid = kidService.getById(id);
        if (kid == null) {
            Result.error(ResultErrorEnum.W_PARAM_IS_NULL.getMessage());
        }
        CreateKidVo createKidVo = new CreateKidVo();
        BeanUtils.copyProperties(kid, createKidVo);
        return Result.success(createKidVo);
    }

    //根据id删除儿童信息
    @DeleteMapping("/kids/{id}")
    @AuthCheck(mustRole = "user")
    public Result<?> deleteKid(@PathVariable Long id) {
        boolean isRemoved = kidService.removeById(id);
        return isRemoved ? Result.success() : Result.error("删除失败");
    }

    //添加儿童的信息
    @PostMapping("/kids")
    @AuthCheck(mustRole = "user")
    public Result<?> addKid(@RequestBody KidAddReq kidAddReq) {
        Result result = kidService.add(kidAddReq.getUser_id(), kidAddReq.getAvatar(),kidAddReq.getNickname(),kidAddReq.getBirthdate(),kidAddReq.getHeight(),kidAddReq.getWeight());
        return result;
    }

    //修改儿童信息
    @PutMapping("/kids")
    @AuthCheck(mustRole = "user")
    public Result<?> updateKid(@RequestBody KidUpdateReq kidUpdateReq) {
        Result result = kidService.update(kidUpdateReq.getId(), kidUpdateReq.getAvatar(), kidUpdateReq.getNickname(), kidUpdateReq.getBirthdate(), kidUpdateReq.getHeight(), kidUpdateReq.getWeight());
        return result;
    }
}
