package com.digital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.model.entity.Product;
import com.digital.service.ProductService;
import com.digital.mapper.ProductMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【product】的数据库操作Service实现
* @createDate 2025-03-26 14:57:14
*/
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{

}




