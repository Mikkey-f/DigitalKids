package com.digital.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.mapper.MessageMapper;
import com.digital.model.entity.Message;
import com.digital.service.MessageService;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【message(站内通知消息表)】的数据库操作Service实现
* @createDate 2025-04-20 21:00:53
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService {

}




