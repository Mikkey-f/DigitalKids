package com.digital.service.impl;

import com.digital.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @Author: Mikkeyf
 * @CreateTime: 2025/4/20 19:39
 */
@Service
public class LikeService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 点赞
    public void like(int userId, int entityType, int entityId, int entityUserId) {

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getLikeEntityKey(String.valueOf(entityType), String.valueOf(entityId));
                String userLikeKey = RedisKeyUtil.getLikeUserKey(String.valueOf(entityUserId));
                Boolean isMember = redisOperations.opsForSet().isMember(entityLikeKey, userId);

                redisOperations.multi();

                if(isMember) {
                    redisOperations.opsForSet().remove(entityLikeKey, userId);
                    redisOperations.opsForValue().increment(userLikeKey);
                } else {
                    redisOperations.opsForSet().add(entityLikeKey, userId);
                    redisOperations.opsForValue().increment(userLikeKey);
                }
                return redisOperations.exec();
            }
        });
    }

    // 查询某实体点赞的数量
    public long findEntityLikeCount(int entityType, int entityId) {
        String likeEntityKey = RedisKeyUtil.getLikeEntityKey(String.valueOf(entityType), String.valueOf(entityId));
        return redisTemplate.opsForSet().size(likeEntityKey);
    }

    // 查询某人对某实体的点赞状态
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String likeEntityKey = RedisKeyUtil.getLikeEntityKey(String.valueOf(entityType), String.valueOf(entityId));
        return redisTemplate.opsForSet().isMember(likeEntityKey, userId) ? 1 : 0;
    }
    // 查询某个用户获得的赞
    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getLikeUserKey(String.valueOf(userId));
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count;
    }
}
