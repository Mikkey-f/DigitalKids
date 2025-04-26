package com.digital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.enums.ResultSuccessEnum;
import com.digital.exception.BusinessException;
import com.digital.model.entity.Favorite;
import com.digital.model.entity.User;
import com.digital.model.request.favorite.FavorAddReq;
import com.digital.model.request.page.PageReq;
import com.digital.model.request.user.*;
import com.digital.model.vo.user.GetUserVo;
import com.digital.model.vo.user.LoginUserVo;
import com.digital.result.Result;
import com.digital.service.FavoriteService;
import com.digital.service.UserService;
import com.digital.utils.OssPutUtil;
import com.digital.utils.SMUtils;
import com.digital.utils.ValidateCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025-03-12  10:06
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    FavoriteService favoriteService;


    /**
     * 用户注册
     * @param userRegisterReq 用户注册请求
     * @return 返回响应
     */
    @PostMapping("/register")
    public Result registerUser(@RequestBody UserRegisterReq userRegisterReq,
                               HttpSession session) {
        //测试阶段不加入验证码
        String code = userRegisterReq.getCode();
        Object codeInSession = session.getAttribute(userRegisterReq.getPhone());
        if (codeInSession == null || !codeInSession.equals(code)) {
            return Result.error(ResultErrorEnum.REGISTER_IS_FAILURE.getMessage());
        }
        Result result = userService.registerUser(userRegisterReq.getName(), userRegisterReq.getPassword(), userRegisterReq.getPhone(), userRegisterReq.getGender());
        result.setMsg(ResultSuccessEnum.REGISTER_SUCCESS.getMsg());
        return result;
    }

    /**
     * 登录
     * @param userLoginReq
     * @return
     */
    @PostMapping("/login")
    public Result<LoginUserVo> loginUser(@RequestBody UserLoginReq userLoginReq,
                                         HttpSession session) {
        if (userLoginReq == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_NULL.getMessage());
        }
        String phoneNum = userLoginReq.getPhone();
        String userPassword = userLoginReq.getPassword();
        String code = userLoginReq.getCode();
        if (StringUtils.isAnyBlank(phoneNum, userPassword, code)) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Object codeInSession = session.getAttribute(phoneNum);
        if (codeInSession == null || !codeInSession.equals(code)) {
            return Result.error(ResultErrorEnum.LOGIN_IS_FAILURE.getMessage());
        }

        return userService.loginUser(phoneNum, userPassword);
    }

    /**
     * 获得当前登录用户信息
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<LoginUserVo> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return Result.success(userService.getLoginUserVO(user));
    }




    /**
     * 根据 id 获取包装类
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<GetUserVo> getUserVOById(long id, HttpServletRequest request) {
        User user = userService.getById(id);
        return Result.success(userService.getUserVo(user));
    }

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    @PostMapping("/update/my")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<Boolean> updateMyUser(@RequestBody UserUpdateMyReq userUpdateMyRequest,
                                              HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ResultErrorEnum.PARAM_IS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        if (!result) {
            Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * admin 获取用户分页列表
     * @param pageReq
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Page<User>> listUserByPage(@RequestBody PageReq pageReq) {
        long current = pageReq.getCurrent();
        long size = pageReq.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size));
        return Result.success(userPage);
    }

    /**
     * 用户获取userVO
     * @param pageReq
     * @return
     */
    @PostMapping("/list/page/vo")
    public Result<Page<GetUserVo>> listUserVOByPage(@RequestBody PageReq pageReq) {
        if (pageReq == null) {
            throw new BusinessException(ResultErrorEnum.PARAM_IS_ERROR);
        }
        long current = pageReq.getCurrent();
        long size = pageReq.getPageSize();
        // 限制爬虫
        if (size >= 20) {
            throw new BusinessException(ResultErrorEnum.PARAM_IS_ERROR);
        }
        Page<User> userPage = userService.page(new Page<>(current, size));
        Page<GetUserVo> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<GetUserVo> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return Result.success(userVOPage);
    }

    /**
     * 为当前用户添加收藏
     * @param favorAddReq
     * @return
     */
    @PostMapping("/favorite/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result addFavorite(@RequestBody FavorAddReq favorAddReq) {
        if (favorAddReq == null) {
            throw new BusinessException(ResultErrorEnum.PARAM_IS_ERROR);
        }

        Favorite favorite = new Favorite();
        QueryWrapper<Favorite> eq = new QueryWrapper<Favorite>().eq("user_id", favorAddReq.getUserId()).eq("target_id", favorAddReq.getTargetId()).eq("target_type", favorAddReq.getTargetType());
        Favorite one = favoriteService.getOne(eq);
        if (one != null) {
            throw new BusinessException(ResultErrorEnum.NOT_ALLOW_ADD_SAME_THING);
        }
        BeanUtils.copyProperties(favorAddReq, favorite);
        boolean save = favoriteService.save(favorite);
        if (!save) {
            throw new BusinessException(ResultErrorEnum.OPERATION_ERROR);
        }

        return Result.success(ResultErrorEnum.SUCCESS.getMessage());
    }

    /**
     * 删除某个收藏
     * @param id
     * @return
     */
    @DeleteMapping("/favorite/delete/{id}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result deleteFavoriteById(@PathVariable long id) {
        boolean b = favoriteService.removeById(id);
        return Result.success(ResultErrorEnum.SUCCESS.getMessage());
    }

//    @PostMapping("/favorite/list/page")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
//    public Result<Page<FavoriteVo>> listFavoriteByPage(@RequestBody PageReq pageReq) {
//        long current = pageReq.getCurrent();
//        long size = pageReq.getPageSize();
//        Page<Favorite> favoritePage = favoriteService.page(new Page<>(current, size));
//        Page<FavoriteVo> favoriteVOPage = new Page<>(current, size, favoritePage.getTotal());
//        List<FavoriteVo> favoriteVO = favoriteService.getFavoriteVoList(favoritePage.getRecords());
//        favoriteVOPage.setRecords(favoriteVO);
//        return Result.success(favoriteVOPage);
//    }

    /**
     * 发送手机验证码
     * @param phoneNum
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(String phoneNum, HttpSession session) {

        if (StringUtils.isEmpty(phoneNum)) {
            throw new BusinessException(ResultErrorEnum.NOT_GET_CODE);
        }

//      2.随机生成四位验证码
        String code = ValidateCodeUtil.generateValidateCode(4).toString();

//      3.调用阿里云提供的短信服务
        SMUtils.sendMessage(phoneNum, code);

//      4.需要将生成的验证码保存到session中
        session.setAttribute(phoneNum, code);

        return Result.success(ResultErrorEnum.SUCCESS.getMessage());
    }

    /**
     * 上传头像
     * @param file
     * @return
     */
    @PostMapping("/avatar")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<String> fileUpload(@RequestParam("file") MultipartFile file) throws FileNotFoundException {
        if (file.isEmpty()) {
            return Result.error(ResultErrorEnum.FILE_UPLOAD_IS_EMPTY.getMessage());
        }
        String tempFilePath = this.getClass().getResource("/").getPath();
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

