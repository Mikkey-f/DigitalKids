package com.digital.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.Category;
import com.digital.model.request.category.CategoryAddReq;
import com.digital.model.request.category.CategoryUpdateReq;
import com.digital.result.Result;
import com.digital.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/27 20:06
 */
@RestController
@Slf4j
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 添加商品类别
     * @param categoryAddReq
     * @return
     */
    @PostMapping("/category")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result addCategory(@RequestBody CategoryAddReq categoryAddReq) {
        if (categoryAddReq.getParentId() == null || categoryAddReq.getStatus() == null
                || categoryAddReq.getName() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        if (!categoryAddReq.getStatus().equals(0) || categoryAddReq.getParentId() < 0) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        Category category = new Category();
        BeanUtils.copyProperties(categoryAddReq, category);

        boolean save = categoryService.save(category);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success(true);
    }

    /**
     * 删除商品类别
     * @param id
     * @return
     */
    @DeleteMapping("/category/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result deleteCategoryById(@PathVariable Integer id) {

        if (id == null || id <= 0) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        boolean removeById = categoryService.removeById(id);
        if (!removeById) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success(true);
    }

    /**
     * 更新商品类别信息
     * @param id
     * @param categoryUpdateReq
     * @return
     */
    @PutMapping("/category/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result updateCategoryById(@PathVariable Integer id,
                                     @RequestBody CategoryUpdateReq categoryUpdateReq) {

        if (id == null || id <= 0) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.eq("id", id);

        Category category = new Category();
        BeanUtils.copyProperties(categoryUpdateReq, category);

        boolean update = categoryService.update(category, categoryQueryWrapper);
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success(true);
    }
}
