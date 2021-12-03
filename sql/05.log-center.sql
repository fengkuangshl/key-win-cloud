###导出 sms-center 的数据库结构
CREATE DATABASE IF NOT EXISTS `log-center-new` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `log-center-new`;
SET FOREIGN_KEY_CHECKS=0;
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  id varchar(36) not null,
  create_date datetime,
  create_user_id varchar(255),
  create_user_name varchar(255),
  enable_flag bit not null,
  update_date datetime,
  update_user_id varchar(255),
  update_user_name varchar(255),
  version bigint,
  `username` varchar(50) COMMENT '用户名',
  `module` varchar(3000)  COMMENT '模块名',
  `params` text COMMENT '方法参数',
  `remark` text COMMENT '备注',
  `flag` tinyint(1) NOT NULL ,
  PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;