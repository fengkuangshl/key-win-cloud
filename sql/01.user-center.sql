/*
Navicat MySQL Data Transfer

Source Server         : localhost-3307-mysql-5.7.32
Source Server Version : 50732
Source Host           : localhost:3307
Source Database       : user-center-new

Target Server Type    : MYSQL
Target Server Version : 50732
File Encoding         : 65001

Date: 2021-12-04 21:40:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` varchar(36) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user_id` varchar(255) DEFAULT NULL,
  `create_user_name` varchar(255) DEFAULT NULL,
  `enable_flag` bit(1) NOT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_user_id` varchar(255) DEFAULT NULL,
  `update_user_name` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `parent_id` varchar(36) NOT NULL,
  `name` varchar(64) NOT NULL,
  `url` varchar(1024) DEFAULT NULL,
  `path` varchar(1024) DEFAULT NULL,
  `css` varchar(32) DEFAULT NULL,
  `sort` int(11) NOT NULL,
  `is_menu` int(11) DEFAULT NULL COMMENT '是否菜单 1 是 2 不是',
  `hidden` int(11) DEFAULT NULL COMMENT '是否隐藏,0 false 1 true',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '-1', '后台管理', 'javascript:;', '', 'layui-icon-set', '2', '1', '0');
INSERT INTO `sys_menu` VALUES ('10', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '7', '令牌管理', '#!token', 'attestation/token.html', 'layui-icon-util', '11', '1', '0');
INSERT INTO `sys_menu` VALUES ('11', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '-1', '系统监控', 'javascript:;', '', 'layui-icon-set', '3', '1', '0');
INSERT INTO `sys_menu` VALUES ('12', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '11', '注册中心', '#!register', 'http://127.0.0.1:1111/', 'layui-icon-engine', '2', '1', '0');
INSERT INTO `sys_menu` VALUES ('13', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '11', '服务治理', '#!eureka', 'eureka/list.html', 'layui-icon-engine', '1', '1', '0');
INSERT INTO `sys_menu` VALUES ('14', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '11', '慢查询', '#!sql', 'system/sql.html', 'layui-icon-util', '11', '1', '0');
INSERT INTO `sys_menu` VALUES ('1466247571289755650', '2021-12-02 11:27:17', '1277137734524300032', 'admin', '', '2021-12-02 11:27:17', null, null, null, '-1', '后台管理-vue', 'javascript:;', '', 'el-icon-setting', '-100', '1', '0');
INSERT INTO `sys_menu` VALUES ('1466247783630589954', '2021-12-02 11:28:47', '1277137734524300032', 'admin', '', '2021-12-02 11:28:47', '1277137734524300032', 'admin', null, '1466247571289755650', '菜单管理', '/sysmenu', 'system/menu/SysMenu', 'el-icon-user-solid', '1', '1', '0');
INSERT INTO `sys_menu` VALUES ('1466276460649078786', '2021-12-02 13:22:05', '1277137734524300032', 'admin', '', '2021-12-02 13:22:05', '1277137734524300032', 'admin', null, '1466247571289755650', '我的信息', '/myinfo', 'system/user/MyInfo', '', '99', '1', '1');
INSERT INTO `sys_menu` VALUES ('1466276709111259138', '2021-12-02 13:23:04', '1277137734524300032', 'admin', '', '2021-12-02 13:23:04', '1277137734524300032', 'admin', null, '1466247571289755650', '角色管理', '/sysrole', 'system/sys-role/SysRole', 'el-icon-user-solid', '2', '1', '0');
INSERT INTO `sys_menu` VALUES ('1466276902217015298', '2021-12-02 13:23:50', '1277137734524300032', 'admin', '', '2021-12-02 13:23:50', '1277137734524300032', 'admin', null, '1466247571289755650', '用户管理', '/user', 'system/user/SysUser', 'el-icon-user-solid', '1', '1', '0');
INSERT INTO `sys_menu` VALUES ('1466277044160651265', '2021-12-02 13:24:24', '1277137734524300032', 'admin', '', '2021-12-02 13:24:24', '1277137734524300032', 'admin', null, '1466247571289755650', '权限管理', '/permission', 'system/permission/SysPermission', 'el-icon-user-solid', '4', '1', '0');
INSERT INTO `sys_menu` VALUES ('1466277450039255041', '2021-12-02 13:26:01', '1277137734524300032', 'admin', '', '2021-12-02 13:26:01', '1277137734524300032', 'admin', null, '-1', 'test1', '', '', '', '0', '1', '0');
INSERT INTO `sys_menu` VALUES ('15', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '11', '文件中心', '#!files', 'files/files.html', 'layui-icon-file', '10', '1', '0');
INSERT INTO `sys_menu` VALUES ('16', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '11', '文档中心', '#!swagger', 'http://127.0.0.1:9200/swagger-ui.html', 'layui-icon-app', '9', '1', '0');
INSERT INTO `sys_menu` VALUES ('17', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '11', '代码生成器', '#!generator', 'generator/list.html', 'layui-icon-app', '999', '1', '0');
INSERT INTO `sys_menu` VALUES ('18', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '11', '日志中心', '#!log', 'system/log.html', 'layui-icon-engine', '18', '1', '0');
INSERT INTO `sys_menu` VALUES ('19', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '11', 'prometheus监控', '#!prometheus', 'http://127.0.0.1:9090', 'layui-icon-engine', '1111', '1', '0');
INSERT INTO `sys_menu` VALUES ('2', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '1', '用户管理', '#!user', 'system/user.html', 'layui-icon-friends', '2', '1', '0');
INSERT INTO `sys_menu` VALUES ('20', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '-1', '任务中心', 'javascript:;', '', 'layui-icon-set', '4', '1', '0');
INSERT INTO `sys_menu` VALUES ('21', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '20', '任务管理', '#!jobinfo', 'http://127.0.0.1:8888/jobinfo', 'layui-icon-senior', '1', '1', '0');
INSERT INTO `sys_menu` VALUES ('22', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '20', '调度日志', '#!joblog', 'http://127.0.0.1:8888/joblog', 'layui-icon-senior', '2', '1', '0');
INSERT INTO `sys_menu` VALUES ('23', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '20', '执行器管理', '#!jobgroup', 'http://127.0.0.1:8888/jobgroup', 'layui-icon-senior', '3', '1', '0');
INSERT INTO `sys_menu` VALUES ('24', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '-1', '用户地图', '#!map', 'baiduMap/userMap.html', 'layui-icon-engine', '111111', '1', '0');
INSERT INTO `sys_menu` VALUES ('25', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '-1', '路由管理', '#!route', 'route/route.html', 'layui-icon-engine', '111111', '1', '0');
INSERT INTO `sys_menu` VALUES ('3', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '1', '角色管理', '#!role', 'system/role.html', 'layui-icon-friends', '3', '1', '0');
INSERT INTO `sys_menu` VALUES ('4', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '1', '菜单管理', '#!menus', 'system/menus.html', 'layui-icon-menu-fill', '4', '1', '0');
INSERT INTO `sys_menu` VALUES ('5', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '1', '权限管理', '#!permissions', 'system/permissions.html', 'layui-icon-password', '5', '1', '0');
INSERT INTO `sys_menu` VALUES ('6', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '1', '我的信息', '#!myInfo', 'system/myInfo.html', '', '10', '1', '1');
INSERT INTO `sys_menu` VALUES ('7', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '-1', '认证中心', 'javascript:;', '', 'layui-icon-set', '1', '1', '0');
INSERT INTO `sys_menu` VALUES ('8', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '7', '应用管理', '#!app', 'attestation/app.html', 'layui-icon-app', '9', '1', '0');
INSERT INTO `sys_menu` VALUES ('9', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', '7', '服务管理', '#!services', 'attestation/services.html', 'layui-icon-website', '8', '1', '0');

-- ----------------------------
-- Table structure for `sys_permission`
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` varchar(36) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user_id` varchar(255) DEFAULT NULL,
  `create_user_name` varchar(255) DEFAULT NULL,
  `enable_flag` bit(1) NOT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_user_id` varchar(255) DEFAULT NULL,
  `update_user_name` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `permission` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `permission` (`permission`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('1', '2021-11-30 23:02:44', '-1', 'system', '', '2021-11-30 23:02:44', '-1', 'system', '0', 'permission:post/permissions', '保存权限标识');
INSERT INTO `sys_permission` VALUES ('10', '2021-11-30 23:02:45', '-1', 'system', '', '2021-11-30 23:02:45', '-1', 'system', '0', 'role:post/roles/{id}/permissions', '给角色分配权限');
INSERT INTO `sys_permission` VALUES ('11', '2021-11-30 23:02:45', '-1', 'system', '', '2021-11-30 23:02:45', '-1', 'system', '0', 'role:get/roles', '查询角色');
INSERT INTO `sys_permission` VALUES ('12', '2021-11-30 23:02:45', '-1', 'system', '', '2021-11-30 23:02:45', '-1', 'system', '0', 'role:get/roles/{id}/permissions', '获取角色的权限');
INSERT INTO `sys_permission` VALUES ('13', '2021-11-30 23:02:45', '-1', 'system', '', '2021-11-30 23:02:45', '-1', 'system', '0', 'user:post/users/{id}/roles', '给用户分配角色');
INSERT INTO `sys_permission` VALUES ('14', '2021-11-30 23:02:45', '-1', 'system', '', '2021-11-30 23:02:45', '-1', 'system', '0', 'user:post/users/{id}/resetPassword', '用户重置密码');
INSERT INTO `sys_permission` VALUES ('15', '2021-11-30 23:02:46', '-1', 'system', '', '2021-11-30 23:02:46', '-1', 'system', '0', 'user:get/users', '用户查询');
INSERT INTO `sys_permission` VALUES ('16', '2021-11-30 23:02:46', '-1', 'system', '', '2021-11-30 23:02:46', '-1', 'system', '0', 'user:put/users/me', '修改用户');
INSERT INTO `sys_permission` VALUES ('17', '2021-11-30 23:02:46', '-1', 'system', '', '2021-11-30 23:02:46', '-1', 'system', '0', 'user:get/users/{id}/roles', '获取用户的角色');
INSERT INTO `sys_permission` VALUES ('18', '2021-11-30 23:02:46', '-1', 'system', '', '2021-11-30 23:02:46', '-1', 'system', '0', 'user:post/users/saveOrUpdate', '新增用户');
INSERT INTO `sys_permission` VALUES ('19', '2021-11-30 23:02:46', '-1', 'system', '', '2021-11-30 23:02:46', '-1', 'system', '0', 'user:post/users/exportUser', '导出用户');
INSERT INTO `sys_permission` VALUES ('2', '2021-11-30 23:02:45', '-1', 'system', '', '2021-11-30 23:02:45', '-1', 'system', '0', 'permission:put/permissions', '修改权限标识');
INSERT INTO `sys_permission` VALUES ('20', '2021-11-30 23:02:46', '-1', 'system', '', '2021-11-30 23:02:46', '-1', 'system', '0', 'user:get/users/updateEnabled', '用户状态修改');
INSERT INTO `sys_permission` VALUES ('21', '2021-11-30 23:02:46', '-1', 'system', '', '2021-11-30 23:02:46', '-1', 'system', '0', 'user:put/users/password', '修改密码');
INSERT INTO `sys_permission` VALUES ('22', '2021-11-30 23:02:46', '-1', 'system', '', '2021-11-30 23:02:46', '-1', 'system', '0', 'menu:get/menus/all', '查询菜单');
INSERT INTO `sys_permission` VALUES ('23', '2021-11-30 23:02:46', '-1', 'system', '', '2021-11-30 23:02:46', '-1', 'system', '0', 'menu:post/menus/granted', '给角色分配菜单');
INSERT INTO `sys_permission` VALUES ('24', '2021-11-30 23:02:46', '-1', 'system', '', '2021-11-30 23:02:46', '-1', 'system', '0', 'menu:get/menus/tree', '树形显示');
INSERT INTO `sys_permission` VALUES ('25', '2021-11-30 23:02:46', '-1', 'system', '', '2021-11-30 23:02:46', '-1', 'system', '0', 'menu:get/menus/{roleId}/menus', '获取角色的菜单');
INSERT INTO `sys_permission` VALUES ('26', '2021-11-30 23:02:46', '-1', 'system', '', '2021-11-30 23:02:46', '-1', 'system', '0', 'menu:post/menus', '添加菜单');
INSERT INTO `sys_permission` VALUES ('27', '2021-11-30 23:02:47', '-1', 'system', '', '2021-11-30 23:02:47', '-1', 'system', '0', 'menu:put/menus', '修改菜单');
INSERT INTO `sys_permission` VALUES ('28', '2021-11-30 23:02:47', '-1', 'system', '', '2021-11-30 23:02:47', '-1', 'system', '0', 'menu:delete/menus/{id}', '删除菜单');
INSERT INTO `sys_permission` VALUES ('29', '2021-11-30 23:02:47', '-1', 'system', '', '2021-11-30 23:02:47', '-1', 'system', '0', 'menu:get/menus/current', '当前用户菜单');
INSERT INTO `sys_permission` VALUES ('3', '2021-11-30 23:02:45', '-1', 'system', '', '2021-11-30 23:02:45', '-1', 'system', '0', 'permission:delete/permissions/{id}', '删除权限标识');
INSERT INTO `sys_permission` VALUES ('30', '2021-11-30 23:02:47', '-1', 'system', '', '2021-11-30 23:02:47', '-1', 'system', '0', 'menu:get/menus/findAlls', '所有菜单');
INSERT INTO `sys_permission` VALUES ('31', '2021-11-30 23:02:47', '-1', 'system', '', '2021-11-30 23:02:47', '-1', 'system', '0', 'client:post/clients', '保存应用');
INSERT INTO `sys_permission` VALUES ('32', '2021-11-30 23:02:47', '-1', 'system', '', '2021-11-30 23:02:47', '-1', 'system', '0', 'client:get/clients', '应用列表');
INSERT INTO `sys_permission` VALUES ('33', '2021-11-30 23:02:47', '-1', 'system', '', '2021-11-30 23:02:47', '-1', 'system', '0', 'client:get/clients/{id}', '根据id获取应用');
INSERT INTO `sys_permission` VALUES ('34', '2021-11-30 23:02:47', '-1', 'system', '', '2021-11-30 23:02:47', '-1', 'system', '0', 'client:delete/clients', '删除应用');
INSERT INTO `sys_permission` VALUES ('35', '2021-11-30 23:02:47', '-1', 'system', '', '2021-11-30 23:02:47', '-1', 'system', '0', 'service:get/service/findAlls', '查询所有服务');
INSERT INTO `sys_permission` VALUES ('36', '2021-11-30 23:02:47', '-1', 'system', '', '2021-11-30 23:02:47', '-1', 'system', '0', 'service:get/service/findOnes', '服务树');
INSERT INTO `sys_permission` VALUES ('37', '2021-11-30 23:02:47', '-1', 'system', '', '2021-11-30 23:02:47', '-1', 'system', '0', 'menu:get/menus/findOnes', '获取菜单以及顶层菜单');
INSERT INTO `sys_permission` VALUES ('38', '2021-11-30 23:02:47', '-1', 'system', '', '2021-11-30 23:02:47', '-1', 'system', '0', 'permission:get/permissions/{roleId}/permissions', '根据roleId获取权限');
INSERT INTO `sys_permission` VALUES ('39', '2021-11-30 23:02:47', '-1', 'system', '', '2021-11-30 23:02:47', '-1', 'system', '0', 'file:query', '获取文件列表');
INSERT INTO `sys_permission` VALUES ('4', '2021-11-30 23:02:45', '-1', 'system', '', '2021-11-30 23:02:45', '-1', 'system', '0', 'permission:get/permissions', '查询权限标识');
INSERT INTO `sys_permission` VALUES ('40', '2021-11-30 23:02:48', '-1', 'system', '', '2021-11-30 23:02:48', '-1', 'system', '0', 'file:del', '删除文件');
INSERT INTO `sys_permission` VALUES ('5', '2021-11-30 23:02:45', '-1', 'system', '', '2021-11-30 23:02:45', '-1', 'system', '0', '/permissions/{roleId}/permissions', '查看角色权限');
INSERT INTO `sys_permission` VALUES ('6', '2021-11-30 23:02:45', '-1', 'system', '', '2021-11-30 23:02:45', '-1', 'system', '0', '/permissions/granted', '角色分配权限');
INSERT INTO `sys_permission` VALUES ('7', '2021-11-30 23:02:45', '-1', 'system', '', '2021-11-30 23:02:45', '-1', 'system', '0', 'role:post/roles', '添加角色');
INSERT INTO `sys_permission` VALUES ('8', '2021-11-30 23:02:45', '-1', 'system', '', '2021-11-30 23:02:45', '-1', 'system', '0', 'role:put/roles', '修改角色');
INSERT INTO `sys_permission` VALUES ('9', '2021-11-30 23:02:45', '-1', 'system', '', '2021-11-30 23:02:45', '-1', 'system', '0', 'role:delete/roles/{id}', '删除角色');

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(36) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user_id` varchar(255) DEFAULT NULL,
  `create_user_name` varchar(255) DEFAULT NULL,
  `enable_flag` bit(1) NOT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_user_id` varchar(255) DEFAULT NULL,
  `update_user_name` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `code` varchar(32) NOT NULL COMMENT '角色code',
  `name` varchar(50) NOT NULL COMMENT '角色名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '2021-11-30 23:02:48', '-1', 'system', '', '2021-11-30 23:02:48', '-1', 'system', '0', 'ADMIN', '管理员');
INSERT INTO `sys_role` VALUES ('1466421850944638978', '2021-12-02 22:59:49', '1277137734524300032', 'admin', '', '2021-12-02 22:59:49', null, null, null, 'test', 'test');
INSERT INTO `sys_role` VALUES ('3', '2021-11-30 23:02:48', '-1', 'system', '', '2021-11-30 23:02:48', '-1', 'system', '0', '002', '普通用户');

-- ----------------------------
-- Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` varchar(36) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user_id` varchar(255) DEFAULT NULL,
  `create_user_name` varchar(255) DEFAULT NULL,
  `enable_flag` bit(1) NOT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_user_id` varchar(255) DEFAULT NULL,
  `update_user_name` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `role_id` varchar(36) NOT NULL,
  `menu_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_id` (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('1466277133063118850', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '22');
INSERT INTO `sys_role_menu` VALUES ('1466277133063118851', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '23');
INSERT INTO `sys_role_menu` VALUES ('1466277133063118852', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '24');
INSERT INTO `sys_role_menu` VALUES ('1466277133063118853', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '25');
INSERT INTO `sys_role_menu` VALUES ('1466277133063118854', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '1466247571289755650');
INSERT INTO `sys_role_menu` VALUES ('1466277133063118855', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '1466276460649078786');
INSERT INTO `sys_role_menu` VALUES ('1466277133130227713', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '10');
INSERT INTO `sys_role_menu` VALUES ('1466277133142810626', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '11');
INSERT INTO `sys_role_menu` VALUES ('1466277133151199234', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '1466276709111259138');
INSERT INTO `sys_role_menu` VALUES ('1466277133155393538', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '12');
INSERT INTO `sys_role_menu` VALUES ('1466277133155393539', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '13');
INSERT INTO `sys_role_menu` VALUES ('1466277133155393540', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '14');
INSERT INTO `sys_role_menu` VALUES ('1466277133155393541', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '15');
INSERT INTO `sys_role_menu` VALUES ('1466277133197336578', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '1466247783630589954');
INSERT INTO `sys_role_menu` VALUES ('1466277133201530881', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '16');
INSERT INTO `sys_role_menu` VALUES ('1466277133201530882', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '17');
INSERT INTO `sys_role_menu` VALUES ('1466277133201530883', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '18');
INSERT INTO `sys_role_menu` VALUES ('1466277133201530884', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '19');
INSERT INTO `sys_role_menu` VALUES ('1466277133272834050', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '1466276902217015298');
INSERT INTO `sys_role_menu` VALUES ('1466277133277028354', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '1466277044160651265');
INSERT INTO `sys_role_menu` VALUES ('1466277133293805569', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '1');
INSERT INTO `sys_role_menu` VALUES ('1466277133293805570', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '2');
INSERT INTO `sys_role_menu` VALUES ('1466277133293805571', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '3');
INSERT INTO `sys_role_menu` VALUES ('1466277133327360002', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '4');
INSERT INTO `sys_role_menu` VALUES ('1466277133344137218', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '5');
INSERT INTO `sys_role_menu` VALUES ('1466277133348331521', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '6');
INSERT INTO `sys_role_menu` VALUES ('1466277133348331522', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '7');
INSERT INTO `sys_role_menu` VALUES ('1466277133348331523', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '8');
INSERT INTO `sys_role_menu` VALUES ('1466277133348331524', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '9');
INSERT INTO `sys_role_menu` VALUES ('1466277133390274561', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '20');
INSERT INTO `sys_role_menu` VALUES ('1466277133402857474', '2021-12-02 13:24:45', '1277137734524300032', 'admin', '', '2021-12-02 13:24:45', null, null, null, '1', '21');
INSERT INTO `sys_role_menu` VALUES ('1466417477644521473', '2021-12-02 22:42:26', '1277137734524300032', 'admin', '', '2021-12-02 22:42:26', null, null, null, '3', '1466247783630589954');

-- ----------------------------
-- Table structure for `sys_role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` varchar(36) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user_id` varchar(255) DEFAULT NULL,
  `create_user_name` varchar(255) DEFAULT NULL,
  `enable_flag` bit(1) NOT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_user_id` varchar(255) DEFAULT NULL,
  `update_user_name` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `role_id` varchar(36) NOT NULL,
  `permission_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_id` (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('1466425546231279618', '2021-12-02 23:14:30', '1277137734524300032', 'admin', '', '2021-12-02 23:14:30', null, null, null, '1466421850944638978', '9');
INSERT INTO `sys_role_permission` VALUES ('95a7f684-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '1');
INSERT INTO `sys_role_permission` VALUES ('95a7fa58-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '2');
INSERT INTO `sys_role_permission` VALUES ('95a7fbac-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '3');
INSERT INTO `sys_role_permission` VALUES ('95a7fc41-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '4');
INSERT INTO `sys_role_permission` VALUES ('95a7fcd2-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '5');
INSERT INTO `sys_role_permission` VALUES ('95a7fd5c-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '6');
INSERT INTO `sys_role_permission` VALUES ('95a7fde5-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '7');
INSERT INTO `sys_role_permission` VALUES ('95a7fe69-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '8');
INSERT INTO `sys_role_permission` VALUES ('95a7ff41-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '9');
INSERT INTO `sys_role_permission` VALUES ('95a7ffef-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '10');
INSERT INTO `sys_role_permission` VALUES ('95a800ab-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '11');
INSERT INTO `sys_role_permission` VALUES ('95a80143-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '12');
INSERT INTO `sys_role_permission` VALUES ('95a801dc-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '13');
INSERT INTO `sys_role_permission` VALUES ('95a80259-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '14');
INSERT INTO `sys_role_permission` VALUES ('95a80356-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '15');
INSERT INTO `sys_role_permission` VALUES ('95a8046d-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '16');
INSERT INTO `sys_role_permission` VALUES ('95a805fc-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '17');
INSERT INTO `sys_role_permission` VALUES ('95a8074b-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '18');
INSERT INTO `sys_role_permission` VALUES ('95a8085b-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '19');
INSERT INTO `sys_role_permission` VALUES ('95a809e7-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '20');
INSERT INTO `sys_role_permission` VALUES ('95a80b31-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '21');
INSERT INTO `sys_role_permission` VALUES ('95a80d93-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '22');
INSERT INTO `sys_role_permission` VALUES ('95a80ec3-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '23');
INSERT INTO `sys_role_permission` VALUES ('95a80fd4-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '24');
INSERT INTO `sys_role_permission` VALUES ('95a810f4-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '25');
INSERT INTO `sys_role_permission` VALUES ('95a81221-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '26');
INSERT INTO `sys_role_permission` VALUES ('95a81382-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '27');
INSERT INTO `sys_role_permission` VALUES ('95a814d8-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '29');
INSERT INTO `sys_role_permission` VALUES ('95a8160a-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '30');
INSERT INTO `sys_role_permission` VALUES ('95a8175b-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '31');
INSERT INTO `sys_role_permission` VALUES ('95a818eb-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '32');
INSERT INTO `sys_role_permission` VALUES ('95a81aa7-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '33');
INSERT INTO `sys_role_permission` VALUES ('95a81c15-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '34');
INSERT INTO `sys_role_permission` VALUES ('95a81d59-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '35');
INSERT INTO `sys_role_permission` VALUES ('95a81e77-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '36');
INSERT INTO `sys_role_permission` VALUES ('95a81f81-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '37');
INSERT INTO `sys_role_permission` VALUES ('95a82102-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '38');
INSERT INTO `sys_role_permission` VALUES ('95a8227b-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:49', '-1', 'system', '', '2021-11-30 23:02:49', '-1', 'system', '0', '1', '39');

-- ----------------------------
-- Table structure for `sys_role_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `id` varchar(36) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user_id` varchar(255) DEFAULT NULL,
  `create_user_name` varchar(255) DEFAULT NULL,
  `enable_flag` bit(1) NOT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_user_id` varchar(255) DEFAULT NULL,
  `update_user_name` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `user_id` varchar(36) NOT NULL,
  `role_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES ('1466578931811164162', '2021-12-03 09:24:00', '1277137734524300032', 'admin', '', '2021-12-03 09:24:00', null, null, null, '1466571619876757505', '3');
INSERT INTO `sys_role_user` VALUES ('95f91c02-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:50', '-1', 'system', '', '2021-11-30 23:02:50', '-1', 'system', '0', '1277137734524300032', '1');
INSERT INTO `sys_role_user` VALUES ('95f91e8a-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:50', '-1', 'system', '', '2021-11-30 23:02:50', '-1', 'system', '0', '1277120261867529984', '1');
INSERT INTO `sys_role_user` VALUES ('95f91f7b-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:50', '-1', 'system', '', '2021-11-30 23:02:50', '-1', 'system', '0', '1277056689447719936', '1');
INSERT INTO `sys_role_user` VALUES ('95f91ff4-51ee-11ec-ad6e-00155def428f', '2021-11-30 23:02:50', '-1', 'system', '', '2021-11-30 23:02:50', '-1', 'system', '0', '1275397643669949952', '1');

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(36) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user_id` varchar(255) DEFAULT NULL,
  `create_user_name` varchar(255) DEFAULT NULL,
  `enable_flag` bit(1) NOT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_user_id` varchar(255) DEFAULT NULL,
  `update_user_name` varchar(255) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(60) NOT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `head_img_url` varchar(1024) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `sex` tinyint(1) DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `type` varchar(16) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1275397643669949952', '2021-11-30 23:02:50', '-1', 'system', '', '2021-11-30 23:02:50', '-1', 'system', '0', 'test', '$2a$10$RD18sHNphJMmcuLuUX/Np.IV/7Ngbjd3Jtj3maFLpwaA6KaHVqPtq', '测试账户', 'http://payo7kq4i.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720180819191900.jpg', '13851539156', '0', '0', 'APP');
INSERT INTO `sys_user` VALUES ('1277056689447719936', '2021-11-30 23:02:50', '-1', 'system', '', '2021-11-30 23:02:50', '-1', 'system', '0', 'user', '$2a$10$fL/AfD4RDS0LxLJS7zpaZ.YUMfjNWKVvUn7oiA75L1K6PXazSTJPi', '体验用户', 'http://payo7kq4i.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720180819191900.jpg', null, '1', '0', 'APP');
INSERT INTO `sys_user` VALUES ('1277120261867529984', '2021-11-30 23:02:50', '-1', 'system', '', '2021-11-30 23:02:50', '-1', 'system', '0', 'owen', '$2a$10$4WkpmB1jHncBCrzJ7hJRq.SsiEFiyE/FdgPF26hLs8vzPyoNpZjta', '欧文', 'http://payo7kq4i.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720180819191900.jpg', '18579068166', '1', '0', 'APP');
INSERT INTO `sys_user` VALUES ('1277137734524300032', '2021-11-30 23:02:50', '-1', 'system', '', '2021-11-30 23:02:50', '-1', 'system', '0', 'admin', '$2a$10$Wtw81uu43fGKw9lkOr1RAOTNWxQIZBsB3YDwc/5yDnr/yeG5x92EG', '管理员', 'http://payo7kq4i.bkt.clouddn.com/耳机.jpg', '13106975707', '1', '1', 'BACKEND');
INSERT INTO `sys_user` VALUES ('1466571619876757505', '2021-12-03 08:54:56', '1277137734524300032', 'admin', '', '2021-12-03 08:54:56', null, null, null, 'test1', '$2a$10$Vur9EXRD2Sn1TChtdY/tq.LOwxjCWQQKc/FgZfN4BAhQKwp3Z3zIm', 'test1', null, '13524555395', '0', '1', 'BACKEND');
