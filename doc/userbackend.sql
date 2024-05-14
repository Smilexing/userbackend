
create database userbackend;
use userbackend;

-- 用户信息
create table if not exists userbackend.`User`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `userAccount` varchar(256) not null comment '用户主账号',
    `username` varchar(32) null comment '用户名',
    `create_time` timestamp  null comment '创建时间',
    `update_time` timestamp null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_deleted` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)',
    `password` varchar(128) not null comment '密码',
    `iphone` varchar(256) null comment '手机号',
    `gener` tinyint null comment '性别 0-男 1-女',
    `email` varchar(256) null comment '邮箱',
    `userStatus` int default 0 not null comment '用户状态 0-正常 ',
    `userRole` int null comment '用户权限 0-普通用户 1-管理员',
    `avatarUrl` varchar(256) null comment '用户头像',
    `planetCode` varchar(128) null comment '星球编号'
) comment '用户信息';