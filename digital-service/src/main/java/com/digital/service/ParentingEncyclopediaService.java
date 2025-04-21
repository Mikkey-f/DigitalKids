package com.digital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digital.model.entity.ParentingEncyclopedia;
import com.digital.model.request.parentingEncyclopedia.AddParentingEncyclopediaReq;
import com.digital.model.request.parentingEncyclopedia.UpdateParentingEncyclopediaReq;
import com.digital.model.vo.parentingEncyclopedia.GetParentingEncyclopediaVo;
import com.digital.result.Result;

/**
* @description 针对表【parenting_encyclopedia】的数据库操作Service
*/
public interface ParentingEncyclopediaService extends IService<ParentingEncyclopedia> {
    Result<GetParentingEncyclopediaVo> get(Integer stage);

    Result delete(Integer id);

    Result EncyclopediaUpdate(UpdateParentingEncyclopediaReq updateParentingEncyclopediaReq);

    Result Add(AddParentingEncyclopediaReq addParentingEncyclopediaReq);
}
