package com.digital.controller.threejs;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digital.constant.ThreeJsConstant;
import com.digital.enums.ThreeJsEnum;
import com.digital.model.entity.*;
import com.digital.model.vo.threejs.ThreeJsVo;
import com.digital.result.Result;
import com.digital.service.*;
import com.digital.utils.ThreeJsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/29 00:58
 */
@RequestMapping("/threeJs/get")
@RestController
public class ThreeJsGetController {

    @Autowired
    BodyService bodyService;

    @Autowired
    EndocrineService endocrineService;

    @Autowired
    EntService entService;

    @Autowired
    HeadService headService;

    @Autowired
    LeftArmService leftArmService;

    @Autowired
    LeftFootService leftFootService;

    @Autowired
    LeftHandService leftHandService;

    @Autowired
    LeftLegService leftLegService;

    @Autowired
    LeftShoulderService leftShoulderService;

    @Autowired
    OralService oralService;

    @Autowired
    RespiratoryService respiratoryService;

    @Autowired
    RightArmService rightArmService;

    @Autowired
    RightFootService rightFootService;

    @Autowired
    RightHandService rightHandService;

    @Autowired
    RightLegService rightLegService;

    @Autowired
    RightShoulderService rightShoulderService;

    @Autowired
    VisualService visualService;

    @Autowired
    ThreeJsUtil threeJsUtil;

