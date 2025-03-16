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
CREATE TABLE user (
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



