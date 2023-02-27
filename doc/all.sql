drop table if exists test;
create table test (
    id bigint NOT NULL comment 'id',
    name varchar(50) comment 'name',
    password varchar(50) comment 'password',
    primary key (id)
)engine=innodb default charset=utf8mb4 comment 'for testing';

insert into test (id, name, password) values (1, 'test', 'password');


drop table if exists demo;
create table demo (
                      id bigint NOT NULL comment 'id',
                      name varchar(50) comment 'name',

                      primary key (id)
)engine=innodb default charset=utf8mb4 comment 'for testing';

insert into demo (id, name) values (1, 'test');



-- ----------------------------
-- ebook table structure
-- ----------------------------

DROP TABLE IF EXISTS `ebook`;
CREATE TABLE `ebook`  (
                          `id` bigint NOT NULL COMMENT '主键ID',
                          `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '专栏名',
                          `category1_id` bigint NULL DEFAULT NULL COMMENT '分类1',
                          `category2_id` bigint NULL DEFAULT NULL COMMENT '分类2',
                          `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
                          `cover` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
                          `doc_count` int NULL DEFAULT 0 COMMENT '文章数',
                          `view_count` int NULL DEFAULT 0 COMMENT '阅读数',
                          `vote_count` int NULL DEFAULT 0 COMMENT '点赞数',
                          `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除',
                          `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '专栏' ROW_FORMAT = DYNAMIC;

insert into ebook (id, name,  description ) values (1,'Spring boot教程','自动配置简单得很');
insert into ebook (id, name,  description ) values (2,'vue 入门教程','企业级应用首先框架');
insert into ebook (id, name,  description ) values (3,'python 教程','零基础开始学')