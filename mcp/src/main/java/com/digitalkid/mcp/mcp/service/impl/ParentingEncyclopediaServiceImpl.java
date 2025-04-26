package com.digitalkid.mcp.mcp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digitalkid.mcp.mcp.mapper.ParentingEncyclopediaMapper;
import com.digitalkid.mcp.mcp.model.entity.ParentingEncyclopedia;
import com.digitalkid.mcp.mcp.model.request.AddParentingEncyclopediaReq;
import com.digitalkid.mcp.mcp.service.ParentingEncyclopediaService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/24 20:38
 */
@Service
@RequiredArgsConstructor
public class ParentingEncyclopediaServiceImpl implements ParentingEncyclopediaService {

    @Resource
    private ParentingEncyclopediaMapper parentingEncyclopediaMapper;

    @Override
    @Tool(name = "findParentingEncyclopediaByTitle", description = "根据百科标题查询百科文章，支持部分标题匹配")
    public List<ParentingEncyclopedia> findParentingEncyclopediaByTitle(@ToolParam(description = "百科标题字段") String title) {
        return parentingEncyclopediaMapper.selectList(new QueryWrapper<ParentingEncyclopedia>().eq("title", title));
    }

    @Override
    @Tool(name = "findEncyclopediaByStage", description = "根据阶段字段查询百科文章列表")
    public List<ParentingEncyclopedia> findEncyclopediaByStage(@ToolParam(description = "百科文章阶段字段") Integer stage) {
        return parentingEncyclopediaMapper.selectList(new QueryWrapper<ParentingEncyclopedia>().eq("stage", stage));
    }

    @Override
    @Tool(name = "deleteEncyclopediaById", description = "根据百科文章id删除数据库中的百科文章")
    public Integer deleteEncyclopediaById(Integer id) {
        return parentingEncyclopediaMapper.deleteById(id);
    }

    @Override
    @Tool(name = "createEncyclopedia", description = "根据百科文章信息添加到数据库中")
    public Integer createEncyclopedia(AddParentingEncyclopediaReq addParentingEncyclopediaReq) {
        ParentingEncyclopedia parentingEncyclopedia = new ParentingEncyclopedia();
        BeanUtils.copyProperties(addParentingEncyclopediaReq, parentingEncyclopedia);
        return parentingEncyclopediaMapper.insert(parentingEncyclopedia);
    }
}
