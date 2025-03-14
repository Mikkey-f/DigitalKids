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


