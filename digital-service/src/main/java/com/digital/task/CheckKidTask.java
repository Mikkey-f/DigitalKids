package com.digital.task;

import com.alibaba.druid.support.json.JSONUtils;
import com.digital.constant.TopicConstant;
import com.digital.model.entity.*;
import com.digital.service.*;
import com.digital.utils.SseUtil;
import com.digital.utils.ThreeJsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/21 22:12
 */
@Component
public class CheckKidTask {

    @Autowired
    private KidService kidService;

    @Autowired
    private KidBodyService kidbodyService;

    @Autowired
    private KidHeadService kidheadService;

    @Autowired
    private LeftArmService leftArmService;

    @Autowired
    private LeftLegService leftLegService;

    @Autowired
    private RightArmService rightArmService;

    @Autowired
    private RightLegService rightLegService;

    @Autowired
    private SseUtil sseUtil;

    // 每 10 分钟执行一次，Cron 表达式：0 0/10 * * * ?
    @Scheduled(cron = "0/10 * * * * *")
    public void checkKidDatabase() {
        // 在这里编写扫描 kid 数据库表的逻辑，例如查询数据库、处理数据等
        List<Kid> kidList = kidService.list();
        List<KidBody> kidBodyList = kidbodyService.list();
        List<LeftArm> leftArmList = leftArmService.list();
        List<LeftLeg> leftLegList = leftLegService.list();
        List<RightArm> rightArmList = rightArmService.list();
        List<RightLeg> rightLegList = rightLegService.list();
        List<KidHead> kidHeadList = kidheadService.list();
        for (Kid kid : kidList) {
            Map<String, String> map = new ConcurrentHashMap<>();
            List<KidBody> kidBodies = kidBodyList.stream().filter(kidBody -> kidBody.getKidId().equals(kid.getId()))
                    .toList();
            List<LeftArm> leftArms = leftArmList.stream().filter(leftArm -> leftArm.getKidId().equals(kid.getId()))
                    .toList();
            List<LeftLeg> leftLegs = leftLegList.stream().filter(leftLeg -> leftLeg.getKidId().equals(kid.getId()))
                    .toList();
            List<RightArm> rightArms = rightArmList.stream().filter(rightArm -> rightArm.getKidId().equals(kid.getId()))
                    .toList();
            List<RightLeg> rightLegs = rightLegList.stream().filter(rightLeg -> rightLeg.getKidId().equals(kid.getId()))
                    .toList();
            List<KidHead> kidHeads = kidHeadList.stream().filter(kidHead -> kidHead.getKidId().equals(kid.getId()))
                    .toList();


            ThreeJsUtil.calculateRightLeg(map, rightLegs.get(0), kid.getOld());
            ThreeJsUtil.calculateKidHead(map, kidHeads.get(0), kid.getOld());
            ThreeJsUtil.calculateKidBody(map, kidBodies.get(0), kid.getOld());
            ThreeJsUtil.calculateRightArm(map, rightArms.get(0), kid.getOld());
            ThreeJsUtil.calculateLeftLeg(map, leftLegs.get(0), kid.getOld());
            ThreeJsUtil.calculateLeftArm(map, leftArms.get(0), kid.getOld());
            map.put("topicId", String.valueOf(TopicConstant.TOPIC_ALARM_ID));
            String jsonString = JSONUtils.toJSONString(map);
            sseUtil.sendMessage(kid.getUserId(), String.valueOf(UUID.randomUUID()), jsonString);
        }
    }
}
