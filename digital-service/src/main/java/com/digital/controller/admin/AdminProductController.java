package com.digital.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.Product;
import com.digital.model.request.product.ProductAddReq;
import com.digital.model.request.product.ProductUpdateReq;
import com.digital.result.Result;
import com.digital.service.ProductService;
import com.digital.service.impl.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/27 19:14
 */
@RestController
@Slf4j
@RequestMapping("/admin/product")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ElasticsearchService elasticsearchService;

    /**
     * 添加商品，限管理员
     * @param productAddReq
     * @return
     */
    @PostMapping
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Boolean> addProduct(@RequestBody ProductAddReq productAddReq) {
        if (productAddReq.getCategoryId() == null || productAddReq.getName() == null
                || productAddReq.getSubtitle() == null || productAddReq.getMainImage() == null
                || productAddReq.getSubImages() == null || productAddReq.getDetail() == null
                || productAddReq.getPrice() == null || productAddReq.getStock() == null || productAddReq.getStatus() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        Product product = new Product();
        BeanUtils.copyProperties(productAddReq, product);
        product.setCreateTime(new Date());
        product.setUpdateTime(new Date());
        boolean save = productService.save(product);
        elasticsearchService.saveProduct(product);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success(true);
    }

    /**
     * 删除商品，限管理员
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Boolean> deleteProductById(@PathVariable Integer id) {

        if (id == null || id <= 0) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        boolean removeById = productService.removeById(id);
        if (!removeById) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        elasticsearchService.deleteProduct(id);
        return Result.success(true);
    }

    /**
     * 更新商品信息，限管理员
     * @param id
     * @param productUpdateReq
     * @return
     */
    @PutMapping("/{id}")
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
        product.setId(id);
        product.setUpdateTime(new Date());
        product.setCreateTime(new Date());
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        elasticsearchService.saveProduct(product);
        return Result.success(true);
    }
}
