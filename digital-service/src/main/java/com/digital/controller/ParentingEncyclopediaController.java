package com.digital.controller;

import com.digital.annotation.AuthCheck;
import com.digital.model.request.parentingEncyclopedia.AddParentingEncyclopediaReq;
import com.digital.model.request.parentingEncyclopedia.UpdateParentingEncyclopediaReq;
import com.digital.model.vo.parentingEncyclopedia.AddParentingEncyclopediaVo;
import com.digital.model.vo.parentingEncyclopedia.GetParentingEncyclopediaVo;
import com.digital.model.vo.parentingEncyclopedia.UpdateParentingEncyclopediaVo;
import com.digital.result.Result;
import com.digital.service.ParentingEncyclopediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/parentingEncyclopedia")
public class ParentingEncyclopediaController {

    @Autowired
    ParentingEncyclopediaService parentingEncyclopediaService;

    /**
     * 得到相应stage的所有育儿百科
     * @param stage
     * @return
     */
    @GetMapping("/{stage}")
    @AuthCheck(mustRole = "guest")
    public Result<GetParentingEncyclopediaVo> getParentingEncyclopedia(@PathVariable Integer stage) {
        Result result = parentingEncyclopediaService.get(stage);
        return result;
    }

    /**
     * 删除对应id的育儿百科
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @AuthCheck(mustRole = "admin")
    public Result deleteParentingEncyclopedia(@PathVariable Integer id) {
        Result result = parentingEncyclopediaService.delete(id);
        return result;
    }

    /**
     * 修改育儿百科内容
     * @param updateParentingEncyclopediaReq
     * @return
     */
    @PutMapping
    @AuthCheck(mustRole = "admin")
    public Result<UpdateParentingEncyclopediaVo> updateParentingEncyclopediaVoResult(@RequestBody UpdateParentingEncyclopediaReq updateParentingEncyclopediaReq) {
        Result result = parentingEncyclopediaService.EncyclopediaUpdate(updateParentingEncyclopediaReq);
        return result;
    }

    /**
     * 添加育儿百科
     * @param addParentingEncyclopediaReq
     * @return
     */
    @PostMapping
    @AuthCheck(mustRole = "admin")
    public Result<AddParentingEncyclopediaVo> addParentingEncyclopediaVoResult(@RequestBody AddParentingEncyclopediaReq addParentingEncyclopediaReq) {
        Result result = parentingEncyclopediaService.Add(addParentingEncyclopediaReq);
        return result;
    }
}
