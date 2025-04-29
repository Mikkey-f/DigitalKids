package com.digital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.model.entity.Ent;
import com.digital.service.EntService;
import com.digital.mapper.EntMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【ent】的数据库操作Service实现
* @createDate 2025-04-28 20:39:36
*/
@Service
public class EntServiceImpl extends ServiceImpl<EntMapper, Ent>
    implements EntService{

}




