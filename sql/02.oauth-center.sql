/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1-3307
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : 127.0.0.1:3307
 Source Schema         : oauth-center-new

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 13/10/2022 18:12:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
  `id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `create_date` datetime NULL DEFAULT NULL,
  `create_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `enable_flag` bit(1) NOT NULL,
  `update_date` datetime NULL DEFAULT NULL,
  `update_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `version` bigint(20) NULL DEFAULT NULL,
  `client_id` varchar(48) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '应用标识',
  `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源限定串(逗号分割)',
  `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用密钥(bcyt) 加密',
  `client_secret_str` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用密钥(明文)',
  `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '范围',
  `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '5种oauth授权方式(authorization_code,password,refresh_token,client_credentials)',
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回调地址 ',
  `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限',
  `access_token_validity` int(11) NULL DEFAULT NULL COMMENT 'access_token有效期',
  `refresh_token_validity` int(11) NULL DEFAULT NULL COMMENT 'refresh_token有效期',
  `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '{}' COMMENT '{}',
  `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否自动授权 是-true',
  `status` int(1) NULL DEFAULT NULL,
  `if_limit` int(11) NOT NULL DEFAULT 0 COMMENT '是否应用限流',
  `limit_count` int(11) NOT NULL DEFAULT 10000 COMMENT '限流阈值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('1', '2021-12-02 08:15:34', '-1', 'system', b'1', '2021-12-02 08:15:34', '-1', 'system', 0, 'app', NULL, '$2a$10$i3F515wEDiB4Gvj9ym9Prui0dasRttEUQ9ink4Wpgb4zEDCAlV8zO', 'app', 'app', 'password,refresh_token', NULL, NULL, 180000, NULL, '{}', 'true', 1, 0, 10000);
