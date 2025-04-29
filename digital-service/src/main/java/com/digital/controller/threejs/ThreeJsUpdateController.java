package com.digital.controller.threejs;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digital.enums.ResultErrorEnum;
import com.digital.enums.ThreeJsEnum;
import com.digital.model.entity.*;
import com.digital.model.request.threejs.*;
import com.digital.result.Result;
import com.digital.service.*;
import com.digital.utils.ThreeJsUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/29 01:24
 */
@RequestMapping("/threeJs/update")
@RestController
public class ThreeJsUpdateController {
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
     * 修改孩子躯干信息
     * @param bodyReq
     * @return
     */
    @PutMapping("/body")
    public Result<Boolean> updateBody(@RequestBody BodyReq bodyReq) {
        if (bodyReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Body body = bodyService.getOne(new QueryWrapper<Body>().eq("kid_id", bodyReq.getKidId()));
        if (bodyReq.getScoliosisDegree() != null) {
            body.setScoliosisDegree(bodyReq.getScoliosisDegree());
        }
        if (bodyReq.getFlexibility() != null) {
            body.setFlexibility(bodyReq.getFlexibility());
        }
        if (bodyReq.getBodyFatPercentage() != null) {
            body.setBodyFatPercentage(bodyReq.getBodyFatPercentage());
        }
        if (bodyReq.getCoreStrength() != null) {
            body.setCoreStrength(bodyReq.getCoreStrength());
        }
        String json = "脊柱侧弯" + body.getScoliosisDegree() + SPILT + "核心肌力" + body.getCoreStrength()
                + "体脂率" + body.getBodyFatPercentage() + SPILT + "柔韧性" + body.getFlexibility() + "。";
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.BODY.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        body.setRecommendation(recommendation);
        boolean update = bodyService.update(body, new QueryWrapper<Body>().eq("kid_id", body.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 修改孩子内分泌信息
     * @param endocrineReq 包含内分泌信息的请求体
     * @return Result<Boolean> 操作结果
     */
    @PutMapping("/endocrine")
    public Result<Boolean> updateEndocrine(@RequestBody EndocrineReq endocrineReq) {
        if (endocrineReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Endocrine endocrine = endocrineService.getOne(new QueryWrapper<Endocrine>().eq("kid_id", endocrineReq.getKidId()));
        if (endocrineReq.getThyroidFunction() != null) {
            endocrine.setThyroidFunction(endocrineReq.getThyroidFunction());
        }
        if (endocrineReq.getGrowthHormone() != null) {
            endocrine.setGrowthHormone(endocrineReq.getGrowthHormone());
        }
        if (endocrineReq.getInsulin() != null) {
            endocrine.setInsulin(endocrineReq.getInsulin());
        }
        if (endocrineReq.getMetabolicRate() != null) {
            endocrine.setMetabolicRate(endocrineReq.getMetabolicRate());
        }
        String json = "甲状腺功能" + endocrine.getThyroidFunction() + "生长激素" + endocrine.getGrowthHormone()
                + "胰岛素" + endocrine.getInsulin() + "代谢率" + endocrine.getMetabolicRate() + "。";
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.ENDOCRINE.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        endocrine.setRecommendation(recommendation);
        boolean update = endocrineService.update(endocrine, new QueryWrapper<Endocrine>().eq("kid_id", endocrine.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }


    /**
     * 修改孩子耳鼻喉信息
     * @param entReq 包含耳鼻喉信息的请求体
     * @return Result<Boolean> 操作结果
     */
    @PutMapping("/ent")
    public Result<Boolean> updateEnt(@RequestBody EntReq entReq) {
        if (entReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Ent ent = entService.getOne(new QueryWrapper<Ent>().eq("kid_id", entReq.getKidId()));
        if (entReq.getLeftHearing() != null) {
            ent.setLeftHearing(entReq.getLeftHearing());
        }
        if (entReq.getRightHearing() != null) {
            ent.setRightHearing(entReq.getRightHearing());
        }
        if (entReq.getSinusCondition() != null) {
            ent.setSinusCondition(entReq.getSinusCondition());
        }
        if (entReq.getTonsilCondition() != null) {
            ent.setTonsilCondition(entReq.getTonsilCondition());
        }
        if (entReq.getThroatCondition() != null) {
            ent.setThroatCondition(entReq.getThroatCondition());
        }
        String json = "左耳听力" + ent.getLeftHearing() + "右耳听力" + ent.getRightHearing()
                + "鼻窦状况" + ent.getSinusCondition() + "扁桃体状况" + ent.getTonsilCondition()
                + "咽喉状况" + ent.getThroatCondition() + "。";
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.ENT.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        ent.setRecommendation(recommendation);
        boolean update = entService.update(ent, new QueryWrapper<Ent>().eq("kid_id", ent.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 修改孩子头部信息
     * @param headReq 包含头部信息的请求体
     * @return Result<Boolean> 操作结果
     */
    @PutMapping("/head")
    public Result<Boolean> updateHead(@RequestBody HeadReq headReq) {
        if (headReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Head head = headService.getOne(new QueryWrapper<Head>().eq("kid_id", headReq.getKidId()));
        if (headReq.getHeadacheFrequency() != null) {
            head.setHeadacheFrequency(headReq.getHeadacheFrequency());
        }
        if (headReq.getDizziness() != null) {
            head.setDizziness(headReq.getDizziness());
        }
        if (headReq.getTraumaHistory() != null) {
            head.setTraumaHistory(headReq.getTraumaHistory());
        }
        if (headReq.getCognitiveTestResult() != null) {
            head.setCognitiveTestResult(headReq.getCognitiveTestResult());
        }
        String json = "头痛频率" + head.getHeadacheFrequency() + "头晕情况" + head.getDizziness()
                + "外伤史" + head.getTraumaHistory() + "认知测试结果" + head.getCognitiveTestResult() + "。";
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.HEAD.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        head.setRecommendation(recommendation);
        boolean update = headService.update(head, new QueryWrapper<Head>().eq("kid_id", head.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 修改孩子左臂信息
     * @param leftArmReq 包含左臂信息的请求体
     * @return Result<Boolean> 操作结果
     */
    @PutMapping("/leftArm")
    public Result<Boolean> updateLeftArm(@RequestBody ArmReq leftArmReq) {
        if (leftArmReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        LeftArm leftArm = leftArmService.getOne(new QueryWrapper<LeftArm>().eq("kid_id", leftArmReq.getKidId()));
        if (leftArmReq.getGripStrength() != null) {
            leftArm.setGripStrength(leftArmReq.getGripStrength());
        }
        if (leftArmReq.getElbowRangeOfMotion() != null) {
            leftArm.setElbowRangeOfMotion(leftArmReq.getElbowRangeOfMotion());
        }
        if (leftArmReq.getTinelSign() != null) {
            leftArm.setTinelSign(leftArmReq.getTinelSign());
        }
        if (leftArmReq.getCircumferenceDifference() != null) {
            leftArm.setCircumferenceDifference(leftArmReq.getCircumferenceDifference());
        }
        String json = "握力" + leftArm.getGripStrength() + "肘部活动范围" + leftArm.getElbowRangeOfMotion()
                + "蒂内尔征" + leftArm.getTinelSign() + "周径差" + leftArm.getCircumferenceDifference() + "。";
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.ARM.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        leftArm.setRecommendation(recommendation);
        boolean update = leftArmService.update(leftArm, new QueryWrapper<LeftArm>().eq("kid_id", leftArm.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 修改孩子左脚信息
     * @param leftFootReq 包含左脚信息的请求体
     * @return Result<Boolean> 操作结果
     */
    @PutMapping("/leftFoot")
    public Result<Boolean> updateLeftFoot(@RequestBody FootReq leftFootReq) {
        if (leftFootReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        LeftFoot leftFoot = leftFootService.getOne(new QueryWrapper<LeftFoot>().eq("kid_id", leftFootReq.getKidId()));
        if (leftFootReq.getArchStatus() != null) {
            leftFoot.setArchStatus(leftFootReq.getArchStatus());
        }
        if (leftFootReq.getHalluxValgusDegree() != null) {
            leftFoot.setHalluxValgusDegree(leftFootReq.getHalluxValgusDegree());
        }
        if (leftFootReq.getCallusStatus() != null) {
            leftFoot.setCallusStatus(leftFootReq.getCallusStatus());
        }
        if (leftFootReq.getGaitCycleStatus() != null) {
            leftFoot.setGaitCycleStatus(leftFootReq.getGaitCycleStatus());
        }
        String json = "足弓状态" + leftFoot.getArchStatus() + "拇外翻程度" + leftFoot.getHalluxValgusDegree()
                + "胼胝状态" + leftFoot.getCallusStatus() + "步态周期状态" + leftFoot.getGaitCycleStatus() + "。";
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.FOOT.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        leftFoot.setRecommendation(recommendation);
        boolean update = leftFootService.update(leftFoot, new QueryWrapper<LeftFoot>().eq("kid_id", leftFoot.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 修改孩子左手信息
     * @param leftHandReq 包含左手信息的请求体
     * @return Result<Boolean> 操作结果
     */
    @PutMapping("/leftHand")
    public Result<Boolean> updateLeftHand(@RequestBody HandReq leftHandReq) {
        if (leftHandReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        LeftHand leftHand = leftHandService.getOne(new QueryWrapper<LeftHand>().eq("kid_id", leftHandReq.getKidId()));
        if (leftHandReq.getFlexibility() != null) {
            leftHand.setFlexibility(leftHandReq.getFlexibility());
        }
        if (leftHandReq.getJointSwelling() != null) {
            leftHand.setJointSwelling(leftHandReq.getJointSwelling());
        }
        if (leftHandReq.getTwoPointDiscrimination() != null) {
            leftHand.setTwoPointDiscrimination(leftHandReq.getTwoPointDiscrimination());
        }
        if (leftHandReq.getNailBedCirculation() != null) {
            leftHand.setNailBedCirculation(leftHandReq.getNailBedCirculation());
        }
        String json = "柔韧性" + leftHand.getFlexibility() + "关节肿胀情况" + leftHand.getJointSwelling()
                + "两点辨别觉" + leftHand.getTwoPointDiscrimination() + "甲床循环" + leftHand.getNailBedCirculation() + "。";
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.HAND.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        leftHand.setRecommendation(recommendation);
        boolean update = leftHandService.update(leftHand, new QueryWrapper<LeftHand>().eq("kid_id", leftHand.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 修改孩子左腿信息
     * @param leftLegReq 包含左腿信息的请求体
     * @return Result<Boolean> 操作结果
     */
    @PutMapping("/leftLeg")
    public Result<Boolean> updateLeftLeg(@RequestBody LegReq leftLegReq) {
        if (leftLegReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        LeftLeg leftLeg = leftLegService.getOne(new QueryWrapper<LeftLeg>().eq("kid_id", leftLegReq.getKidId()));
        if (leftLegReq.getLengthDifference() != null) {
            leftLeg.setLengthDifference(leftLegReq.getLengthDifference());
        }
        if (leftLegReq.getMuscleStrength() != null) {
            leftLeg.setMuscleStrength(leftLegReq.getMuscleStrength());
        }
        if (leftLegReq.getKneeReflex() != null) {
            leftLeg.setKneeReflex(leftLegReq.getKneeReflex());
        }
        if (leftLegReq.getSwellingDegree() != null) {
            leftLeg.setSwellingDegree(leftLegReq.getSwellingDegree());
        }
        String json = "腿长差" + leftLeg.getLengthDifference() + "肌肉力量" + leftLeg.getMuscleStrength()
                + "膝反射" + leftLeg.getKneeReflex() + "肿胀程度" + leftLeg.getSwellingDegree() + "。";
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.LEG.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        leftLeg.setRecommendation(recommendation);
        boolean update = leftLegService.update(leftLeg, new QueryWrapper<LeftLeg>().eq("kid_id", leftLeg.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 修改孩子左肩部信息
     * @param leftShoulderReq 包含左肩部信息的请求体
     * @return Result<Boolean> 操作结果
     */
    @PutMapping("/leftShoulder")
    public Result<Boolean> updateLeftShoulder(@RequestBody ShoulderReq leftShoulderReq) {
        if (leftShoulderReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        LeftShoulder leftShoulder = leftShoulderService.getOne(new QueryWrapper<LeftShoulder>().eq("kid_id", leftShoulderReq.getKidId()));
        if (leftShoulderReq.getRangeOfMotion() != null) {
            leftShoulder.setRangeOfMotion(leftShoulderReq.getRangeOfMotion());
        }
        if (leftShoulderReq.getPainIndex() != null) {
            leftShoulder.setPainIndex(leftShoulderReq.getPainIndex());
        }
        if (leftShoulderReq.getStability() != null) {
            leftShoulder.setStability(leftShoulderReq.getStability());
        }
        if (leftShoulderReq.getMuscleStrength() != null) {
            leftShoulder.setMuscleStrength(leftShoulderReq.getMuscleStrength());
        }
        String json = "活动范围" + leftShoulder.getRangeOfMotion() + "疼痛指数" + leftShoulder.getPainIndex()
                + "稳定性" + leftShoulder.getStability() + "肌肉力量" + leftShoulder.getMuscleStrength() + "。";
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.SHOULDER.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        leftShoulder.setRecommendation(recommendation);
        boolean update = leftShoulderService.update(leftShoulder, new QueryWrapper<LeftShoulder>().eq("kid_id", leftShoulder.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 修改孩子口腔信息
     * @param oralReq 包含口腔信息的请求体
     * @return Result<Boolean> 操作结果
     */
    @PutMapping("/oral")
    public Result<Boolean> updateOral(@RequestBody OralReq oralReq) {
        if (oralReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Oral oral = oralService.getOne(new QueryWrapper<Oral>().eq("kid_id", oralReq.getKidId()));
        if (oralReq.getDeciduousTeeth() != null) {
            oral.setDeciduousTeeth(oralReq.getDeciduousTeeth());
        }
        if (oralReq.getPermanentTeeth() != null) {
            oral.setPermanentTeeth(oralReq.getPermanentTeeth());
        }
        if (oralReq.getDecayedTeeth() != null) {
            oral.setDecayedTeeth(oralReq.getDecayedTeeth());
        }
        if (oralReq.getGumCondition() != null) {
            oral.setGumCondition(oralReq.getGumCondition());
        }
        if (oralReq.getOcclusion() != null) {
            oral.setOcclusion(oralReq.getOcclusion());
        }
        String json = "乳牙情况" + oral.getDeciduousTeeth() + "恒牙情况" + oral.getPermanentTeeth()
                + "龋齿情况" + oral.getDecayedTeeth() + "牙龈状况" + oral.getGumCondition()
                + "咬合情况" + oral.getOcclusion() + "。";
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.ORAL.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        oral.setRecommendation(recommendation);
        boolean update = oralService.update(oral, new QueryWrapper<Oral>().eq("kid_id", oral.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 修改孩子呼吸信息
     * @param respiratoryReq 包含呼吸信息的请求体
     * @return Result<Boolean> 操作结果
     */
    @PutMapping("/respiratory")
    public Result<Boolean> updateRespiratory(@RequestBody RespiratoryReq respiratoryReq) {
        if (respiratoryReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Respiratory respiratory = respiratoryService.getOne(new QueryWrapper<Respiratory>().eq("kid_id", respiratoryReq.getKidId()));
        if (respiratoryReq.getVitalCapacity() != null) {
            respiratory.setVitalCapacity(respiratoryReq.getVitalCapacity());
        }
        if (respiratoryReq.getRespiratoryFrequency() != null) {
            respiratory.setRespiratoryFrequency(respiratoryReq.getRespiratoryFrequency());
        }
        if (respiratoryReq.getLungAdventitiousSound() != null) {
            respiratory.setLungAdventitiousSound(respiratoryReq.getLungAdventitiousSound());
        }
        if (respiratoryReq.getAirwayPatency() != null) {
            respiratory.setAirwayPatency(respiratoryReq.getAirwayPatency());
        }
        String json = "肺活量" + respiratory.getVitalCapacity() + "呼吸频率" + respiratory.getRespiratoryFrequency()
                + "肺部啰音" + respiratory.getLungAdventitiousSound() + "气道通畅度" + respiratory.getAirwayPatency() + "。";
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.RESPIRATORY.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        respiratory.setRecommendation(recommendation);
        boolean update = respiratoryService.update(respiratory, new QueryWrapper<Respiratory>().eq("kid_id", respiratory.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 修改孩子右臂信息
     * @param rightArmReq 包含右臂信息的请求体
     * @return Result<Boolean> 操作结果
     */
    @PutMapping("/rightArm")
    public Result<Boolean> updateRightArm(@RequestBody ArmReq rightArmReq) {
        if (rightArmReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        RightArm rightArm = rightArmService.getOne(new QueryWrapper<RightArm>().eq("kid_id", rightArmReq.getKidId()));
        if (rightArmReq.getGripStrength() != null) {
            rightArm.setGripStrength(rightArmReq.getGripStrength());
        }
        if (rightArmReq.getElbowRangeOfMotion() != null) {
            rightArm.setElbowRangeOfMotion(rightArmReq.getElbowRangeOfMotion());
        }
        if (rightArmReq.getTinelSign() != null) {
            rightArm.setTinelSign(rightArmReq.getTinelSign());
        }
        if (rightArmReq.getCircumferenceDifference() != null) {
            rightArm.setCircumferenceDifference(rightArmReq.getCircumferenceDifference());
        }
        String json = "握力" + rightArm.getGripStrength() + "肘部活动范围" + rightArm.getElbowRangeOfMotion()
                + "蒂内尔征" + rightArm.getTinelSign() + "周径差" + rightArm.getCircumferenceDifference() + "。";
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.ARM.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        rightArm.setRecommendation(recommendation);
        boolean update = rightArmService.update(rightArm, new QueryWrapper<RightArm>().eq("kid_id", rightArm.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }
    /**
     * 修改孩子右脚信息
     * @param rightFootReq 包含右脚信息的请求体
     * @return Result<Boolean> 操作结果
     */
    @PutMapping("/rightFoot")
    public Result<Boolean> updateRightFoot(@RequestBody FootReq rightFootReq) {
        if (rightFootReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        RightFoot rightFoot = rightFootService.getOne(new QueryWrapper<RightFoot>().eq("kid_id", rightFootReq.getKidId()));
        if (rightFootReq.getArchStatus() != null) {
            rightFoot.setArchStatus(rightFootReq.getArchStatus());
        }
        if (rightFootReq.getHalluxValgusDegree() != null) {
            rightFoot.setHalluxValgusDegree(rightFootReq.getHalluxValgusDegree());
        }
        if (rightFootReq.getCallusStatus() != null) {
            rightFoot.setCallusStatus(rightFootReq.getCallusStatus());
        }
        if (rightFootReq.getGaitCycleStatus() != null) {
            rightFoot.setGaitCycleStatus(rightFootReq.getGaitCycleStatus());
        }
        String json = "足弓状态" + rightFoot.getArchStatus() + "拇外翻程度" + rightFoot.getHalluxValgusDegree()
                + "胼胝状态" + rightFoot.getCallusStatus() + "步态周期状态" + rightFoot.getGaitCycleStatus() + "。";
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.FOOT.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        rightFoot.setRecommendation(recommendation);
        boolean update = rightFootService.update(rightFoot, new QueryWrapper<RightFoot>().eq("kid_id", rightFoot.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 修改孩子右手信息
     * @param rightHandReq 包含右手信息的请求体
     * @return Result<Boolean> 操作结果
     */
    @PutMapping("/rightHand")
    public Result<Boolean> updateRightHand(@RequestBody HandReq rightHandReq) {
        if (rightHandReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        RightHand rightHand = rightHandService.getOne(new QueryWrapper<RightHand>().eq("kid_id", rightHandReq.getKidId()));
        if (rightHandReq.getFlexibility() != null) {
            rightHand.setFlexibility(rightHandReq.getFlexibility());
        }
        if (rightHandReq.getJointSwelling() != null) {
            rightHand.setJointSwelling(rightHandReq.getJointSwelling());
        }
        if (rightHandReq.getTwoPointDiscrimination() != null) {
            rightHand.setTwoPointDiscrimination(rightHandReq.getTwoPointDiscrimination());
        }
        if (rightHandReq.getNailBedCirculation() != null) {
            rightHand.setNailBedCirculation(rightHandReq.getNailBedCirculation());
        }
        String json = "柔韧性" + rightHand.getFlexibility() + "关节肿胀情况" + rightHand.getJointSwelling()
                + "两点辨别觉" + rightHand.getTwoPointDiscrimination() + "甲床循环" + rightHand.getNailBedCirculation() + "。";
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.HAND.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        rightHand.setRecommendation(recommendation);
        boolean update = rightHandService.update(rightHand, new QueryWrapper<RightHand>().eq("kid_id", rightHand.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 修改孩子右腿信息
     * @param rightLegReq 包含右腿信息的请求体
     * @return Result<Boolean> 操作结果
     */
    @PutMapping("/rightLeg")
    public Result<Boolean> updateRightLeg(@RequestBody LegReq rightLegReq) {
        if (rightLegReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        RightLeg rightLeg = rightLegService.getOne(new QueryWrapper<RightLeg>().eq("kid_id", rightLegReq.getKidId()));
        if (rightLegReq.getLengthDifference() != null) {
            rightLeg.setLengthDifference(rightLegReq.getLengthDifference());
        }
        if (rightLegReq.getMuscleStrength() != null) {
            rightLeg.setMuscleStrength(rightLegReq.getMuscleStrength());
        }
        if (rightLegReq.getKneeReflex() != null) {
            rightLeg.setKneeReflex(rightLegReq.getKneeReflex());
        }
        if (rightLegReq.getSwellingDegree() != null) {
            rightLeg.setSwellingDegree(rightLegReq.getSwellingDegree());
        }
        String json = "腿长差" + rightLeg.getLengthDifference() + "肌肉力量" + rightLeg.getMuscleStrength()
                + "膝反射" + rightLeg.getKneeReflex() + "肿胀程度" + rightLeg.getSwellingDegree() + "。";
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.LEG.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        rightLeg.setRecommendation(recommendation);
        boolean update = rightLegService.update(rightLeg, new QueryWrapper<RightLeg>().eq("kid_id", rightLeg.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 修改孩子右肩信息
     * @param rightShoulderReq 包含右肩信息的请求体
     * @return Result<Boolean> 操作结果
     */
    @PutMapping("/rightShoulder")
    public Result<Boolean> updateRightShoulder(@RequestBody ShoulderReq rightShoulderReq) {
        if (rightShoulderReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        RightShoulder rightShoulder = rightShoulderService.getOne(new QueryWrapper<RightShoulder>().eq("kid_id", rightShoulderReq.getKidId()));
        if (rightShoulderReq.getRangeOfMotion() != null) {
            rightShoulder.setRangeOfMotion(rightShoulderReq.getRangeOfMotion());
        }
        if (rightShoulderReq.getPainIndex() != null) {
            rightShoulder.setPainIndex(rightShoulderReq.getPainIndex());
        }
        if (rightShoulderReq.getStability() != null) {
            rightShoulder.setStability(rightShoulderReq.getStability());
        }
        if (rightShoulderReq.getMuscleStrength() != null) {
            rightShoulder.setMuscleStrength(rightShoulderReq.getMuscleStrength());
        }
        String json = "活动范围: " + rightShoulder.getRangeOfMotion() +
                " 疼痛指数: " + rightShoulder.getPainIndex() +
                " 稳定性: " + rightShoulder.getStability() +
                " 肌肉力量: " + rightShoulder.getMuscleStrength();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.SHOULDER.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        rightShoulder.setRecommendation(recommendation);
        boolean update = rightShoulderService.update(rightShoulder, new QueryWrapper<RightShoulder>().eq("kid_id", rightShoulder.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 修改孩子视觉信息
     * @param visualReq 包含视觉信息的请求体
     * @return Result<Boolean> 操作结果
     */
    @PutMapping("/visual")
    public Result<Boolean> updateVisual(@RequestBody VisualReq visualReq) {
        if (visualReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        Visual visual = visualService.getOne(new QueryWrapper<Visual>().eq("kid_id", visualReq.getKidId()));
        if (visualReq.getLeftVision() != null) {
            visual.setLeftVision(visualReq.getLeftVision());
        }
        if (visualReq.getRightVision() != null) {
            visual.setRightVision(visualReq.getRightVision());
        }
        if (visualReq.getLeftAstigmatism() != null) {
            visual.setLeftAstigmatism(visualReq.getLeftAstigmatism());
        }
        if (visualReq.getRightAstigmatism() != null) {
            visual.setRightAstigmatism(visualReq.getRightAstigmatism());
        }
        if (visualReq.getColorVision() != null) {
            visual.setColorVision(visualReq.getColorVision());
        }
        String json = "左眼视力: " + visual.getLeftVision() +
                " 右眼视力: " + visual.getRightVision() +
                " 左眼散光: " + visual.getLeftAstigmatism() +
                " 右眼散光: " + visual.getRightAstigmatism() +
                " 色觉: " + visual.getColorVision();
        String question = threeJsUtil.getQuestion(json, ThreeJsEnum.VISUAL.getCode());
        String recommendation = threeJsUtil.getRecommendation(question);
        visual.setRecommendation(recommendation);
        boolean update = visualService.update(visual, new QueryWrapper<Visual>().eq("kid_id", visual.getKidId()));
        if (!update) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }
}
