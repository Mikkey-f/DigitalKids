package com.digital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digital.enums.ResultErrorEnum;
import com.digital.model.entity.*;
import com.digital.model.request.threejs.*;
import com.digital.model.vo.threejs.*;
import com.digital.result.Result;
import com.digital.service.*;
import com.digital.utils.ThreeJsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/19 19:40
 */
@RestController
@Slf4j
public class ThreeJsController {

    @Autowired
    private KidBodyService kidBodyService;

    @Autowired
    private KidHeadService kidHeadService;

    @Autowired
    private RightArmService rightArmService;

    @Autowired
    private RightLegService rightLegService;

    @Autowired
    private LeftArmService leftArmService;

    @Autowired
    private LeftLegService leftLegService;
    @Autowired
    private KidService kidService;

    @PostMapping("/kid_body/add")
    public Result addKidBody(@RequestBody KidBodyAddReq kidBodyAddReq) {
        if (kidBodyAddReq.getWeight() == null || kidBodyAddReq.getBmi() == null
        || kidBodyAddReq.getHeight() == null || kidBodyAddReq.getHeartbeatRate() == null
        || kidBodyAddReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        KidBody kidBody = new KidBody();
        KidBody kidId = kidBodyService.getOne(new QueryWrapper<KidBody>().eq("kid_id", kidBodyAddReq.getKidId()));
        if (kidId == null) {
            return Result.error(ResultErrorEnum.KID_BODY_IS_LOADED.getMessage());
        }

        BeanUtils.copyProperties(kidBodyAddReq, kidBody);
        boolean save = kidBodyService.save(kidBody);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success();
    }

    @PostMapping("/kid_head/add")
    public Result addKidHead(@RequestBody KidHeadAddReq kidHeadAddReq) {
        if (kidHeadAddReq.getKidId() == null || kidHeadAddReq.getHairColor() == null
        || kidHeadAddReq.getEyeColor() == null || kidHeadAddReq.getLeftEyeDegree() == null
        || kidHeadAddReq.getRightEyeDegree() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        KidHead kidHead = new KidHead();
        KidHead kidId = kidHeadService.getOne(new QueryWrapper<KidHead>().eq("kid_id", kidHeadAddReq.getKidId()));
        if (kidId == null) {
            return Result.error(ResultErrorEnum.KID_HEAD_IS_LOADED.getMessage());
        }

        BeanUtils.copyProperties(kidHeadAddReq, kidHead);
        boolean save = kidHeadService.save(kidHead);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success();
    }

    @PostMapping("/left_arm/add")
    public Result addLeftArm(@RequestBody LeftArmAddReq leftArmAddReq) {
        if (leftArmAddReq.getKidId() == null || leftArmAddReq.getLeftArmId() == null
        || leftArmAddReq.getArmLength() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        LeftArm leftArm = new LeftArm();
        LeftArm kidId = leftArmService.getOne(new QueryWrapper<LeftArm>().eq("kid_id", leftArmAddReq.getKidId()));
        if (kidId == null) {
            return Result.error(ResultErrorEnum.LEFT_ARM_IS_LOADED.getMessage());
        }

        BeanUtils.copyProperties(leftArmAddReq, leftArm);
        boolean save = leftArmService.save(leftArm);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success();
    }
    @PostMapping("/left_leg/add")
    public Result addLeftLeg(@RequestBody LeftLegAddReq leftLegAddReq) {
        if (leftLegAddReq.getKidId() == null || leftLegAddReq.getLeftLegId() == null
                || leftLegAddReq.getLegLength() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        LeftLeg leftLeg = new LeftLeg();
        LeftLeg kidId = leftLegService.getOne(new QueryWrapper<LeftLeg>().eq("kid_id", leftLegAddReq.getKidId()));
        if (kidId == null) {
            return Result.error(ResultErrorEnum.LEFT_LEG_IS_LOADED.getMessage());
        }

        BeanUtils.copyProperties(leftLegAddReq, leftLeg);
        boolean save = leftLegService.save(leftLeg);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success();
    }
    @PostMapping("/right_arm/add")
    public Result addRightArm(@RequestBody RightArmAddReq rightArmAddReq) {
        if (rightArmAddReq.getKidId() == null || rightArmAddReq.getRightArmId() == null
                || rightArmAddReq.getArmLength() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        RightArm rightArm = new RightArm();
        RightArm kidId = rightArmService.getOne(new QueryWrapper<RightArm>().eq("kid_id", rightArmAddReq.getKidId()));
        if (kidId == null) {
            return Result.error(ResultErrorEnum.RIGHT_ARM_IS_LOADED.getMessage());
        }

        BeanUtils.copyProperties(rightArmAddReq, rightArm);
        boolean save = rightArmService.save(rightArm);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success();
    }
    @PostMapping("/right_leg/add")
    public Result addRightLeg(@RequestBody RightLegAddReq rightLegAddReq) {
        if (rightLegAddReq.getKidId() == null || rightLegAddReq.getRightLegId() == null
                || rightLegAddReq.getLegLength() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        RightLeg rightLeg = new RightLeg();
        RightLeg kidId = rightLegService.getOne(new QueryWrapper<RightLeg>().eq("kid_id", rightLegAddReq.getKidId()));
        if (kidId == null) {
            return Result.error(ResultErrorEnum.LEFT_ARM_IS_LOADED.getMessage());
        }

        BeanUtils.copyProperties(rightLegAddReq, rightLeg);
        boolean save = rightLegService.save(rightLeg);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success();
    }



    @PutMapping("/kid_body/update")
    public Result updateKidBody(@RequestBody KidBodyUpdateReq kidBodyUpdateReq) {
        if (kidBodyUpdateReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        KidBody kidBody = new KidBody();
        BeanUtils.copyProperties(kidBodyUpdateReq, kidBody);
        boolean b = kidBodyService.updateById(kidBody);
        if (!b) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success();
    }

    @PutMapping("/kid_head/update")
    public Result updateKidHead(@RequestBody KidHeadUpdateReq kidHeadUpdateReq) {
        if (kidHeadUpdateReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        KidHead kidHead = new KidHead();
        BeanUtils.copyProperties(kidHeadUpdateReq, kidHead);
        boolean b = kidHeadService.updateById(kidHead);
        if (!b) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success();
    }

    @PutMapping("/left_arm/update")
    public Result updateLeftArm(@RequestBody LeftArmUpdateReq leftArmUpdateReq) {
        if (leftArmUpdateReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        LeftArm leftArm = new LeftArm();
        BeanUtils.copyProperties(leftArmUpdateReq, leftArm);
        boolean b = leftArmService.updateById(leftArm);
        if (!b) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success();
    }
    @PutMapping("/left_leg/update")
    public Result updateLeftLeg(@RequestBody LeftLegUpdateReq leftLegUpdateReq) {
        if (leftLegUpdateReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        LeftLeg leftLeg = new LeftLeg();
        BeanUtils.copyProperties(leftLegUpdateReq, leftLeg);
        boolean b = leftLegService.updateById(leftLeg);
        if (!b) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success();
    }
    @PutMapping("/right_arm/update")
    public Result updateRightArm(@RequestBody RightArmUpdateReq rightArmUpdateReq) {
        if (rightArmUpdateReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        RightArm rightArm = new RightArm();
        BeanUtils.copyProperties(rightArmUpdateReq, rightArm);
        boolean b = rightArmService.updateById(rightArm);
        if (!b) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success();
    }
    @PutMapping("/right_leg/update")
    public Result updateRightLeg(@RequestBody RightLegUpdateReq rightLegUpdateReq) {
        if (rightLegUpdateReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        RightLeg rightLeg = new RightLeg();
        BeanUtils.copyProperties(rightLegUpdateReq, rightLeg);
        boolean b = rightLegService.updateById(rightLeg);
        if (!b) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success();
    }
    @GetMapping("/kid_body/{kidId}")
    public Result<GetKidBodyVo> getKidBody(@PathVariable Long kidId) {
        Kid kid = kidService.getById(kidId);
        if (kid == null) {
            return Result.error(ResultErrorEnum.NOT_HAVE_THIS_KID.getMessage());
        }
        KidBody kidBody = kidBodyService.getOne(new QueryWrapper<KidBody>().eq("kid_id", kidId));
        if (kidBody == null) {
            return Result.error(ResultErrorEnum.KID_BODY_UNLOADED.getMessage());
        }
        Map<String, String> map = new ConcurrentHashMap<>();
        ThreeJsUtil.calculateKidBody(map, kidBody, kid.getOld());
        GetKidBodyVo build = GetKidBodyVo.builder()
                .bmi(kidBody.getBmi())
                .height(kidBody.getHeight())
                .heartbeatRate(kidBody.getHeartbeatRate())
                .weight(kidBody.getWeight())
                .status(map)
                .build();
        return Result.success(build);
    }

    @GetMapping("/kid_head/{kidId}")
    public Result<GetKidHeadVo> getKidHead(@PathVariable Long kidId) {
        Kid kid = kidService.getById(kidId);
        if (kid == null) {
            return Result.error(ResultErrorEnum.NOT_HAVE_THIS_KID.getMessage());
        }
        KidHead kidHead = kidHeadService.getOne(new QueryWrapper<KidHead>().eq("kid_id", kidId));
        if (kidHead == null) {
            return Result.error(ResultErrorEnum.KID_HEAD_UNLOADED.getMessage());
        }
        Map<String, String> map = new ConcurrentHashMap<>();
        ThreeJsUtil.calculateKidHead(map, kidHead, kid.getOld());
        GetKidHeadVo build = GetKidHeadVo.builder()
                .status(map)
                .hairColor(kidHead.getHairColor())
                .eyeColor(kidHead.getEyeColor())
                .leftEyeDegree(kidHead.getLeftEyeDegree())
                .rightEyeDegree(kidHead.getRightEyeDegree())
                .build();
        return Result.success(build);
    }

    @GetMapping("/left_arm/{kidId}")
    public Result<GetLeftArmVo> getLeftArm(@PathVariable Long kidId) {
        Kid kid = kidService.getById(kidId);
        if (kid == null) {
            return Result.error(ResultErrorEnum.NOT_HAVE_THIS_KID.getMessage());
        }
        LeftArm leftArm = leftArmService.getOne(new QueryWrapper<LeftArm>().eq("kid_id", kidId));
        if (leftArm == null) {
            return Result.error(ResultErrorEnum.LEFT_ARM_UNLOADED.getMessage());
        }
        Map<String, String> map = new ConcurrentHashMap<>();
        ThreeJsUtil.calculateLeftArm(map, leftArm, kid.getOld());
        GetLeftArmVo build = GetLeftArmVo.builder()
                .status(map)
                .armLength(leftArm.getArmLength())
                .build();
        return Result.success(build);
    }

    @GetMapping("/right_arm/{kidId}")
    public Result<GetRightArmVo> getRightArm(@PathVariable Long kidId) {
        Kid kid = kidService.getById(kidId);
        if (kid == null) {
            return Result.error(ResultErrorEnum.NOT_HAVE_THIS_KID.getMessage());
        }
        RightArm rightArm = rightArmService.getOne(new QueryWrapper<RightArm>().eq("kid_id", kidId));
        if (rightArm == null) {
            return Result.error(ResultErrorEnum.KID_BODY_UNLOADED.getMessage());
        }
        Map<String, String> map = new ConcurrentHashMap<>();
        ThreeJsUtil.calculateRightArm(map, rightArm, kid.getOld());
        GetRightArmVo build = GetRightArmVo.builder()
                .status(map)
                .armLength(rightArm.getArmLength())
                .build();
        return Result.success(build);
    }

    @GetMapping("/left_leg/{kidId}")
    public Result<GetLeftLegVo> getLeftLeg(@PathVariable Long kidId) {
        Kid kid = kidService.getById(kidId);
        if (kid == null) {
            return Result.error(ResultErrorEnum.NOT_HAVE_THIS_KID.getMessage());
        }
        LeftLeg leftLeg = leftLegService.getOne(new QueryWrapper<LeftLeg>().eq("kid_id", kidId));
        if (leftLeg == null) {
            return Result.error(ResultErrorEnum.KID_BODY_UNLOADED.getMessage());
        }
        Map<String, String> map = new ConcurrentHashMap<>();
        ThreeJsUtil.calculateLeftLeg(map, leftLeg, kid.getOld());
        GetLeftLegVo build = GetLeftLegVo.builder()
                .legLength(leftLeg.getLegLength())
                .status(map)
                .build();
        return Result.success(build);
    }

    @GetMapping("/right_leg/{kidId}")
    public Result<GetRightLegVo> getRightLeg(@PathVariable Long kidId) {
        Kid kid = kidService.getById(kidId);
        if (kid == null) {
            return Result.error(ResultErrorEnum.NOT_HAVE_THIS_KID.getMessage());
        }
        RightLeg rightLeg = rightLegService.getOne(new QueryWrapper<RightLeg>().eq("kid_id", kidId));
        if (rightLeg == null) {
            return Result.error(ResultErrorEnum.KID_BODY_UNLOADED.getMessage());
        }
        Map<String, String> map = new ConcurrentHashMap<>();
        ThreeJsUtil.calculateRightLeg(map, rightLeg, kid.getOld());
        GetRightLegVo build = GetRightLegVo.builder()
                .legLength(rightLeg.getLegLength())
                .status(map)
                .build();
        return Result.success(build);
    }
}
