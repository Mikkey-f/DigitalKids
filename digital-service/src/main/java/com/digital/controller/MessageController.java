package com.digital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.model.entity.Message;
import com.digital.model.entity.User;
import com.digital.model.vo.message.MessageVo;
import com.digital.model.vo.user.GetUserVo;
import com.digital.result.Result;
import com.digital.service.MessageService;
import com.digital.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/21 00:25
 */
@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    /**
     * 根据topicId来获得通知
     * @param topicId 通知类型
     * @param request 请求
     * @return
     */
    @GetMapping("/list/{topicId}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result<List<MessageVo>> listMessage(@PathVariable Integer topicId, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<Message> messageList = messageService.list(new QueryWrapper<Message>().eq("topic_id", topicId).eq("is_read", 0).eq("to_id", loginUser.getId()));
        List<MessageVo> messageVos = new ArrayList<>();
        for (Message message : messageList) {
            MessageVo messageVo = new MessageVo();
            int fromId = Math.toIntExact(message.getFromId());
            GetUserVo getUserVo = null;
            if (fromId != UserConstant.SYSTEM_USER_ID) {
                getUserVo = userService.getUserVo(userService.getById(fromId));
            }
            messageVo.setGetUserVo(getUserVo);
            messageVo.setCreateTime(message.getCreateTime());
            messageVo.setContent(message.getContent());
            messageVos.add(messageVo);
        }
        messageList.forEach(message -> message.setIsRead(1));
        messageService.updateBatchById(messageList);
        return Result.success(messageVos);
    }
}
