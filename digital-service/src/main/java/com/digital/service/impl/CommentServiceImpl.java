package com.digital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.mapper.CommentMapper;
import com.digital.model.entity.Comment;
import com.digital.service.CommentService;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2025-04-14 19:16:57
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService {

}




