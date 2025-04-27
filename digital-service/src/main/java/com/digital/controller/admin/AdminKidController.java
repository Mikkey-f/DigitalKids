package com.digital.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.mapper.KidMapper;
import com.digital.model.entity.Kid;
import com.digital.model.entity.User;
import com.digital.model.request.kid.KidAddReq;
import com.digital.model.request.kid.KidUpdateReq;
import com.digital.model.request.page.PageReq;
import com.digital.result.Result;
import com.digital.service.KidService;
import com.digital.service.UserService;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/26 19:16
 */
@RestController
@RequestMapping("/admin/kid")
public class AdminKidController {

    @Autowired
    KidService kidService;

    @Autowired
    KidMapper kidMapper;

    @Autowired
    UserService userService;
    /**
     * 返回所有儿童的相关信息
     * @return
     */
    @GetMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Page<Kid>> getAllKids(PageReq pageReq) {
        long current = pageReq.getCurrent();
        long size = pageReq.getPageSize();
        Page<Kid> page = new Page<>(current, size);
        Page<Kid> kidPage = kidMapper.selectPage(page, null);// null 表示无查询条件，可替换为 QueryWrapper 等条件构造器
        return Result.success(kidPage);
    }

    /**
     * 管理员添加儿童信息
     * @param kidAddReq
     * @param userId
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Boolean> addKid(KidAddReq kidAddReq, Integer userId) {
        if (kidAddReq.getOld() == null || kidAddReq.getOld() <= 0) {
            return Result.error(ResultErrorEnum.W_PARAM_IS_NULL.getMessage());
        }
        if (kidAddReq.getAvatar() == null || kidAddReq.getNickname() == null) {
            return Result.error(ResultErrorEnum.W_PARAM_IS_NULL.getMessage());
        }
        Kid kid = new Kid();
        BeanUtils.copyProperties(kidAddReq, kid);
        kid.setUserId(Long.valueOf(userId));

        boolean save = kidService.save(kid);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 管理员更新孩子信息
     * @param kidUpdateReq
     * @param userId
     * @return
     */
    @PutMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Boolean> updateKid(@RequestBody KidUpdateReq kidUpdateReq, Integer userId) {
        Kid kid = new Kid();
        BeanUtils.copyProperties(kidUpdateReq, kid);
        kid.setUserId(Long.valueOf(userId));
        kid.setId(Long.valueOf(kidUpdateReq.getId()));
        boolean b = kidService.updateById(kid);
        if (!b) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 管理员直接删除孩子
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Boolean> deleteKidById(@PathVariable Integer id) {
        boolean b = kidService.removeById(id);
        if (!b) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }
}