INSERT INTO `oauth_client_details` VALUES ('10', '2021-12-02 08:15:34', '-1', 'system', b'1', '2021-12-02 08:15:34', '-1', 'system', 0, 'testtwo', '', '$2a$10$49ESIYmzu1n.cGzwMLRgleQMk0.kBTMOYnW4WUBDKwu9V23qOBovG', 'testtwo', 'all', 'authorization_code,password,refresh_token,client_credentials', 'locahost:9090/test', '', 18000, 18000, '{}', 'true', 1, 0, 10000);
INSERT INTO `oauth_client_details` VALUES ('11', '2021-12-02 08:15:34', '-1', 'system', b'1', '2021-12-02 08:15:34', '-1', 'system', 0, 'uc-app', '', '$2a$10$8UxEUaT2D2vSTJvTA/7YbODgCbK44bozsNA1kvMFSz8R153Xat7UO', 'uc-app', 'all', 'authorization_code,password,refresh_token,client_credentials', 'www.baidu.com', '', 18000, 18000, '{}', 'true', 1, 0, 10000);
INSERT INTO `oauth_client_details` VALUES ('12', '2021-12-02 08:15:34', '-1', 'system', b'1', '2021-12-02 08:15:34', '-1', 'system', 0, 'testtwo4', '', '$2a$10$Q8Qg5RQv1t0NFyL8Epfnj.wB/5NQnNJRMv5yIOVyeZ3ACXvzGwloq', 'testtwo4', 'all', 'implicit,password,refresh_token', 'locahost:9090/test', '', 18000, 18000, '{}', 'true', 1, 1, 10000);
INSERT INTO `oauth_client_details` VALUES ('2', '2021-12-02 08:15:34', '-1', 'system', b'1', '2021-12-02 08:15:34', '-1', 'system', 0, 'mobile', 'mobile,test', '$2a$10$ULxRssv/4NWOc388lZFbyus3IFfsbcpG/BZOq4TRxDhsx5HHIR7Jm', 'mobile', 'all', 'password,refresh_token', NULL, NULL, 180000, NULL, '{}', 'true', 1, 0, 10000);
INSERT INTO `oauth_client_details` VALUES ('4', '2021-12-02 08:15:34', '-1', 'system', b'1', '2021-12-02 08:15:34', '-1', 'system', 0, 'webApp', NULL, '$2a$10$06msMGYRH8nrm4iVnKFNKOoddB8wOwymVhbUzw/d3ZixD7Nq8ot72', 'webApp', 'app', 'authorization_code,password,refresh_token,client_credentials', NULL, NULL, 180000, NULL, '{}', 'true', 1, 0, 10000);
INSERT INTO `oauth_client_details` VALUES ('5', '2021-12-02 08:15:34', '-1', 'system', b'1', '2021-12-02 08:15:34', '-1', 'system', 0, 'beck', '', '$2a$10$56LGyH.2wOFNNp3ScUkspOMdyRnenYhnWEnfI0itIFfsXsd5ZhKh.', 'beck', 'all', 'authorization_code,password,refresh_token,client_credentials', 'http://www.baidu.com', '', 180000, NULL, '{}', 'true', 1, 0, 10000);
INSERT INTO `oauth_client_details` VALUES ('6', '2021-12-02 08:15:34', '-1', 'system', b'1', '2021-12-02 08:15:34', '-1', 'system', 0, 'owen', NULL, '$2a$10$a1ZEXiZQr604LN.wVxet.etPm6RvDs.HIaXP48J2HKRaEnZORTVwe', 'owen', 'app', 'authorization_code,password,refresh_token,client_credentials', 'http://127.0.0.1:9997/dashboard/login', NULL, 180000, NULL, '{}', 'true', 1, 0, 10000);
INSERT INTO `oauth_client_details` VALUES ('8', '2021-12-02 08:15:34', '-1', 'system', b'1', '2021-12-02 08:15:34', '-1', 'system', 0, 'testOne', '', '$2a$10$nI9kx19HHJTkJq0kMRPZ6uu/4uW7J9kPIpZ8YjFmbUlvwJmorc5Qa', 'testOne', 'all', 'authorization_code,password,refresh_token,client_credentials', 'http://bai.com', '', 18000, 18000, '{}', 'true', 0, 0, 10000);
INSERT INTO `oauth_client_details` VALUES ('9', '2021-12-02 08:15:34', '-1', 'system', b'1', '2021-12-02 08:15:34', '-1', 'system', 0, 'gwapi', '', '$2a$10$l7plpxQk42cuKbB8tbNe8eAA6v2xA6xkPXsjGEezago239102LRL2', 'gwapi', 'all', 'authorization_code,password,refresh_token,client_credentials', 'https://www.baidu.co', '', 18000, 18000, '{}', 'true', 1, 0, 10000);

-- ----------------------------
-- Table structure for sys_client_service
-- ----------------------------
DROP TABLE IF EXISTS `sys_client_service`;
CREATE TABLE `sys_client_service`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `create_date` datetime NULL DEFAULT NULL,
  `create_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `enable_flag` bit(1) NOT NULL,
  `update_date` datetime NULL DEFAULT NULL,
  `update_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `version` bigint(20) NULL DEFAULT NULL,
  `client_id` int(11) NOT NULL COMMENT '应用主键ID',
  `service_id` int(11) NOT NULL COMMENT '服务主键ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `client_id`(`client_id`, `service_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_client_service
-- ----------------------------
INSERT INTO `sys_client_service` VALUES ('1', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 1);
INSERT INTO `sys_client_service` VALUES ('10', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 10);
INSERT INTO `sys_client_service` VALUES ('11', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 11);
INSERT INTO `sys_client_service` VALUES ('12', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 12);
INSERT INTO `sys_client_service` VALUES ('13', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 13);
INSERT INTO `sys_client_service` VALUES ('14', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 14);
INSERT INTO `sys_client_service` VALUES ('15', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 15);
INSERT INTO `sys_client_service` VALUES ('16', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 16);
INSERT INTO `sys_client_service` VALUES ('17', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 17);
INSERT INTO `sys_client_service` VALUES ('18', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 18);
INSERT INTO `sys_client_service` VALUES ('2', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 2);
INSERT INTO `sys_client_service` VALUES ('3', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 3);
INSERT INTO `sys_client_service` VALUES ('4', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 4);
INSERT INTO `sys_client_service` VALUES ('5', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 5);
INSERT INTO `sys_client_service` VALUES ('6', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 6);
INSERT INTO `sys_client_service` VALUES ('7', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 7);
INSERT INTO `sys_client_service` VALUES ('8', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 8);
INSERT INTO `sys_client_service` VALUES ('9', '2021-12-02 08:15:35', '-1', 'system', b'1', '2021-12-02 08:15:35', '-1', 'system', 0, 4, 9);

