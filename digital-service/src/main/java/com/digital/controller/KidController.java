package com.digital.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.exception.BusinessException;
import com.digital.model.entity.Kid;
import com.digital.model.entity.User;
import com.digital.model.request.kid.KidAddReq;
import com.digital.model.request.kid.KidUpdateReq;
import com.digital.model.vo.kid.CreateKidVo;
import com.digital.result.Result;
import com.digital.service.KidService;
import com.digital.service.UserService;
import com.digital.utils.OssPutUtil;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
public class KidController {

    @Autowired
    private KidService kidService;

    @Autowired
    private UserService userService;

    /**
     * 返回值为id的儿童的相关信息
     * @param id 儿童id
     * @return
     */
    @GetMapping("/kids/{id}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<CreateKidVo> getKidById(@PathVariable Long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Kid kid = kidService.getOne(new QueryWrapper<Kid>().eq("id", id).eq("user_id", loginUser.getId()));
        if (kid == null) {
            return Result.error(ResultErrorEnum.W_PARAM_IS_NULL.getMessage());
        }
        CreateKidVo createKidVo = new CreateKidVo();
        BeanUtils.copyProperties(kid, createKidVo);
        return Result.success(createKidVo);
    }

    /**
     * 根据id删除儿童信息
     * @param id
     * @return
     */
    @DeleteMapping("/kids/{id}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<Boolean> deleteKid(@PathVariable Long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        boolean isRemoved = kidService.remove(new QueryWrapper<Kid>().eq("id", id).eq("user_id", loginUser.getId()));
        return isRemoved ? Result.success(true) : Result.error("删除失败");
    }

    /**
     * 添加儿童的信息
     * @param kidAddReq
     * @return
     */
    @PostMapping("/kids")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<Kid> addKidForUser(@RequestBody KidAddReq kidAddReq, HttpServletRequest request) {

        if (kidAddReq.getOld() == null || kidAddReq.getOld() < 0) {
            return Result.error(ResultErrorEnum.W_PARAM_IS_NULL.getMessage());
        }
        if (kidAddReq.getAvatar() == null || kidAddReq.getNickname() == null) {
            return Result.error(ResultErrorEnum.W_PARAM_IS_NULL.getMessage());
        }
        Kid kid = new Kid();
        BeanUtils.copyProperties(kidAddReq, kid);
        kid.setUserId(userService.getLoginUser(request).getId());
        boolean save = kidService.save(kid);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(kid);
    }

    /**
     * 修改儿童信息
     * @param kidUpdateReq
     * @return
     */
    @PutMapping("/kids")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<Boolean> updateKid(@RequestBody KidUpdateReq kidUpdateReq) {
        Kid kid = new Kid();
        BeanUtils.copyProperties(kidUpdateReq, kid);
        kid.setId(Long.valueOf(kidUpdateReq.getId()));
        boolean b = kidService.updateById(kid);
        if (!b) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 修改孩子头像
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    @PostMapping("/kids/avatar")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<String> fileUpload(@RequestParam("file") MultipartFile file) throws FileNotFoundException {
        if (file.isEmpty()) {
            return Result.error(ResultErrorEnum.FILE_UPLOAD_IS_EMPTY.getMessage());
        }
        String tempFilePath = Objects.requireNonNull(this.getClass().getResource("/")).getPath();
        String fileName = file.getOriginalFilename();
        File tempFile = new File(tempFilePath + fileName);
        try {
            file.transferTo(tempFile);
            //return "上传成功" + tempFilePath + fileName;
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new BusinessException(ResultErrorEnum.FILE_UPLOAD_ERROR);
        }
        return Result.success(OssPutUtil.fileUpload(fileName, tempFilePath + fileName));
    }
}
