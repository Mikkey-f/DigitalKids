package com.digital.controller.threejs;

import com.digital.enums.ResultErrorEnum;
import com.digital.enums.ThreeJsEnum;
import com.digital.model.entity.*;
import com.digital.model.request.threejs.*;
import com.digital.result.Result;
import com.digital.service.*;
import com.digital.utils.ThreeJsUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/28 20:42
 */
@RequestMapping("/threeJs/add")
@RestController
public class ThreeJsAddController {

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

    private static final String SPILT = ",";

    /**
     * 添加孩子躯干信息
     * @param bodyReq
     * @return
     */
    @PostMapping("/body")
    public Result<Boolean> addBody(@RequestBody BodyReq bodyReq) {
        if (bodyReq.getBodyFatPercentage() == null || bodyReq.getFlexibility() == null
        || bodyReq.getCoreStrength() == null || bodyReq.getKidId() == null
        || bodyReq.getScoliosisDegree() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Body body = new Body();
        BeanUtils.copyProperties(bodyReq, body);
        String json = "脊柱侧弯" + bodyReq.getScoliosisDegree() + SPILT + "核心肌力" + bodyReq.getCoreStrength()
                + "体脂率" + bodyReq.getBodyFatPercentage() + SPILT + "柔韧性" + bodyReq.getFlexibility() + "。";
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.BODY.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        body.setRecommendation(recommendation);
        boolean save = bodyService.save(body);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 添加孩子内分泌
     * @param endocrineReq
     * @return
     */
    @PostMapping("/endocrine")
    public Result<Boolean> addEndocrine(@RequestBody EndocrineReq endocrineReq) {
        if (endocrineReq.getGrowthHormone() == null || endocrineReq.getInsulin() == null
        || endocrineReq.getMetabolicRate() == null || endocrineReq.getThyroidFunction() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Endocrine endocrine = new Endocrine();
        BeanUtils.copyProperties(endocrineReq, endocrine);
        String json = "甲状腺功能" + endocrineReq.getThyroidFunction() + SPILT + "生长激素" + endocrineReq.getGrowthHormone()
                + SPILT + "胰岛素" + endocrineReq.getInsulin() + SPILT + "代谢率" + endocrineReq.getMetabolicRate();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.ENDOCRINE.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        endocrine.setRecommendation(recommendation);
        boolean save = endocrineService.save(endocrine);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }


    @PostMapping("/ent")
    public Result<Boolean> addEnt (@RequestBody EntReq entReq) {
        if (entReq.getLeftHearing() == null || entReq.getRightHearing() == null
        || entReq.getSinusCondition() == null || entReq.getThroatCondition() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Ent ent = new Ent();
        BeanUtils.copyProperties(entReq, ent);
        String json = "听力(左)" + entReq.getLeftHearing() + SPILT + "听力(右)" + entReq.getRightHearing()
                + SPILT + "鼻窦" + entReq.getSinusCondition() + SPILT + "咽喉" + entReq.getThroatCondition();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.ENT.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        ent.setRecommendation(recommendation);
        boolean save = entService.save(ent);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 添加孩子头部相关信息
     * @param headReq
     * @return
     */
    @PostMapping("/head")
    public Result<Boolean> addHead(@RequestBody HeadReq headReq) {
        if (headReq.getKidId() == null
                || headReq.getHeadacheFrequency() == null
                || headReq.getDizziness() == null
                || headReq.getTraumaHistory() == null
                || headReq.getCognitiveTestResult() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Head head = new Head();
        BeanUtils.copyProperties(headReq, head);
        String json = "头痛频率" + headReq.getHeadacheFrequency() + SPILT
                + "眩晕" + headReq.getDizziness() + SPILT
                + "外伤史" + headReq.getTraumaHistory() + SPILT
                + "认知测试结果" + headReq.getCognitiveTestResult();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.HEAD.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        head.setRecommendation(recommendation);
        boolean save = headService.save(head);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 添加孩子左臂相关信息
     * @param leftArmReq
     * @return
     */
    @PostMapping("/leftArm")
    public Result<Boolean> addLeftArm(@RequestBody ArmReq leftArmReq) {
        if (leftArmReq.getKidId() == null
                || leftArmReq.getGripStrength() == null
                || leftArmReq.getElbowRangeOfMotion() == null
                || leftArmReq.getTinelSign() == null
                || leftArmReq.getCircumferenceDifference() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        LeftArm leftArm = new LeftArm();
        BeanUtils.copyProperties(leftArmReq, leftArm);
        String json = "握力" + leftArmReq.getGripStrength() + SPILT
                + "肘部活动范围" + leftArmReq.getElbowRangeOfMotion() + SPILT
                + "Tinel征" + leftArmReq.getTinelSign() + SPILT
                + "周长差异" + leftArmReq.getCircumferenceDifference();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.ARM.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        leftArm.setRecommendation(recommendation);
        boolean save = leftArmService.save(leftArm);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 添加孩子右臂相关信息
     * @param rightArmReq
     * @return
     */
    @PostMapping("/rightArm")
    public Result<Boolean> addRightArm(@RequestBody ArmReq rightArmReq) {
        if (rightArmReq.getKidId() == null
                || rightArmReq.getGripStrength() == null
                || rightArmReq.getElbowRangeOfMotion() == null
                || rightArmReq.getTinelSign() == null
                || rightArmReq.getCircumferenceDifference() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        RightArm rightArm = new RightArm();
        BeanUtils.copyProperties(rightArmReq, rightArm);
        String json = "握力" + rightArmReq.getGripStrength() + SPILT
                + "肘部活动范围" + rightArmReq.getElbowRangeOfMotion() + SPILT
                + "Tinel征" + rightArmReq.getTinelSign() + SPILT
                + "周长差异" + rightArmReq.getCircumferenceDifference();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.ARM.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        rightArm.setRecommendation(recommendation);
        boolean save = rightArmService.save(rightArm);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 添加孩子左脚相关信息
     * @param leftFootReq
     * @return
     */
    @PostMapping("/leftFoot")
    public Result<Boolean> addLeftFoot(@RequestBody FootReq leftFootReq) {
        if (leftFootReq.getKidId() == null
                || leftFootReq.getArchStatus() == null
                || leftFootReq.getHalluxValgusDegree() == null
                || leftFootReq.getCallusStatus() == null
                || leftFootReq.getGaitCycleStatus() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        LeftFoot leftFoot = new LeftFoot();
        BeanUtils.copyProperties(leftFootReq, leftFoot);
        String json = "足弓状态" + leftFootReq.getArchStatus() + SPILT
                + "拇外翻程度" + leftFootReq.getHalluxValgusDegree() + SPILT
                + "胼胝状态" + leftFootReq.getCallusStatus() + SPILT
                + "步态周期状态" + leftFootReq.getGaitCycleStatus();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.FOOT.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        leftFoot.setRecommendation(recommendation);
        boolean save = leftFootService.save(leftFoot);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 添加孩子左手相关信息
     * @param leftHandReq
     * @return
     */
    @PostMapping("/leftHand")
    public Result<Boolean> addLeftHand(@RequestBody HandReq leftHandReq) {
        if (leftHandReq.getKidId() == null
                || leftHandReq.getFlexibility() == null
                || leftHandReq.getJointSwelling() == null
                || leftHandReq.getTwoPointDiscrimination() == null
                || leftHandReq.getNailBedCirculation() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        LeftHand leftHand = new LeftHand();
        BeanUtils.copyProperties(leftHandReq, leftHand);
        String json = "柔韧性" + leftHandReq.getFlexibility() + SPILT
                + "关节肿胀情况" + leftHandReq.getJointSwelling() + SPILT
                + "两点辨别觉" + leftHandReq.getTwoPointDiscrimination() + SPILT
                + "甲床循环情况" + leftHandReq.getNailBedCirculation();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.HAND.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        leftHand.setRecommendation(recommendation);
        boolean save = leftHandService.save(leftHand);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 添加孩子左腿相关信息
     * @param leftLegReq
     * @return
     */
    @PostMapping("/leftLeg")
    public Result<Boolean> addLeftLeg(@RequestBody LegReq leftLegReq) {
        if (leftLegReq.getKidId() == null
                || leftLegReq.getLengthDifference() == null
                || leftLegReq.getMuscleStrength() == null
                || leftLegReq.getKneeReflex() == null
                || leftLegReq.getSwellingDegree() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        LeftLeg leftLeg = new LeftLeg();
        BeanUtils.copyProperties(leftLegReq, leftLeg);
        String json = "长度差异" + leftLegReq.getLengthDifference() + SPILT
                + "肌肉力量" + leftLegReq.getMuscleStrength() + SPILT
                + "膝反射" + leftLegReq.getKneeReflex() + SPILT
                + "肿胀程度" + leftLegReq.getSwellingDegree();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.LEG.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        leftLeg.setRecommendation(recommendation);
        boolean save = leftLegService.save(leftLeg);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 添加孩子左肩相关信息
     * @param leftShoulderReq
     * @return
     */
    @PostMapping("/leftShoulder")
    public Result<Boolean> addLeftShoulder(@RequestBody ShoulderReq leftShoulderReq) {
        if (leftShoulderReq.getKidId() == null
                || leftShoulderReq.getRangeOfMotion() == null
                || leftShoulderReq.getPainIndex() == null
                || leftShoulderReq.getStability() == null
                || leftShoulderReq.getMuscleStrength() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        LeftShoulder leftShoulder = new LeftShoulder();
        BeanUtils.copyProperties(leftShoulderReq, leftShoulder);
        String json = "活动范围" + leftShoulderReq.getRangeOfMotion() + SPILT
                + "疼痛指数" + leftShoulderReq.getPainIndex() + SPILT
                + "稳定性" + leftShoulderReq.getStability() + SPILT
                + "肌肉力量" + leftShoulderReq.getMuscleStrength();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.SHOULDER.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        leftShoulder.setRecommendation(recommendation);
        boolean save = leftShoulderService.save(leftShoulder);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 添加孩子口腔相关信息
     * @param oralReq
     * @return
     */
    @PostMapping("/oral")
    public Result<Boolean> addOral(@RequestBody OralReq oralReq) {
        if (oralReq.getKidId() == null
                || oralReq.getDeciduousTeeth() == null
                || oralReq.getPermanentTeeth() == null
                || oralReq.getDecayedTeeth() == null
                || oralReq.getGumCondition() == null
                || oralReq.getOcclusion() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Oral oral = new Oral();
        BeanUtils.copyProperties(oralReq, oral);
        String json = "乳牙情况" + oralReq.getDeciduousTeeth() + SPILT
                + "恒牙情况" + oralReq.getPermanentTeeth() + SPILT
                + "龋齿情况" + oralReq.getDecayedTeeth() + SPILT
                + "牙龈状况" + oralReq.getGumCondition() + SPILT
                + "咬合情况" + oralReq.getOcclusion();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.ORAL.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        oral.setRecommendation(recommendation);
        boolean save = oralService.save(oral);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 添加孩子右脚相关信息
     * @param rightFootReq
     * @return
     */
    @PostMapping("/rightFoot")
    public Result<Boolean> addRightFoot(@RequestBody FootReq rightFootReq) {
        if (rightFootReq.getKidId() == null
                || rightFootReq.getArchStatus() == null
                || rightFootReq.getHalluxValgusDegree() == null
                || rightFootReq.getCallusStatus() == null
                || rightFootReq.getGaitCycleStatus() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        RightFoot rightFoot = new RightFoot();
        BeanUtils.copyProperties(rightFootReq, rightFoot);
        String json = "足弓状态" + rightFootReq.getArchStatus() + SPILT
                + "拇外翻程度" + rightFootReq.getHalluxValgusDegree() + SPILT
                + "胼胝状态" + rightFootReq.getCallusStatus() + SPILT
                + "步态周期状态" + rightFootReq.getGaitCycleStatus();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.FOOT.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        rightFoot.setRecommendation(recommendation);
        boolean save = rightFootService.save(rightFoot);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 添加孩子右手相关信息
     * @param rightHandReq
     * @return
     */
    @PostMapping("/rightHand")
    public Result<Boolean> addRightHand(@RequestBody HandReq rightHandReq) {
        if (rightHandReq.getKidId() == null
                || rightHandReq.getFlexibility() == null
                || rightHandReq.getJointSwelling() == null
                || rightHandReq.getTwoPointDiscrimination() == null
                || rightHandReq.getNailBedCirculation() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        RightHand rightHand = new RightHand();
        BeanUtils.copyProperties(rightHandReq, rightHand);
        String json = "柔韧性" + rightHandReq.getFlexibility() + SPILT
                + "关节肿胀情况" + rightHandReq.getJointSwelling() + SPILT
                + "两点辨别觉" + rightHandReq.getTwoPointDiscrimination() + SPILT
                + "甲床循环情况" + rightHandReq.getNailBedCirculation();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.HAND.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        rightHand.setRecommendation(recommendation);
        boolean save = rightHandService.save(rightHand);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 添加孩子右腿相关信息
     * @param rightLegReq
     * @return
     */
    @PostMapping("/rightLeg")
    public Result<Boolean> addRightLeg(@RequestBody LeftLeg rightLegReq) {
        if (rightLegReq.getKidId() == null
                || rightLegReq.getLengthDifference() == null
                || rightLegReq.getMuscleStrength() == null
                || rightLegReq.getKneeReflex() == null
                || rightLegReq.getSwellingDegree() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        RightLeg rightLeg = new RightLeg();
        BeanUtils.copyProperties(rightLegReq, rightLeg);
        String json = "长度差异" + rightLegReq.getLengthDifference() + SPILT
                + "肌肉力量" + rightLegReq.getMuscleStrength() + SPILT
                + "膝反射" + rightLegReq.getKneeReflex() + SPILT
                + "肿胀程度" + rightLegReq.getSwellingDegree();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.LEG.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        rightLeg.setRecommendation(recommendation);
        boolean save = rightLegService.save(rightLeg);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 添加孩子视力相关信息
     * @param visualReq
     * @return
     */
    @PostMapping("/visual")
    public Result<Boolean> addVisual(@RequestBody VisualReq visualReq) {
        if (visualReq.getKidId() == null
                || visualReq.getLeftVision() == null
                || visualReq.getRightVision() == null
                || visualReq.getLeftAstigmatism() == null
                || visualReq.getRightAstigmatism() == null
                || visualReq.getColorVision() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Visual visual = new Visual();
        BeanUtils.copyProperties(visualReq, visual);
        String json = "左眼视力" + visualReq.getLeftVision() + SPILT
                + "右眼视力" + visualReq.getRightVision() + SPILT
                + "左眼散光" + visualReq.getLeftAstigmatism() + SPILT
                + "右眼散光" + visualReq.getRightAstigmatism() + SPILT
                + "色觉" + visualReq.getColorVision();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.VISUAL.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        visual.setRecommendation(recommendation);
        boolean save = visualService.save(visual);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 添加孩子右肩相关信息接口
     * @param rightShoulderReq 请求参数对象
     * @return 操作结果
     */
    @PostMapping
    public Result<Boolean> addRightShoulder(@RequestBody ShoulderReq rightShoulderReq) {
        // 参数校验
        if (rightShoulderReq.getKidId() == null
                || rightShoulderReq.getRangeOfMotion() == null
                || rightShoulderReq.getPainIndex() == null
                || rightShoulderReq.getStability() == null
                || rightShoulderReq.getMuscleStrength() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        RightShoulder rightShoulder = new RightShoulder();
        BeanUtils.copyProperties(rightShoulderReq, rightShoulder);
        // 拼接信息生成json字符串
        String json = "活动范围" + rightShoulderReq.getRangeOfMotion() + SPILT
                + "疼痛指数" + rightShoulderReq.getPainIndex() + SPILT
                + "稳定性" + rightShoulderReq.getStability() + SPILT
                + "肌肉力量" + rightShoulderReq.getMuscleStrength();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.SHOULDER.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        rightShoulder.setRecommendation(recommendation);
        // 保存数据到数据库
        boolean save = rightShoulderService.save(rightShoulder);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 添加孩子呼吸相关信息
     * @param respiratoryReq
     * @return
     */
    @PostMapping
    public Result<Boolean> addRespiratory(@RequestBody RespiratoryReq respiratoryReq) {
        if (respiratoryReq.getKidId() == null
                || respiratoryReq.getVitalCapacity() == null
                || respiratoryReq.getRespiratoryFrequency() == null
                || respiratoryReq.getLungAdventitiousSound() == null
                || respiratoryReq.getAirwayPatency() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Respiratory respiratory = new Respiratory();
        BeanUtils.copyProperties(respiratoryReq, respiratory);
        String json = "肺活量" + respiratoryReq.getVitalCapacity() + SPILT
                + "呼吸频率" + respiratoryReq.getRespiratoryFrequency() + SPILT
                + "肺部啰音" + respiratoryReq.getLungAdventitiousSound() + SPILT
                + "气道通畅情况" + respiratoryReq.getAirwayPatency();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.RESPIRATORY.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        respiratory.setRecommendation(recommendation);
        boolean save = respiratoryService.save(respiratory);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }
}
