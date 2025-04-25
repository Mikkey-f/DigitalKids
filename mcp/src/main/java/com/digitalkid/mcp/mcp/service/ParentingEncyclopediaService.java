package com.digitalkid.mcp.service;

import com.digitalkid.mcp.model.entity.ParentingEncyclopedia;
import com.digitalkid.mcp.model.request.AddParentingEncyclopediaReq;

import java.util.List;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/24 20:33
 */

public interface ParentingEncyclopediaService {
    // 根据书名模糊查询
    List<ParentingEncyclopedia> findParentingEncyclopediaByTitle(String title);

    // 根据作者查询
    List<ParentingEncyclopedia> findEncyclopediaByStage(Integer stage);

    // 根据分类查询
    Integer deleteEncyclopediaById(Integer id);

    Integer createEncyclopedia(AddParentingEncyclopediaReq addParentingEncyclopediaReq);
}
