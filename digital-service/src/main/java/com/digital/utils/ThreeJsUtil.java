package com.digital.utils;

import com.digital.constant.ThreeJsConstant;
import com.digital.enums.*;
import com.digital.model.entity.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/19 22:21
 */
public class ThreeJsUtil {

    public static void calculateKidHead(Map<String, String> map,  KidHead kidHead, Integer old) {

        if (old >= KidOldEnum.CHILD.getFrom() && KidOldEnum.CHILD.getEnd() >= old) {

            int resultLeftTo = kidHead.getLeftEyeDegree().compareTo(new BigDecimal(KidHeadValueEnum.CHILD.getEyeTo()));
            int resultLeftFrom = kidHead.getLeftEyeDegree().compareTo(new BigDecimal(KidHeadValueEnum.CHILD.getEyeFrom()));
            int resultRightTo = kidHead.getRightEyeDegree().compareTo(new BigDecimal(KidHeadValueEnum.CHILD.getEyeTo()));
            int resultRightFrom = kidHead.getRightEyeDegree().compareTo(new BigDecimal(KidHeadValueEnum.CHILD.getEyeFrom()));
            calGeneralInsert(map, ThreeJsConstant.LeftEyeStatus, ThreeJsConstant.Low, ThreeJsConstant.High,
                    ThreeJsConstant.Good, resultLeftTo, resultLeftFrom);
            calGeneralInsert(map, ThreeJsConstant.RightEyeStatus, ThreeJsConstant.Low, ThreeJsConstant.High,
                    ThreeJsConstant.Good, resultRightTo, resultRightFrom);

        } else if (old >= KidOldEnum.YOUNG.getFrom() && KidOldEnum.YOUNG.getEnd() >= old) {

            int resultLeftTo = kidHead.getLeftEyeDegree().compareTo(new BigDecimal(KidHeadValueEnum.YOUNG.getEyeTo()));
            int resultLeftFrom = kidHead.getLeftEyeDegree().compareTo(new BigDecimal(KidHeadValueEnum.YOUNG.getEyeFrom()));
            int resultRightTo = kidHead.getRightEyeDegree().compareTo(new BigDecimal(KidHeadValueEnum.YOUNG.getEyeTo()));
            int resultRightFrom = kidHead.getRightEyeDegree().compareTo(new BigDecimal(KidHeadValueEnum.YOUNG.getEyeFrom()));
            calGeneralInsert(map, ThreeJsConstant.LeftEyeStatus, ThreeJsConstant.Low, ThreeJsConstant.High,
                    ThreeJsConstant.Good, resultLeftTo, resultLeftFrom);
            calGeneralInsert(map, ThreeJsConstant.RightEyeStatus, ThreeJsConstant.Low, ThreeJsConstant.High,
                    ThreeJsConstant.Good, resultRightTo, resultRightFrom);

        } else {
            int resultLeftTo = kidHead.getLeftEyeDegree().compareTo(new BigDecimal(KidHeadValueEnum.TEENAGER.getEyeTo()));
            int resultLeftFrom = kidHead.getLeftEyeDegree().compareTo(new BigDecimal(KidHeadValueEnum.TEENAGER.getEyeFrom()));
            int resultRightTo = kidHead.getRightEyeDegree().compareTo(new BigDecimal(KidHeadValueEnum.TEENAGER.getEyeTo()));
            int resultRightFrom = kidHead.getRightEyeDegree().compareTo(new BigDecimal(KidHeadValueEnum.TEENAGER.getEyeFrom()));
            calGeneralInsert(map, ThreeJsConstant.LeftEyeStatus, ThreeJsConstant.Low, ThreeJsConstant.High,
                    ThreeJsConstant.Good, resultLeftTo, resultLeftFrom);
            calGeneralInsert(map, ThreeJsConstant.RightEyeStatus, ThreeJsConstant.Low, ThreeJsConstant.High,
                    ThreeJsConstant.Good, resultRightTo, resultRightFrom);
        }
    }


