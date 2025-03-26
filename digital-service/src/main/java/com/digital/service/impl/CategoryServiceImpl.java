package com.digital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.model.entity.Category;
import com.digital.service.CategoryService;
import com.digital.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【category】的数据库操作Service实现
* @createDate 2025-03-26 15:03:25
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




