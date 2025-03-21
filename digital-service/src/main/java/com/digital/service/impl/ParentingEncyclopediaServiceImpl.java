package com.digital.service.impl;


import com.digital.event.EventProducer;
import com.digital.model.entity.Event;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.enums.ResultErrorEnum;
import com.digital.enums.ResultSuccessEnum;
import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.request.parentingEncyclopedia.AddParentingEncyclopediaReq;
import com.digital.model.request.parentingEncyclopedia.UpdateParentingEncyclopediaReq;
import com.digital.result.Result;
import com.digital.service.ParentingEncyclopediaService;
import com.digital.mapper.ParentingEncyclopediaMapper;

import org.apache.poi.ss.formula.functions.Even;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import static com.digital.constant.EntityTypeConstant.PARENTING_ENCYCLOPEDIA;
import static com.digital.constant.TopicConstant.TOPIC_PUBLISH_PARENTING_ENCY;

/**
* @description 针对表【parenting_encyclopedia】的数据库操作Service实现
*/
@Service
public class ParentingEncyclopediaServiceImpl extends ServiceImpl<ParentingEncyclopediaMapper, ParentingEncyclopedia>
    implements ParentingEncyclopediaService{

    @Autowired
    ParentingEncyclopediaMapper parentingEncyclopediaMapper;

    @Autowired
    EventProducer eventProducer;;

    @Override
    public Result get(Integer stage) {
        try {
            QueryWrapper<ParentingEncyclopedia> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stage", stage);
            List<ParentingEncyclopedia> list = baseMapper.selectList(queryWrapper);
            return Result.success(list);
        } catch (Exception e) {
            log.error("查询育儿百科数据失败: ", e);
            return Result.error(ResultErrorEnum.W_FAIL_TO_SELECT.getMessage()); // 根据实际 Result 结构调整
        }
    }

    @Override
    public Result delete(Integer id) {
        try {
            // 1. 参数校验
            if (id == null || id <= 0) {
                return Result.error(ResultErrorEnum.W_FAIL_TO_DELETE_NOT_ID.getMessage());
            }

            // 2. 执行删除（MyBatis-Plus 的 removeById 方法）
            boolean isSuccess = removeById(id);

            // 3. 根据删除结果返回响应
            if (isSuccess) {
                Result result = Result.success();
                result.setMsg(ResultSuccessEnum.W_ENCYCLOPEDIA_DELETE_SUCCESS.getMsg());
                return result;
            } else {
                return Result.error(ResultErrorEnum.W_FAIL_TO_DELETE.getMessage());
            }
        } catch (Exception e) {
            // 4. 异常处理（记录日志 + 返回友好提示）
            log.error("删除育儿百科数据异常");
            return Result.error(ResultErrorEnum.W_FAIL_TO_DELETE.getMessage());
        }
    }

    @Override
    public Result EncyclopediaUpdate(UpdateParentingEncyclopediaReq req) {
        try {
            // 1. 参数基础校验
            if (req == null) {
                return Result.error(ResultErrorEnum.W_REQUEST_IS_NULL.getMessage());
            }
            if (req.getId() == null) {
                return Result.error(ResultErrorEnum.W_ENCYCLOPEDIA_ID_IS_NULL.getMessage());
            }
            if (req.getStage() < 0 || req.getStage() > 9) {
                return Result.error(ResultErrorEnum.W_ENCYCLOPEDIA_STAGE_IS_NULL.getMessage());
            }

            // 2. 查询现有记录
            ParentingEncyclopedia entity = parentingEncyclopediaMapper.selectById(req.getId());
            if (entity == null) {
                return Result.error(ResultErrorEnum.W_ENCYCLOPEDIA_IS_NULL.getMessage());
            }


            // 4. 构建更新实体（只更新非空字段）
            ParentingEncyclopedia updateEntity = new ParentingEncyclopedia();
            updateEntity.setId(req.getId()); // 必须设置更新主键

            if (req.getStage() != null) {
                updateEntity.setStage(req.getStage());
            }
            if (StringUtils.hasText(req.getTitle())) {
                updateEntity.setTitle(req.getTitle().trim());
            }
            if (StringUtils.hasText(req.getContent())) {
                updateEntity.setContent(req.getContent().trim());
            }

            // 5. 执行更新操作
            int rows = parentingEncyclopediaMapper.updateById(updateEntity);
            if (rows <= 0) {
                return Result.error(ResultErrorEnum.W_ENCYCLOPEDIA_FAIL_TO_UPDATE.getMessage());
            }

            // 6. 返回成功结果
            Result result = Result.success();
            result.setMsg(ResultSuccessEnum.W_ENCYCLOPEDIA_UPDATE_SUCCESS.getMsg());
            return result;
        } catch (Exception e) {
            return Result.error(ResultErrorEnum.W_ENCYCLOPEDIA_FAIL_TO_UPDATE.getMessage());
        }
    }

    @Override
    @Transactional
    public Result Add(AddParentingEncyclopediaReq req) {
        try {
            // 1. 基础参数校验
            if (req == null) {
                return Result.error(ResultErrorEnum.W_REQUEST_IS_NULL.getMessage());
            }
            if (req.getUserId() == null) {
                return Result.error(ResultErrorEnum.W_ENCYCLOPEDIA_USER_ID_IS_NULL.getMessage());
            }
            if (req.getTitle() == null || req.getTitle().trim().isEmpty()) {
                return Result.error(ResultErrorEnum.W_ENCYCLOPEDIA_TITLE_IS_NULL.getMessage());
            }
            if (req.getContent() == null || req.getContent().trim().isEmpty()) {
                return Result.error(ResultErrorEnum.W_ENCYCLOPEDIA_CONTENT_IS_NULL.getMessage());
            }
            if (req.getStage() == null || req.getStage() < 0 || req.getStage() > 9) {
                return Result.error(ResultErrorEnum.W_ENCYCLOPEDIA_STAGE_IS_NULL.getMessage());
            }

            // 2. 业务逻辑校验
            // 示例：检查同用户是否重复创建相同标题的记录
            LambdaQueryWrapper<ParentingEncyclopedia> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ParentingEncyclopedia::getUserId, req.getUserId())
                    .eq(ParentingEncyclopedia::getTitle, req.getTitle().trim());
            if (parentingEncyclopediaMapper.exists(queryWrapper)) {
                return Result.error(ResultErrorEnum.W_ENCYCLOPEDIA_TITLE_IS_EXIST.getMessage());
            }

            // 3. 数据转换与填充
            ParentingEncyclopedia entity = new ParentingEncyclopedia();
            BeanUtils.copyProperties(req, entity);

            // 处理额外字段
            entity.setCreateTime(new Date());  // 创建时间
            entity.setUpdateTime(new Date());  // 更新时间
            entity.setTitle(req.getTitle().trim());    // 去除前后空格
            entity.setContent(req.getContent().trim());

            // 4. 执行插入操作
            int insertResult = parentingEncyclopediaMapper.insert(entity);
            if (insertResult <= 0) {
                return Result.error(ResultErrorEnum.W_FAIL_TO_ADD.getMessage());
            }

            Event event = Event.builder()
                    .topic(TOPIC_PUBLISH_PARENTING_ENCY)
                    .fromUserId(req.getUserId())
                    .entityId(entity.getId())
                    .entityType(PARENTING_ENCYCLOPEDIA)
                    .build();
            eventProducer.fireEvent(event);

            // 5. 返回成功结果（可返回生成的ID
            return Result.success(ResultSuccessEnum.W_ENCYCLOPEDIA_ADD_SUCCESS.getMsg());
        } catch (DuplicateKeyException e) {
            log.warn("唯一键冲突异常");
            return Result.error(ResultErrorEnum.W_ENCYCLOPEDIA_ID_IS_EXIST.getMessage());
        } catch (Exception e) {
            log.error("添加百科发生系统异常",e);
            return Result.error("系统繁忙，请稍后重试");
        }
    }
}