    public static void calculateKidBody(Map<String, String> map, KidBody kidBody, Integer old) {

        if (old >= KidOldEnum.CHILD.getFrom() && KidOldEnum.CHILD.getEnd() >= old) {
            int resultHeartFrom = kidBody.getHeartbeatRate().compareTo(KidBodyValueEnum.CHILD.getFromHeartBeat());
            int resultHeartTo = kidBody.getHeartbeatRate().compareTo(KidBodyValueEnum.CHILD.getToHeartBeat());
            int resultBmiFrom = kidBody.getBmi().compareTo(BigDecimal.valueOf(KidBodyValueEnum.CHILD.getFromBmi()));
            int resultBmiTo = kidBody.getBmi().compareTo(BigDecimal.valueOf(KidBodyValueEnum.CHILD.getToBmi()));
            calGeneralInsert(map, ThreeJsConstant.HeartBeatRateStatus, ThreeJsConstant.Low, ThreeJsConstant.High,
                    ThreeJsConstant.Good, resultHeartTo, resultHeartFrom);
            calGeneralInsert(map, ThreeJsConstant.BmiStatus, ThreeJsConstant.Low, ThreeJsConstant.High,
                    ThreeJsConstant.Good, resultBmiTo, resultBmiFrom);

        } else if (old >= KidOldEnum.YOUNG.getFrom() && KidOldEnum.YOUNG.getEnd() >= old) {

            int resultHeartFrom = kidBody.getHeartbeatRate().compareTo(KidBodyValueEnum.YOUNG.getFromHeartBeat());
            int resultHeartTo = kidBody.getHeartbeatRate().compareTo(KidBodyValueEnum.YOUNG.getToHeartBeat());
            int resultBmiFrom = kidBody.getBmi().compareTo(BigDecimal.valueOf(KidBodyValueEnum.YOUNG.getFromBmi()));
            int resultBmiTo = kidBody.getBmi().compareTo(BigDecimal.valueOf(KidBodyValueEnum.YOUNG.getToBmi()));
            calGeneralInsert(map, ThreeJsConstant.HeartBeatRateStatus, ThreeJsConstant.Low, ThreeJsConstant.High,
                    ThreeJsConstant.Good, resultHeartTo, resultHeartFrom);
            calGeneralInsert(map, ThreeJsConstant.BmiStatus, ThreeJsConstant.Low, ThreeJsConstant.High,
                    ThreeJsConstant.Good, resultBmiTo, resultBmiFrom);
        } else {
            int resultHeartFrom = kidBody.getHeartbeatRate().compareTo(KidBodyValueEnum.TEENAGER.getFromHeartBeat());
            int resultHeartTo = kidBody.getHeartbeatRate().compareTo(KidBodyValueEnum.TEENAGER.getToHeartBeat());
            int resultBmiFrom = kidBody.getBmi().compareTo(BigDecimal.valueOf(KidBodyValueEnum.TEENAGER.getFromBmi()));
            int resultBmiTo = kidBody.getBmi().compareTo(BigDecimal.valueOf(KidBodyValueEnum.TEENAGER.getToBmi()));
            calGeneralInsert(map, ThreeJsConstant.HeartBeatRateStatus, ThreeJsConstant.Low, ThreeJsConstant.High,
                    ThreeJsConstant.Good, resultHeartTo, resultHeartFrom);
            calGeneralInsert(map, ThreeJsConstant.BmiStatus, ThreeJsConstant.Low, ThreeJsConstant.High,
                    ThreeJsConstant.Good, resultBmiTo, resultBmiFrom);
        }
    }

