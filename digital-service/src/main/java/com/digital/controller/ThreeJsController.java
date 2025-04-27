package com.digital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
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

    /**
     * 给儿童身体添加信息
     * @param kidBodyAddReq
     * @return
     */
    @PostMapping("/kid_body/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result addKidBody(@RequestBody KidBodyAddReq kidBodyAddReq) {
        if (kidBodyAddReq.getWeight() == null || kidBodyAddReq.getBmi() == null
        || kidBodyAddReq.getHeight() == null || kidBodyAddReq.getHeartbeatRate() == null
        || kidBodyAddReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        Kid kid = kidService.getById(kidBodyAddReq.getKidId());
        if (kid == null) {
            return Result.error(ResultErrorEnum.KID_NOT_LOADED.getMessage());
        }

        KidBody kidBody = new KidBody();
        KidBody kidId = kidBodyService.getOne(new QueryWrapper<KidBody>().eq("kid_id", kidBodyAddReq.getKidId()));
        if (kidId != null) {
            return Result.error(ResultErrorEnum.KID_BODY_IS_LOADED.getMessage());
        }

        BeanUtils.copyProperties(kidBodyAddReq, kidBody);
        boolean save = kidBodyService.save(kidBody);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }


        return Result.success(true);
    }

    /**
     * 给儿童头部添加信息
     * @param kidHeadAddReq
     * @return
     */
    @PostMapping("/kid_head/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result addKidHead(@RequestBody KidHeadAddReq kidHeadAddReq) {
        if (kidHeadAddReq.getKidId() == null || kidHeadAddReq.getHairColor() == null
        || kidHeadAddReq.getEyeColor() == null || kidHeadAddReq.getLeftEyeDegree() == null
        || kidHeadAddReq.getRightEyeDegree() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        Kid kid = kidService.getById(kidHeadAddReq.getKidId());
        if (kid == null) {
            return Result.error(ResultErrorEnum.KID_NOT_LOADED.getMessage());
        }

        KidHead kidHead = new KidHead();
        KidHead kidId = kidHeadService.getOne(new QueryWrapper<KidHead>().eq("kid_id", kidHeadAddReq.getKidId()));
        if (kidId != null) {
            return Result.error(ResultErrorEnum.KID_HEAD_IS_LOADED.getMessage());
        }

        BeanUtils.copyProperties(kidHeadAddReq, kidHead);
        boolean save = kidHeadService.save(kidHead);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 给儿童左臂添加信息
     * @param leftArmAddReq
     * @return
     */
    @PostMapping("/left_arm/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result addLeftArm(@RequestBody LeftArmAddReq leftArmAddReq) {
        if (leftArmAddReq.getKidId() == null ||  leftArmAddReq.getArmLength() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        Kid kid = kidService.getById(leftArmAddReq.getKidId());
        if (kid == null) {
            return Result.error(ResultErrorEnum.KID_NOT_LOADED.getMessage());
        }

        LeftArm leftArm = new LeftArm();
        LeftArm kidId = leftArmService.getOne(new QueryWrapper<LeftArm>().eq("kid_id", leftArmAddReq.getKidId()));
        if (kidId != null) {
            return Result.error(ResultErrorEnum.LEFT_ARM_IS_LOADED.getMessage());
        }

        BeanUtils.copyProperties(leftArmAddReq, leftArm);
        boolean save = leftArmService.save(leftArm);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 给儿童左腿添加信息
     * @param leftLegAddReq
     * @return
     */
    @PostMapping("/left_leg/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result addLeftLeg(@RequestBody LeftLegAddReq leftLegAddReq) {
        if (leftLegAddReq.getKidId() == null || leftLegAddReq.getLegLength() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        Kid kid = kidService.getById(leftLegAddReq.getKidId());
        if (kid == null) {
            return Result.error(ResultErrorEnum.KID_NOT_LOADED.getMessage());
        }

        LeftLeg leftLeg = new LeftLeg();
        LeftLeg kidId = leftLegService.getOne(new QueryWrapper<LeftLeg>().eq("kid_id", leftLegAddReq.getKidId()));
        if (kidId != null) {
            return Result.error(ResultErrorEnum.LEFT_LEG_IS_LOADED.getMessage());
        }

        BeanUtils.copyProperties(leftLegAddReq, leftLeg);
        boolean save = leftLegService.save(leftLeg);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 给儿童右臂添加信息
     * @param rightArmAddReq
     * @return
     */
    @PostMapping("/right_arm/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result addRightArm(@RequestBody RightArmAddReq rightArmAddReq) {
        if (rightArmAddReq.getKidId() == null || rightArmAddReq.getArmLength() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        Kid kid = kidService.getById(rightArmAddReq.getKidId());
        if (kid == null) {
            return Result.error(ResultErrorEnum.KID_NOT_LOADED.getMessage());
        }

        RightArm rightArm = new RightArm();
        RightArm kidId = rightArmService.getOne(new QueryWrapper<RightArm>().eq("kid_id", rightArmAddReq.getKidId()));
        if (kidId != null) {
            return Result.error(ResultErrorEnum.RIGHT_ARM_IS_LOADED.getMessage());
        }

        BeanUtils.copyProperties(rightArmAddReq, rightArm);
        boolean save = rightArmService.save(rightArm);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }

    /**
     * 给儿童右腿添加信息
     * @param rightLegAddReq
     * @return
     */
    @PostMapping("/right_leg/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result addRightLeg(@RequestBody RightLegAddReq rightLegAddReq) {
        if (rightLegAddReq.getKidId() == null || rightLegAddReq.getLegLength() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }

        Kid kid = kidService.getById(rightLegAddReq.getKidId());
        if (kid == null) {
            return Result.error(ResultErrorEnum.KID_NOT_LOADED.getMessage());
        }

        RightLeg rightLeg = new RightLeg();
        RightLeg kidId = rightLegService.getOne(new QueryWrapper<RightLeg>().eq("kid_id", rightLegAddReq.getKidId()));
        if (kidId != null) {
            return Result.error(ResultErrorEnum.LEFT_ARM_IS_LOADED.getMessage());
        }

        BeanUtils.copyProperties(rightLegAddReq, rightLeg);
        boolean save = rightLegService.save(rightLeg);
        if (!save) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }
        return Result.success(true);
    }


    /**
     * 修改儿童身体信息
     * @param kidBodyUpdateReq
     * @return
     */
    @PutMapping("/kid_body/update")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result updateKidBody(@RequestBody KidBodyUpdateReq kidBodyUpdateReq) {
        if (kidBodyUpdateReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        KidBody kidBody = new KidBody();
        BeanUtils.copyProperties(kidBodyUpdateReq, kidBody);
        boolean b = kidBodyService.update(kidBody, new QueryWrapper<KidBody>().eq("kid_id", kidBody.getKidId()));
        if (!b) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success(true);
    }

    /**
     * 修改儿童头部信息
     * @param kidHeadUpdateReq
     * @return
     */
    @PutMapping("/kid_head/update")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result updateKidHead(@RequestBody KidHeadUpdateReq kidHeadUpdateReq) {
        if (kidHeadUpdateReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        KidHead kidHead = new KidHead();
        BeanUtils.copyProperties(kidHeadUpdateReq, kidHead);
        boolean b = kidHeadService.update(kidHead, new QueryWrapper<KidHead>().eq("kid_id", kidHead.getKidId()));
        if (!b) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success(true);
    }

    /**
     * 修改儿童左臂信息
     * @param leftArmUpdateReq
     * @return
     */
    @PutMapping("/left_arm/update")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result updateLeftArm(@RequestBody LeftArmUpdateReq leftArmUpdateReq) {
        if (leftArmUpdateReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        LeftArm leftArm = new LeftArm();
        BeanUtils.copyProperties(leftArmUpdateReq, leftArm);
        boolean b = leftArmService.update(leftArm, new QueryWrapper<LeftArm>().eq("kid_id", leftArm.getKidId()));
        if (!b) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success(true);
    }

    /**
     * 修改儿童左腿信息
     * @param leftLegUpdateReq
     * @return
     */
    @PutMapping("/left_leg/update")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result updateLeftLeg(@RequestBody LeftLegUpdateReq leftLegUpdateReq) {
        if (leftLegUpdateReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        LeftLeg leftLeg = new LeftLeg();
        BeanUtils.copyProperties(leftLegUpdateReq, leftLeg);
        boolean b = leftLegService.update(leftLeg, new QueryWrapper<LeftLeg>().eq("kid_id", leftLeg.getKidId()));
        if (!b) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success(true);
    }

    /**
     * 修改儿童右臂信息
     * @param rightArmUpdateReq
     * @return
     */
    @PutMapping("/right_arm/update")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result updateRightArm(@RequestBody RightArmUpdateReq rightArmUpdateReq) {
        if (rightArmUpdateReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        RightArm rightArm = new RightArm();
        BeanUtils.copyProperties(rightArmUpdateReq, rightArm);
        boolean b = rightArmService.update(rightArm, new QueryWrapper<RightArm>().eq("kid_id", rightArm.getKidId()));
        if (!b) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success(true);
    }

    /**
     * 修改儿童右腿信息
     * @param rightLegUpdateReq
     * @return
     */
    @PutMapping("/right_leg/update")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public Result updateRightLeg(@RequestBody RightLegUpdateReq rightLegUpdateReq) {
        if (rightLegUpdateReq.getKidId() == null) {
            return Result.error(ResultErrorEnum.PARAM_IS_ERROR.getMessage());
        }
        RightLeg rightLeg = new RightLeg();
        BeanUtils.copyProperties(rightLegUpdateReq, rightLeg);
        boolean b = rightLegService.update(rightLeg, new QueryWrapper<RightLeg>().eq("kid_id", rightLeg.getKidId()));
        if (!b) {
            return Result.error(ResultErrorEnum.OPERATION_ERROR.getMessage());
        }

        return Result.success(true);
    }

    /**
     * 获取儿童身体信息
     * @param kidId
     * @return
     */
    @GetMapping("/kid_body/{kidId}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
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

    /**
     * 获取儿童头部信息
     * @param kidId
     * @return
     */
    @GetMapping("/kid_head/{kidId}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
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

    /**
     * 获取儿童左臂信息
     * @param kidId
     * @return
     */
    @GetMapping("/left_arm/{kidId}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
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

    /**
     * 获取儿童右臂信息
     * @param kidId
     * @return
     */
    @GetMapping("/right_arm/{kidId}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
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

    /**
     * 获取儿童左腿信息
     * @param kidId
     * @return
     */
    @GetMapping("/left_leg/{kidId}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
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

    /**
     * 获取儿童右腿信息
     * @param kidId
     * @return
     */
    @GetMapping("/right_leg/{kidId}")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
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
