package com.digital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.Category;
import com.digital.model.entity.Product;
import com.digital.model.request.category.CategoryAddReq;
import com.digital.model.request.category.CategoryUpdateReq;
import com.digital.model.request.page.PageReq;
import com.digital.model.request.product.ProductAddReq;
import com.digital.model.request.product.ProductUpdateReq;
import com.digital.model.vo.category.GetListCategoryVo;
import com.digital.model.vo.product.GetProductVo;
import com.digital.result.Result;
import com.digital.service.CategoryService;
import com.digital.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/3/26 15:11
 */
@Slf4j
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * 查看商品通过id
     * @param id
     * @return
     */
    @GetMapping("/product/{id}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<GetProductVo> getProductById(@PathVariable Integer id) {
        if (id == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Product product = productService.getById(id);
        if (product == null) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        GetProductVo getProductVo = new GetProductVo();
        BeanUtils.copyProperties(product, getProductVo);
        return Result.success(getProductVo);
    }

    /**
     * 得到商品列表在某个类别下
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @GetMapping("/product")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<Page<GetProductVo>> getProductListByCategoryId(Integer pageNum, Integer pageSize, Integer categoryId) {
        if (pageNum == null || pageSize == null || categoryId == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }


        Page<Product> categoryPage = productService.page(new Page<>(pageNum, pageSize), new QueryWrapper<Product>().eq("category_id", categoryId));
        List<GetProductVo> getProductVoList = categoryPage.getRecords().stream()
                .map(product -> {
                    GetProductVo getProductVo = new GetProductVo();
                    BeanUtils.copyProperties(product, getProductVo);
                    return getProductVo;
                })
                .toList();
        Page<GetProductVo> getProductVoPage = new Page<>();
        getProductVoPage.setRecords(getProductVoList);
        return Result.success(getProductVoPage);
    }

    /**
     * 添加商品，限管理员
     * @param productAddReq
     * @return
     */
    @PostMapping("/product")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result addProduct(@RequestBody ProductAddReq productAddReq) {
        if (productAddReq.getCategoryId() == null || productAddReq.getName() == null
        || productAddReq.getSubtitle() == null || productAddReq.getMainImage() == null
        || productAddReq.getSubImages() == null || productAddReq.getDetail() == null
        || productAddReq.getPrice() == null || productAddReq.getStock() == null || productAddReq.getStatus() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        Product product = new Product();
        BeanUtils.copyProperties(productAddReq, product);

        boolean save = productService.save(product);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success();
    }

    /**
     * 删除商品，限管理员
     * @param id
     * @return
     */
    @DeleteMapping("/product/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result deleteProductById(@PathVariable Integer id) {

        if (id == null || id <= 0) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        boolean removeById = productService.removeById(id);
        if (!removeById) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success();
    }

    /**
     * 更新商品信息，限管理员
     * @param id
     * @param productUpdateReq
     * @return
     */
    @PutMapping("/product/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result updateProductById(@PathVariable Integer id,
                                     @RequestBody ProductUpdateReq productUpdateReq) {

        if (id == null || id <= 0) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.eq("id", id);

        Product product = new Product();
        BeanUtils.copyProperties(productUpdateReq, product);

        boolean update = productService.update(product, productQueryWrapper);
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success();
    }
}