    public static void calculateLeftArm(Map<String, String> map, LeftArm leftArm, Integer old) {
        if (old >= KidOldEnum.CHILD.getFrom() && KidOldEnum.CHILD.getEnd() >= old) {
            int ResultArmFrom = leftArm.getArmLength().compareTo(BigDecimal.valueOf(ArmValueEnum.CHILD.getFromArmLength()));
            int ResultArmTo = leftArm.getArmLength().compareTo(BigDecimal.valueOf(ArmValueEnum.CHILD.getToArmLength()));

            calGeneralInsert(map, ThreeJsConstant.LeftArmStatus, ThreeJsConstant.Long, ThreeJsConstant.Short,
                    ThreeJsConstant.Good, ResultArmTo, ResultArmFrom);

        } else if (old >= KidOldEnum.YOUNG.getFrom() && KidOldEnum.YOUNG.getEnd() >= old) {

            int ResultArmFrom = leftArm.getArmLength().compareTo(BigDecimal.valueOf(ArmValueEnum.YOUNG.getFromArmLength()));
            int ResultArmTo = leftArm.getArmLength().compareTo(BigDecimal.valueOf(ArmValueEnum.YOUNG.getToArmLength()));

            calGeneralInsert(map, ThreeJsConstant.LeftArmStatus, ThreeJsConstant.Long, ThreeJsConstant.Short,
                    ThreeJsConstant.Good, ResultArmTo, ResultArmFrom);
        } else {
            int ResultArmFrom = leftArm.getArmLength().compareTo(BigDecimal.valueOf(ArmValueEnum.TEENAGER.getFromArmLength()));
            int ResultArmTo = leftArm.getArmLength().compareTo(BigDecimal.valueOf(ArmValueEnum.TEENAGER.getToArmLength()));

            calGeneralInsert(map, ThreeJsConstant.LeftArmStatus, ThreeJsConstant.Long, ThreeJsConstant.Short,
                    ThreeJsConstant.Good, ResultArmTo, ResultArmFrom);
        }
    }

    public static void calculateLeftLeg(Map<String, String> map, LeftLeg leftLeg, Integer old) {
        if (old >= KidOldEnum.CHILD.getFrom() && KidOldEnum.CHILD.getEnd() >= old) {
            int ResultLegFrom = leftLeg.getLegLength().compareTo(BigDecimal.valueOf(LegValueEnum.CHILD.getFromLegLength()));
            int ResultLegTo = leftLeg.getLegLength().compareTo(BigDecimal.valueOf(LegValueEnum.CHILD.getToLegLength()));

            calGeneralInsert(map, ThreeJsConstant.LeftLegStatus, ThreeJsConstant.Long, ThreeJsConstant.Short,
                    ThreeJsConstant.Good, ResultLegTo, ResultLegFrom);

        } else if (old >= KidOldEnum.YOUNG.getFrom() && KidOldEnum.YOUNG.getEnd() >= old) {

            int ResultLegFrom = leftLeg.getLegLength().compareTo(BigDecimal.valueOf(LegValueEnum.YOUNG.getFromLegLength()));
            int ResultLegTo = leftLeg.getLegLength().compareTo(BigDecimal.valueOf(LegValueEnum.YOUNG.getToLegLength()));

            calGeneralInsert(map, ThreeJsConstant.LeftLegStatus, ThreeJsConstant.Long, ThreeJsConstant.Short,
                    ThreeJsConstant.Good, ResultLegTo, ResultLegFrom);
        } else {
            int ResultLegFrom = leftLeg.getLegLength().compareTo(BigDecimal.valueOf(LegValueEnum.TEENAGER.getFromLegLength()));
            int ResultLegTo = leftLeg.getLegLength().compareTo(BigDecimal.valueOf(LegValueEnum.TEENAGER.getToLegLength()));

            calGeneralInsert(map, ThreeJsConstant.LeftLegStatus, ThreeJsConstant.Long, ThreeJsConstant.Short,
                    ThreeJsConstant.Good, ResultLegTo, ResultLegFrom);
        }
    }

