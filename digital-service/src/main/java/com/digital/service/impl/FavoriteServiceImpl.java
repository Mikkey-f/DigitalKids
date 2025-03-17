package com.digital.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digital.model.entity.Favorite;
import com.digital.model.entity.User;
import com.digital.model.vo.favorite.FavoriteVo;
import com.digital.model.vo.user.GetUserVo;
import com.digital.service.FavoriteService;
import com.digital.mapper.FavoriteMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 普罗米修斯
* @description 针对表【favorite】的数据库操作Service实现
* @createDate 2025-03-16 17:25:16
*/
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite>
    implements FavoriteService{

//    @Override
//    public List<FavoriteVo> getFavoriteVoList(List<Favorite> records) {
//        if (CollectionUtils.isEmpty(records)) {
//            return new ArrayList<>();
//        }
//        List<FavoriteVo> favoriteVoList = new ArrayList<>();
//        for (Favorite record : records) {
//            FavoriteVo favoriteVo = new FavoriteVo();
//
//        }
//        return userVOList;
//    }
}




