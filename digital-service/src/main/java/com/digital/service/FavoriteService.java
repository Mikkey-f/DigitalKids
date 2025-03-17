package com.digital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digital.model.entity.Favorite;
import com.digital.model.vo.favorite.FavoriteVo;

import java.util.List;

/**
* @author 普罗米修斯
* @description 针对表【favorite】的数据库操作Service
* @createDate 2025-03-16 17:25:16
*/
public interface FavoriteService extends IService<Favorite> {

    //List<FavoriteVo> getFavoriteVoList(List<Favorite> records);
}