    public static void calculateRightArm(Map<String, String> map, RightArm rightArm, Integer old) {
        if (old >= KidOldEnum.CHILD.getFrom() && KidOldEnum.CHILD.getEnd() >= old) {
            int ResultArmFrom = rightArm.getArmLength().compareTo(BigDecimal.valueOf(ArmValueEnum.CHILD.getFromArmLength()));
            int ResultArmTo = rightArm.getArmLength().compareTo(BigDecimal.valueOf(ArmValueEnum.CHILD.getToArmLength()));

            calGeneralInsert(map, ThreeJsConstant.RightArmStatus, ThreeJsConstant.Long, ThreeJsConstant.Short,
                    ThreeJsConstant.Good, ResultArmTo, ResultArmFrom);

        } else if (old >= KidOldEnum.YOUNG.getFrom() && KidOldEnum.YOUNG.getEnd() >= old) {

            int ResultArmFrom = rightArm.getArmLength().compareTo(BigDecimal.valueOf(ArmValueEnum.YOUNG.getFromArmLength()));
            int ResultArmTo = rightArm.getArmLength().compareTo(BigDecimal.valueOf(ArmValueEnum.YOUNG.getToArmLength()));

            calGeneralInsert(map, ThreeJsConstant.RightArmStatus, ThreeJsConstant.Long, ThreeJsConstant.Short,
                    ThreeJsConstant.Good, ResultArmTo, ResultArmFrom);
        } else {
            int ResultArmFrom = rightArm.getArmLength().compareTo(BigDecimal.valueOf(ArmValueEnum.TEENAGER.getFromArmLength()));
            int ResultArmTo = rightArm.getArmLength().compareTo(BigDecimal.valueOf(ArmValueEnum.TEENAGER.getToArmLength()));

            calGeneralInsert(map, ThreeJsConstant.RightArmStatus, ThreeJsConstant.Long, ThreeJsConstant.Short,
                    ThreeJsConstant.Good, ResultArmTo, ResultArmFrom);
        }
    }

    public static void calculateRightLeg(Map<String, String> map, RightLeg rightLeg, Integer old) {
        if (old >= KidOldEnum.CHILD.getFrom() && KidOldEnum.CHILD.getEnd() >= old) {
            int ResultLegFrom = rightLeg.getLegLength().compareTo(BigDecimal.valueOf(LegValueEnum.CHILD.getFromLegLength()));
            int ResultLegTo = rightLeg.getLegLength().compareTo(BigDecimal.valueOf(LegValueEnum.CHILD.getToLegLength()));

            calGeneralInsert(map, ThreeJsConstant.RightLegStatus, ThreeJsConstant.Long, ThreeJsConstant.Short,
                    ThreeJsConstant.Good, ResultLegTo, ResultLegFrom);

        } else if (old >= KidOldEnum.YOUNG.getFrom() && KidOldEnum.YOUNG.getEnd() >= old) {

            int ResultLegFrom = rightLeg.getLegLength().compareTo(BigDecimal.valueOf(LegValueEnum.YOUNG.getFromLegLength()));
            int ResultLegTo = rightLeg.getLegLength().compareTo(BigDecimal.valueOf(LegValueEnum.YOUNG.getToLegLength()));

            calGeneralInsert(map, ThreeJsConstant.RightLegStatus, ThreeJsConstant.Long, ThreeJsConstant.Short,
                    ThreeJsConstant.Good, ResultLegTo, ResultLegFrom);
        } else {
            int ResultLegFrom = rightLeg.getLegLength().compareTo(BigDecimal.valueOf(LegValueEnum.TEENAGER.getFromLegLength()));
            int ResultLegTo = rightLeg.getLegLength().compareTo(BigDecimal.valueOf(LegValueEnum.TEENAGER.getToLegLength()));

            calGeneralInsert(map, ThreeJsConstant.RightLegStatus, ThreeJsConstant.Long, ThreeJsConstant.Short,
                    ThreeJsConstant.Good, ResultLegTo, ResultLegFrom);
        }
    }

    private static void calGeneralInsert(Map<String, String> map, String key, String low,
                                                        String high, String good, Integer to, Integer from) {
        if (to > 0) {
            map.put(key, high);
        } else if (from < 0) {
            map.put(key, low);
        } else if (to < 0 && from > 0) {
            map.put(key, good);
        }
    }

}
