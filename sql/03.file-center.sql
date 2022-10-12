/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1-3307
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : 127.0.0.1:3307
 Source Schema         : file-center

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 12/10/2022 18:04:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_chunk_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_chunk_file`;
CREATE TABLE `sys_chunk_file`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT ''id自增'',
  `create_date` datetime NULL DEFAULT NULL COMMENT ''创建时间'',
  `create_user_id` int(11) NOT NULL COMMENT ''创建者Id'',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''创建者名称'',
  `enable_flag` bit(1) NOT NULL COMMENT ''是否删除1:正常，0：删除'',
  `update_date` datetime NULL DEFAULT NULL COMMENT ''更新时间'',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT ''更新者Id'',
  `update_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''更新者名称'',
  `version` int(3) NOT NULL COMMENT ''版本号'',
  `chunk_number` int(3) NOT NULL COMMENT ''当前文件块，从1开始'',
  `chunk_size` int(11) NOT NULL COMMENT ''分块大小'',
  `current_chunk_size` int(11) NOT NULL COMMENT ''当前分块大小'',
  `total_Size` int(18) NOT NULL COMMENT ''总大小'',
  `total_chunks` int(3) NOT NULL COMMENT ''总块数'',
  `identifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ''文件标识'',
  `chunk_File_Name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ''文件名'',
  `biz_Type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ''业务类型'',
  `physical_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ''物理路径'',
  `relative_Path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ''相对路径'',
  `file_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4197 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_chunk_file
-- ----------------------------
INSERT INTO `sys_chunk_file` VALUES (4195, ''2022-10-12 17:37:42'', 19, ''admin'', b''0'', ''2022-10-12 17:37:42'', NULL, NULL, 0, 1, 2048000, 11280, 11280, 1, ''f23aa07a067b981642f477440a568aac'', ''年报模板 (3).xlsx-1'', ''default'', ''\\individual-soldier-auth\\default\\2022\\10\\12\\chunk\\f23aa07a067b981642f477440a568aac\\'', ''\\default\\2022\\10\\12\\chunk\\f23aa07a067b981642f477440a568aac\\年报模板 (3).xlsx-1'', ''application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'');
INSERT INTO `sys_chunk_file` VALUES (4196, ''2022-10-12 17:37:51'', 19, ''admin'', b''0'', ''2022-10-12 17:37:51'', NULL, NULL, 0, 1, 2048000, 11430, 11430, 1, ''87d28d0635aa1198127926c33be4c4ea'', ''月报导出 (4).xlsx-1'', ''default'', ''\\individual-soldier-auth\\default\\2022\\10\\12\\chunk\\87d28d0635aa1198127926c33be4c4ea\\'', ''\\default\\2022\\10\\12\\chunk\\87d28d0635aa1198127926c33be4c4ea\\月报导出 (4).xlsx-1'', ''application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'');

-- ----------------------------
-- Table structure for sys_file_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_info`;
CREATE TABLE `sys_file_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT ''id自增'',
  `create_date` datetime NULL DEFAULT NULL COMMENT ''创建时间'',
  `create_user_id` int(11) NOT NULL COMMENT ''创建者Id'',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''创建者名称'',
  `enable_flag` bit(1) NOT NULL COMMENT ''是否删除1:正常，0：删除'',
  `update_date` datetime NULL DEFAULT NULL COMMENT ''更新时间'',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT ''更新者Id'',
  `update_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''更新者名称'',
  `version` int(3) NOT NULL COMMENT ''版本号'',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content_Type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `biz_Type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `size` int(11) NULL DEFAULT NULL,
  `physical_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 133 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_file_info
-- ----------------------------
INSERT INTO `sys_file_info` VALUES (129, ''2022-10-12 17:30:32'', 19, ''admin'', b''1'', ''2022-10-12 17:30:32'', NULL, NULL, 0, ''redis-desktop-manager.rar'', ''6ced2be452f151dacf8f8acae7e2473c'', ''application/octet-stream'', ''default'', 39345214, ''\\individual-soldier-auth\\default\\2022\\10\\12\\1580128777216028673.rar'', ''\\default\\2022\\10\\12\\1580128777216028673.rar'');
INSERT INTO `sys_file_info` VALUES (130, ''2022-10-12 17:30:53'', 19, ''admin'', b''1'', ''2022-10-12 17:30:53'', NULL, NULL, 0, ''nacos_config_export_20220928175752.zip'', ''6c12b501fe43267ccd374e51aebcb860'', ''application/x-zip-compressed'', ''default'', 47192, ''\\individual-soldier-auth\\default\\2022\\10\\12\\1580128864008761346.zip'', ''\\default\\2022\\10\\12\\1580128864008761346.zip'');
INSERT INTO `sys_file_info` VALUES (131, ''2022-10-12 17:37:42'', 19, ''admin'', b''1'', ''2022-10-12 17:37:42'', NULL, NULL, 0, ''年报模板 (3).xlsx'', ''f23aa07a067b981642f477440a568aac'', ''application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'', ''default'', 11280, ''\\individual-soldier-auth\\default\\2022\\10\\12\\年报模板 (3).xlsx'', ''\\default\\2022\\10\\12\\年报模板 (3).xlsx'');
INSERT INTO `sys_file_info` VALUES (132, ''2022-10-12 17:37:51'', 19, ''admin'', b''1'', ''2022-10-12 17:37:51'', NULL, NULL, 0, ''月报导出 (4).xlsx'', ''87d28d0635aa1198127926c33be4c4ea'', ''application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'', ''default'', 11430, ''\\individual-soldier-auth\\default\\2022\\10\\12\\月报导出 (4).xlsx'', ''\\default\\2022\\10\\12\\月报导出 (4).xlsx'');

SET FOREIGN_KEY_CHECKS = 1;
