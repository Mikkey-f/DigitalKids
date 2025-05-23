package com.digital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.Category;
import com.digital.model.request.category.CategoryAddReq;
import com.digital.model.request.category.CategoryUpdateReq;
import com.digital.model.vo.category.GetListCategoryVo;
import com.digital.result.Result;
import com.digital.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/26 15:11
 */
@RestController
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 选择所有商品类别
     * @return
     */
    @GetMapping("/category")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<List<GetListCategoryVo>> selectAllCategory() {
        List<Category> list = categoryService.list();
        List<GetListCategoryVo> getListCategoryVoArrayList = list.stream()
                .map(category -> {
                    GetListCategoryVo getListCategoryVo = new GetListCategoryVo();
                    BeanUtils.copyProperties(category, getListCategoryVo);
                    return getListCategoryVo;
                })
                .collect(Collectors.toList());
        return Result.success(getListCategoryVoArrayList);
    }

}
