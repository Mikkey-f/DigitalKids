package com.digital.controller.admin;

import com.digital.annotation.AuthCheck;
import com.digital.constant.UserConstant;
import com.digital.model.request.parentingEncyclopedia.AddParentingEncyclopediaReq;
import com.digital.model.request.parentingEncyclopedia.UpdateParentingEncyclopediaReq;
import com.digital.result.Result;
import com.digital.service.ParentingEncyclopediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/26 21:31
 */
@RestController
@RequestMapping("/admin/parentingEncyclopedia")
public class AdminParentingEncyController {

    @Autowired
    ParentingEncyclopediaService parentingEncyclopediaService;
    /**
     * 删除对应id的育儿百科
     *
     * @param id 百科id
     * @return
     */
    @DeleteMapping("/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result deleteParentingEncyclopedia(@PathVariable Integer id) {
        return parentingEncyclopediaService.delete(id);
    }

    /**
     * 修改育儿百科内容
     * @param updateParentingEncyclopediaReq 更新百科的信息封装实体
     * @return
     */
    @PutMapping
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result updateParentingEncyclopediaVoResult(@RequestBody UpdateParentingEncyclopediaReq updateParentingEncyclopediaReq) {
         return parentingEncyclopediaService.EncyclopediaUpdate(updateParentingEncyclopediaReq);
    }

    /**
     * 添加育儿百科
     *
     * @param addParentingEncyclopediaReq 添加百科的信息封装实体
     * @return
     */
    @PostMapping
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result addParentingEncyclopediaVoResult(@RequestBody AddParentingEncyclopediaReq addParentingEncyclopediaReq) {
        return parentingEncyclopediaService.Add(addParentingEncyclopediaReq);
    }
}