    /**
     * 查看躯干
     * @param kidId
     * @return
     */
    @GetMapping("/body/{kidId}")
    public Result<ThreeJsVo> getBody(@PathVariable Long kidId) {
        Body body = bodyService.getOne(new QueryWrapper<Body>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("躯干");
        threeJsVo.setDescription(ThreeJsEnum.BODY.getName());
        threeJsVo.setIcon("Torso");
        threeJsVo.setRecommendation(body.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("脊柱侧弯", body.getScoliosisDegree());
        map.put("核心肌力", body.getCoreStrength());
        map.put("体脂率", body.getBodyFatPercentage());
        map.put("柔韧性", body.getFlexibility());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }

    /**
     * 查看内分泌
     * @param kidId
     * @return
     */
    @GetMapping("/endocrine/{kidId}")
    public Result<ThreeJsVo> getEndocrine(@PathVariable Long kidId) {
        Endocrine endocrine = endocrineService.getOne(new QueryWrapper<Endocrine>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("内分泌");
        threeJsVo.setDescription(ThreeJsEnum.ENDOCRINE.getName());
        threeJsVo.setIcon("EndocrineIcon"); // 假设图标名称
        threeJsVo.setRecommendation(endocrine.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("甲状腺功能", endocrine.getThyroidFunction());
        map.put("生长激素", endocrine.getGrowthHormone());
        map.put("胰岛素", endocrine.getInsulin());
        map.put("代谢率", endocrine.getMetabolicRate());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }

    /**
     * 查看耳鼻喉
     * @param kidId
     * @return
     */
    @GetMapping("/ent/{kidId}")
    public Result<ThreeJsVo> getEnt(@PathVariable Long kidId) {
        Ent ent = entService.getOne(new QueryWrapper<Ent>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("耳鼻喉");
        threeJsVo.setDescription(ThreeJsEnum.ENT.getName());
        threeJsVo.setIcon("Notification"); // 假设图标名称
        threeJsVo.setRecommendation(ent.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("听力(左)", ent.getLeftHearing());
        map.put("听力(右)", ent.getRightHearing());
        map.put("鼻窦情况", ent.getSinusCondition());
        map.put("扁桃体情况", ent.getTonsilCondition());
        map.put("咽喉情况", ent.getThroatCondition());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }

    /**
     * 查看头部
     * @param kidId
     * @return
     */
    @GetMapping("/head/{kidId}")
    public Result<ThreeJsVo> getHead(@PathVariable Long kidId) {
        Head head = headService.getOne(new QueryWrapper<Head>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("头部");
        threeJsVo.setDescription(ThreeJsEnum.HEAD.getName());
        threeJsVo.setIcon("HeadIcon"); // 假设图标名称
        threeJsVo.setRecommendation(head.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("头痛频率", head.getHeadacheFrequency());
        map.put("头晕情况", head.getDizziness());
        map.put("外伤史", head.getTraumaHistory());
        map.put("认知测试结果", head.getCognitiveTestResult());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }

    /**
     * 查看左臂
     * @param kidId
     * @return
     */
    @GetMapping("/leftArm/{kidId}")
    public Result<ThreeJsVo> getLeftArm(@PathVariable Long kidId) {
        LeftArm leftArm = leftArmService.getOne(new QueryWrapper<LeftArm>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("左臂");
        threeJsVo.setDescription(ThreeJsEnum.ARM.getName());
        threeJsVo.setIcon("LeftArmIcon"); // 假设图标名称
        threeJsVo.setRecommendation(leftArm.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("握力", leftArm.getGripStrength());
        map.put("肘部活动范围", leftArm.getElbowRangeOfMotion());
        map.put("Tinel征", leftArm.getTinelSign());
        map.put("周长差异", leftArm.getCircumferenceDifference());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }

    /**
     * 查看左脚
     * @param kidId
     * @return
     */
    @GetMapping("/leftFoot/{kidId}")
    public Result<ThreeJsVo> getLeftFoot(@PathVariable Long kidId) {
        LeftFoot leftFoot = leftFootService.getOne(new QueryWrapper<LeftFoot>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("左脚");
        threeJsVo.setDescription(ThreeJsEnum.FOOT.getName());
        threeJsVo.setIcon("LeftFootIcon"); // 假设图标名称
        threeJsVo.setRecommendation(leftFoot.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("足弓状态", leftFoot.getArchStatus());
        map.put("拇外翻程度", leftFoot.getHalluxValgusDegree());
        map.put("胼胝状态", leftFoot.getCallusStatus());
        map.put("步态周期状态", leftFoot.getGaitCycleStatus());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }

    /**
     * 查看左手
     * @param kidId
     * @return
     */
    @GetMapping("/leftHand/{kidId}")
    public Result<ThreeJsVo> getLeftHand(@PathVariable Long kidId) {
        LeftHand leftHand = leftHandService.getOne(new QueryWrapper<LeftHand>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("左手");
        threeJsVo.setDescription(ThreeJsEnum.HAND.getName());
        threeJsVo.setIcon("LeftHandIcon"); // 假设图标名称
        threeJsVo.setRecommendation(leftHand.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("柔韧性", leftHand.getFlexibility());
        map.put("关节肿胀情况", leftHand.getJointSwelling());
        map.put("两点辨别觉", leftHand.getTwoPointDiscrimination());
        map.put("甲床循环情况", leftHand.getNailBedCirculation());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }

    @GetMapping("/leftLeg/{kidId}")
    public Result<ThreeJsVo> getLeftLeg(@PathVariable Long kidId) {
        LeftLeg leftLeg = leftLegService.getOne(new QueryWrapper<LeftLeg>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("左腿");
        threeJsVo.setDescription(ThreeJsEnum.LEG.getName());
        threeJsVo.setIcon("LeftLegIcon"); // 假设图标名称
        threeJsVo.setRecommendation(leftLeg.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("长度差异", leftLeg.getLengthDifference());
        map.put("肌肉力量", leftLeg.getMuscleStrength());
        map.put("膝反射", leftLeg.getKneeReflex());
        map.put("肿胀程度", leftLeg.getSwellingDegree());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }

    @GetMapping("/leftShoulder/{kidId}")
    public Result<ThreeJsVo> getLeftShoulder(@PathVariable Long kidId) {
        LeftShoulder leftShoulder = leftShoulderService.getOne(new QueryWrapper<LeftShoulder>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("左肩");
        threeJsVo.setDescription(ThreeJsEnum.SHOULDER.getName());
        threeJsVo.setIcon("LeftShoulderIcon"); // 假设图标名称
        threeJsVo.setRecommendation(leftShoulder.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("活动范围", leftShoulder.getRangeOfMotion());
        map.put("疼痛指数", leftShoulder.getPainIndex());
        map.put("稳定性", leftShoulder.getStability());
        map.put("肌肉力量", leftShoulder.getMuscleStrength());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }

    @GetMapping("/oral/{kidId}")
    public Result<ThreeJsVo> getOral(@PathVariable Long kidId) {
        Oral oral = oralService.getOne(new QueryWrapper<Oral>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("口腔");
        threeJsVo.setDescription(ThreeJsEnum.ORAL.getName());
        threeJsVo.setIcon("OralIcon"); // 假设图标名称
        threeJsVo.setRecommendation(oral.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("乳牙情况", oral.getDeciduousTeeth());
        map.put("恒牙情况", oral.getPermanentTeeth());
        map.put("龋齿情况", oral.getDecayedTeeth());
        map.put("牙龈状况", oral.getGumCondition());
        map.put("咬合情况", oral.getOcclusion());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }

    @GetMapping("/respiratory/{kidId}")
    public Result<ThreeJsVo> getRespiratory(@PathVariable Long kidId) {
        Respiratory respiratory = respiratoryService.getOne(new QueryWrapper<Respiratory>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("呼吸相关");
        threeJsVo.setDescription(ThreeJsEnum.RESPIRATORY.getName());
        threeJsVo.setIcon("RespiratoryIcon"); // 假设图标名称
        threeJsVo.setRecommendation(respiratory.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("肺活量", respiratory.getVitalCapacity());
        map.put("呼吸频率", respiratory.getRespiratoryFrequency());
        map.put("肺部啰音", respiratory.getLungAdventitiousSound());
        map.put("气道通畅情况", respiratory.getAirwayPatency());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }

    @GetMapping("/rightArm/{kidId}")
    public Result<ThreeJsVo> getRightArm(@PathVariable Long kidId) {
        RightArm rightArm = rightArmService.getOne(new QueryWrapper<RightArm>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("右臂");
        threeJsVo.setDescription(ThreeJsEnum.ARM.getName());
        threeJsVo.setIcon("RightArmIcon"); // 假设图标名称
        threeJsVo.setRecommendation(rightArm.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("握力", rightArm.getGripStrength());
        map.put("肘部活动范围", rightArm.getElbowRangeOfMotion());
        map.put("Tinel征", rightArm.getTinelSign());
        map.put("周长差异", rightArm.getCircumferenceDifference());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }
    @GetMapping("/rightFoot/{kidId}")
    public Result<ThreeJsVo> getRightFoot(@PathVariable Long kidId) {
        RightFoot rightFoot = rightFootService.getOne(new QueryWrapper<RightFoot>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("右脚");
        threeJsVo.setDescription(ThreeJsEnum.FOOT.getName());
        threeJsVo.setIcon("RightFootIcon"); // 假设图标名称
        threeJsVo.setRecommendation(rightFoot.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("足弓状态", rightFoot.getArchStatus());
        map.put("拇外翻程度", rightFoot.getHalluxValgusDegree());
        map.put("胼胝状态", rightFoot.getCallusStatus());
        map.put("步态周期状态", rightFoot.getGaitCycleStatus());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }

    @GetMapping("/rightHand/{kidId}")
    public Result<ThreeJsVo> getRightHand(@PathVariable Long kidId) {
        RightHand rightHand = rightHandService.getOne(new QueryWrapper<RightHand>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("右手");
        threeJsVo.setDescription(ThreeJsEnum.HAND.getName());
        threeJsVo.setIcon("RightHandIcon"); // 假设图标名称
        threeJsVo.setRecommendation(rightHand.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("柔韧性", rightHand.getFlexibility());
        map.put("关节肿胀情况", rightHand.getJointSwelling());
        map.put("两点辨别觉", rightHand.getTwoPointDiscrimination());
        map.put("甲床循环情况", rightHand.getNailBedCirculation());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }

    @GetMapping("/rightLeg/{kidId}")
    public Result<ThreeJsVo> getRightLeg(@PathVariable Long kidId) {
        RightLeg rightLeg = rightLegService.getOne(new QueryWrapper<RightLeg>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("右腿");
        threeJsVo.setDescription(ThreeJsEnum.LEG.getName());
        threeJsVo.setIcon("RightLegIcon"); // 假设图标名称
        threeJsVo.setRecommendation(rightLeg.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("长度差异", rightLeg.getLengthDifference());
        map.put("肌肉力量", rightLeg.getMuscleStrength());
        map.put("膝反射", rightLeg.getKneeReflex());
        map.put("肿胀程度", rightLeg.getSwellingDegree());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }

    @GetMapping("/rightShoulder/{kidId}")
    public Result<ThreeJsVo> getRightShoulder(@PathVariable Long kidId) {
        RightShoulder rightShoulder = rightShoulderService.getOne(new QueryWrapper<RightShoulder>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("右肩");
        threeJsVo.setDescription(ThreeJsEnum.SHOULDER.getName());
        threeJsVo.setIcon("RightShoulderIcon"); // 假设图标名称
        threeJsVo.setRecommendation(rightShoulder.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("活动范围", rightShoulder.getRangeOfMotion());
        map.put("疼痛指数", rightShoulder.getPainIndex());
        map.put("稳定性", rightShoulder.getStability());
        map.put("肌肉力量", rightShoulder.getMuscleStrength());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }


    @GetMapping("/visual/{kidId}")
    public Result<ThreeJsVo> getVisual(@PathVariable Long kidId) {
        Visual visual = visualService.getOne(new QueryWrapper<Visual>().eq("kid_id", kidId));
        ThreeJsVo threeJsVo = new ThreeJsVo();
        threeJsVo.setName("视力");
        threeJsVo.setDescription(ThreeJsEnum.VISUAL.getName());
        threeJsVo.setIcon("VisualIcon"); // 假设图标名称
        threeJsVo.setRecommendation(visual.getRecommendation());
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("左眼视力", visual.getLeftVision());
        map.put("右眼视力", visual.getRightVision());
        map.put("左眼散光", visual.getLeftAstigmatism());
        map.put("右眼散光", visual.getRightAstigmatism());
        map.put("色觉", visual.getColorVision());
        threeJsVo.setMap(map);
        return Result.success(threeJsVo);
    }
}
