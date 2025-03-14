package com.digital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digital.model.entity.Kid;
import com.digital.model.request.KidAddReq;
import com.digital.result.Result;
import com.digital.service.KidService;
import lombok.extern.slf4j.Slf4j;
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
    public Result<List<Kid>> getAllKids() {
        List<Kid> kids = kidService.list();
        return Result.success(kids);
    }

    //返回值为id的儿童的相关信息
    @GetMapping("/kids/{id}")
    public Result<Kid> getKidById(@PathVariable Long id) {
        Kid kid = kidService.getById(id);
        return kid != null ? Result.success(kid) : Result.error("未找到该记录");
    }

    //根据id删除儿童信息
    @DeleteMapping("/kids/{id}")
    public Result<?> deleteKid(@PathVariable Long id) {
        boolean isRemoved = kidService.removeById(id);
        return isRemoved ? Result.success(null) : Result.error("删除失败");
    }

    //添加儿童的信息
    @PostMapping("/kids")
    public Result<?> addKid(@RequestBody KidAddReq kidAddReq) {
        Result result = kidService.add(kidAddReq.getAvatar(),kidAddReq.getNickname(),kidAddReq.getBirthdate(),kidAddReq.getHeight(),kidAddReq.getWeight());
        return result;
    }

    //修改儿童信息
    @PutMapping("/kids")
    public Result<?> updateKid(@RequestBody Kid kid) {
        if (kid.getId() == null) {
            return Result.error("ID不能为空");
        }
        boolean isUpdated = kidService.updateById(kid);
        return isUpdated ? Result.success(null) : Result.error( "修改失败");
    }
}
