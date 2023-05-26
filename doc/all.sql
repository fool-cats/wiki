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
insert into ebook (id, name,  description ) values (3,'python 教程','零基础开始学');



DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
                             `id` bigint NOT NULL COMMENT 'id',
                             `parent` bigint NOT NULL DEFAULT 0 COMMENT '父id',
                             `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                             `sort` int NULL DEFAULT NULL COMMENT '顺序',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分类' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (100, 000, '前端开发', 100);
INSERT INTO `category` VALUES (101, 100, 'Vue', 101);
INSERT INTO `category` VALUES (102, 100, 'HTML&CSS', 102);
INSERT INTO `category` VALUES (200, 000, '后端开发', 200);
INSERT INTO `category` VALUES (201, 200, '基础应用', 201);
INSERT INTO `category` VALUES (202, 200, '框架应用', 202);
INSERT INTO `category` VALUES (300, 000, '数据库', 300);
INSERT INTO `category` VALUES (301, 300, 'Mysql', 301);
INSERT INTO `category` VALUES (302, 300, 'Mybatis', 302);


-- ----------------------------
-- Table structure for doc
-- ----------------------------
DROP TABLE IF EXISTS `doc`;
CREATE TABLE `doc`  (
                        `id` bigint NOT NULL COMMENT '主键ID',
                        `ebook_id` bigint NOT NULL DEFAULT 0 COMMENT '专栏id',
                        `parent` bigint NOT NULL DEFAULT 0 COMMENT '父id',
                        `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
                        `sort` int NULL DEFAULT NULL COMMENT '顺序',
                        `view_count` int NULL DEFAULT 0 COMMENT '阅读数',
                        `vote_count` int NULL DEFAULT 0 COMMENT '点赞数',
                        `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除',
                        `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                        `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of doc
-- ----------------------------
INSERT INTO `doc` VALUES (1, 1, 0, '文章1', 1, 35, 9, 0, '2021-09-15 21:01:21', '2021-10-07 18:14:57');
INSERT INTO `doc` VALUES (2, 1, 1, '文章1.1', 1, 18, 2, 0, '2021-09-15 21:01:21', '2021-09-23 15:05:43');
INSERT INTO `doc` VALUES (3, 1, 0, '文章2', 2, 10, 1, 0, '2021-09-15 21:01:21', '2021-09-15 21:01:21');
INSERT INTO `doc` VALUES (4, 1, 3, '文章2.1', 2, 15, 2, 0, '2021-09-15 21:01:21', '2021-09-15 21:01:21');
INSERT INTO `doc` VALUES (5, 1, 1437317930831314945, '文章3.1', 2, 21, 1, 0, '2021-09-15 21:01:21', '2021-09-15 21:01:21');
INSERT INTO `doc` VALUES (6, 1, 5, '文章3.1.1', 1, 19, 1, 0, '2021-09-15 21:01:21', '2021-09-15 21:01:21');
INSERT INTO `doc` VALUES (1437317930831314945, 1, 0, '文章3', 3, 8, 1, 0, '2021-09-15 21:01:21', '2021-09-15 21:01:21');
INSERT INTO `doc` VALUES (1438000187241934850, 2, 0, 'Ant Design of Vue', 1, 90, 6, 0, '2021-09-15 21:01:21', '2021-09-15 21:01:21');
INSERT INTO `doc` VALUES (1438000644823724034, 2, 1438000187241934850, '关于ant-design-vue', 3, 50, 4, 0, '2021-09-15 21:01:21', '2021-10-06 00:25:00');