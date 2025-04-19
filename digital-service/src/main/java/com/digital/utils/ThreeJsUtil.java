package com.digital.utils;

import com.digital.enums.KidOldEnum;
import com.digital.model.entity.*;

import java.util.Map;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/19 22:21
 */
public class ThreeJsUtil {

    public static Map<String, String> calculateKidHead(KidHead kidHead, Integer old) {
        if (old >= KidOldEnum.CHILD.getFrom() && KidOldEnum.CHILD.getEnd() >= old) {

        } else if (old >= KidOldEnum.YOUNG.getFrom() && KidOldEnum.YOUNG.getEnd() >= old) {

        } else {

        }
    }

    public static Map<String, String> calculateKidBody(KidBody kidBody, Integer old) {

    }

    public static Map<String, String> calculateLeftArm(LeftArm leftArm, Integer old) {

    }

    public static Map<String, String> calculateLeftLeg(LeftLeg leftLeg, Integer old) {

    }

    public static Map<String, String> calculateRightArm(RightArm rightArm, Integer old) {

    }

    public static Map<String, String> calculateRightLeg(RightLeg rightLeg, Integer old) {

    }
}