-- ----------------------------
-- Table structure for sys_gateway_routes
-- ----------------------------
DROP TABLE IF EXISTS `sys_gateway_routes`;
CREATE TABLE `sys_gateway_routes`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `create_date` datetime NULL DEFAULT NULL,
  `create_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `enable_flag` bit(1) NOT NULL,
  `update_date` datetime NULL DEFAULT NULL,
  `update_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `version` bigint(20) NULL DEFAULT NULL,
  `uri` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'uri路径',
  `predicates` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '判定器',
  `filters` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '过滤器',
  `order` int(11) NULL DEFAULT NULL COMMENT '排序',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `delFlag` int(11) NULL DEFAULT 0 COMMENT '删除标志 0 不删除 1 删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '服务网关路由表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_gateway_routes
-- ----------------------------

-- ----------------------------
-- Table structure for sys_service
-- ----------------------------
DROP TABLE IF EXISTS `sys_service`;
CREATE TABLE `sys_service`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `create_date` datetime NULL DEFAULT NULL,
  `create_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `enable_flag` bit(1) NOT NULL,
  `update_date` datetime NULL DEFAULT NULL,
  `update_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `version` bigint(20) NULL DEFAULT NULL,
  `parent_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `path` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sort` int(11) NOT NULL,
  `is_service` int(11) NULL DEFAULT NULL COMMENT '是否服务 1 是 2 不是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_service
-- ----------------------------
INSERT INTO `sys_service` VALUES ('1', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '-1', '认证中心', '/api-auth', 1, 1);
INSERT INTO `sys_service` VALUES ('10', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '1', '服务管理', '/api-auth/services**/**', 4, 1);
INSERT INTO `sys_service` VALUES ('11', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '2', '用户管理', '/api-user/users**/**', 1, 1);
INSERT INTO `sys_service` VALUES ('12', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '2', '角色管理', '/api-user/roles**/**', 2, 1);
INSERT INTO `sys_service` VALUES ('13', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '2', '菜单管理', '/api-user/menus**/**', 3, 1);
INSERT INTO `sys_service` VALUES ('14', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '2', '权限管理', '/api-user/permissions**/**', 4, 1);
INSERT INTO `sys_service` VALUES ('15', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '3', '文件管理', '/api-file/files**/**', 1, 1);
INSERT INTO `sys_service` VALUES ('16', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '4', '短信管理', '/api/sms/sms**/**', 1, 1);
INSERT INTO `sys_service` VALUES ('17', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '5', '日志管理', '/api-log/sysLog**/**', 1, 1);
INSERT INTO `sys_service` VALUES ('18', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '6', '服务治理', '/api-eureka/eureka**/**', 1, 1);
INSERT INTO `sys_service` VALUES ('2', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '-1', '用户中心', '/api-user', 2, 1);
INSERT INTO `sys_service` VALUES ('3', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '-1', '文件中心', '/api-file', 3, 1);
INSERT INTO `sys_service` VALUES ('4', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '-1', '短信中心', '/api/sms', 4, 1);
INSERT INTO `sys_service` VALUES ('5', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '-1', '日志中心', '/api-log', 5, 1);
INSERT INTO `sys_service` VALUES ('6', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '-1', '注册中心', '/api-eureka', 6, 1);
INSERT INTO `sys_service` VALUES ('7', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '1', '应用管理', '/api-user/client**/**', 1, 1);
INSERT INTO `sys_service` VALUES ('8', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '1', '认证管理', '/api-auth/oauth**/**', 2, 1);
INSERT INTO `sys_service` VALUES ('9', '2021-12-02 08:15:36', '-1', 'system', b'1', '2021-12-02 08:15:36', '-1', 'system', 0, '1', 'redis监控', '/api-auth/redis**/**', 3, 1);

SET FOREIGN_KEY_CHECKS = 1;
