/*
Navicat MySQL Data Transfer

Source Server         : localhost-3306-mysql-5.5.27
Source Server Version : 50527
Source Host           : 127.0.0.1:3306
Source Database       : admin_center_real

Target Server Type    : MYSQL
Target Server Version : 50527
File Encoding         : 65001

Date: 2021-06-22 09:21:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `data_log`
-- ----------------------------
DROP TABLE IF EXISTS `data_log`;
CREATE TABLE `data_log` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `version` int(11) DEFAULT NULL,
  `crt_dttm` datetime DEFAULT NULL,
  `crt_user` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `crt_user_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `enable_flag` bit(1) DEFAULT NULL,
  `last_update_dttm` datetime DEFAULT NULL,
  `last_update_user` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `content` varchar(8000) COLLATE utf8_bin DEFAULT NULL,
  `fk_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of data_log
-- ----------------------------
