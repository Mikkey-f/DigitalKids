CREATE DATABASE digitalKids;
CREATE TABLE role (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL
);

CREATE TABLE Permission (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL
);

CREATE TABLE role_permission (
                                 role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 permission_id BIGINT
);

CREATE TABLE user_role (
                           user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           role_id VARCHAR(255) NOT NULL
);

create table user
(
    id          bigint auto_increment
        primary key,
    name        varchar(32)                         not null,
    password    varchar(64)                         not null,
    avatar      varchar(500)                        not null,
    gender      enum ('男', '女', '其他')           null,
    phone       varchar(11)                         not null,
    location    varchar(200) charset utf8mb4        null,
    create_time timestamp default CURRENT_TIMESTAMP null,
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
)
    collate = utf8mb3_bin;

INSERT INTO role (id, name) VALUES
                                (1, 'admin'),
                                (2, 'user'),
                                (3, 'guest');

INSERT INTO permission (id, name) VALUES
    (1, 1);

INSERT INTO role_permission (role_id, permission_id) VALUES
                                                         (1, 1),
                                                         (2, 1);

INSERT INTO user_role (user_id, role_id) VALUES
    (1, 2);

-- 创建 favorite 表
CREATE TABLE favorite (
    -- 收藏记录的唯一标识，自增主键
                          id INT AUTO_INCREMENT PRIMARY KEY,
    -- 收藏类型，可取值为 '评论'、'视频'、'帖子'
                          type ENUM('评论', '视频', '帖子') NOT NULL,
    -- 收藏对象的 ID，根据收藏类型对应不同表的主键
                          target_id INT NOT NULL,
    -- 关联用户表的用户 ID，外键约束
                          user_id BIGINT NOT NULL,
    -- 收藏记录的创建时间，默认值为当前时间
                          create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- 收藏记录的更新时间，在记录更新时自动更新为当前时间
                          update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    -- 外键约束，关联用户表（假设用户表名为 user，主键为 id）
                          FOREIGN KEY (user_id) REFERENCES user(id)
);




DROP TABLE IF EXISTS `Kid`;
CREATE TABLE `Kid` (
                       `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '孩子唯一标识',
                       `user_id` BIGINT NOT NULL COMMENT '关联的用户ID',
                       `avatar` VARCHAR(500) COLLATE utf8mb4_bin DEFAULT '/default_avatar.png' COMMENT '头像URL',
                       `nickname` VARCHAR(50) COLLATE utf8mb4_bin NOT NULL COMMENT '昵称',
                       `birthdate` VARCHAR(50) COMMENT '生日',
                       `height` DECIMAL(4,2) COMMENT '身高(单位：米)',
                       `weight` DECIMAL(5,2) COMMENT '体重(单位：千克)',
                       `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='孩子信息表';

DROP TABLE IF EXISTS `parenting_encyclopedia`;
CREATE TABLE parenting_encyclopedia (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        stage INT NOT NULL COMMENT '0:备孕期, 1:孕产期管理, 2:产褥期, 3:产后恢复, 4:0-1岁宝宝, 5:1-2岁宝宝, 6:2-3岁宝宝, 7:3-5岁宝宝, 8:5-10岁宝宝, 9:10-15岁宝宝',
                                        user_id BIGINT NOT NULL,
                                        title VARCHAR(255) NOT NULL,
                                        content TEXT NOT NULL
);
