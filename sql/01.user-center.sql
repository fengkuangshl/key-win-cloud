#导出 user-center 的数据库结构
CREATE DATABASE IF NOT EXISTS `user-center` DEFAULT CHARACTER SET = utf8mb4;
Use `user-center`;

#
# Structure for table "sys_menu"
#

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  id varchar(36) not null,
  create_date datetime,
  create_user_id varchar(255),
  create_user_name varchar(255),
  enable_flag bit not null,
  update_date datetime,
  update_user_id varchar(255),
  update_user_name varchar(255),
  version bigint,
  `parent_id` varchar(36) not null,
  `name` varchar(64) NOT NULL,
  `url` varchar(1024) DEFAULT NULL,
  `path` varchar(1024) DEFAULT NULL,
  `css` varchar(32) DEFAULT NULL,
  `sort` int(11) NOT NULL,
  `is_menu` int(11) DEFAULT NULL COMMENT '是否菜单 1 是 2 不是',
  `hidden` int(11) DEFAULT NULL COMMENT '是否隐藏,0 false 1 true',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8mb4;

#
# Data for table "sys_menu"
#



INSERT INTO `sys_menu` VALUES
('1', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'-1','后台管理','javascript:;','','layui-icon-set',2,1,0),
('2', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'1','用户管理','#!user','system/user.html','layui-icon-friends',2,1,0),
('3', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'1','角色管理','#!role','system/role.html','layui-icon-friends',3,1,0),
('4', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'1','菜单管理','#!menus','system/menus.html','layui-icon-menu-fill',4,1,0),
('5', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'1','权限管理','#!permissions','system/permissions.html','layui-icon-password',5,1,0),
('6', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'1','我的信息','#!myInfo','system/myInfo.html','',10,1,1),
('7', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'-1','认证中心','javascript:;','','layui-icon-set',1,1,0),
('8', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'7','应用管理','#!app','attestation/app.html','layui-icon-app',9,1,0),
('9', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'7','服务管理','#!services','attestation/services.html','layui-icon-website',8,1,0),
('10',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'7','令牌管理','#!token','attestation/token.html','layui-icon-util',11,1,0),
('11',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'-1','系统监控','javascript:;','','layui-icon-set',3,1,0),
('12',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'11','注册中心','#!register','http://127.0.0.1:1111/','layui-icon-engine',2,1,0),
('13',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'11','服务治理','#!eureka','eureka/list.html','layui-icon-engine',1,1,0),
('14',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'11','慢查询','#!sql','system/sql.html','layui-icon-util',11,1,0),
('15',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'11','文件中心','#!files','files/files.html','layui-icon-file',10,1,0),
('16',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'11','文档中心','#!swagger','http://127.0.0.1:9200/swagger-ui.html','layui-icon-app',9,1,0),
('17',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'11','代码生成器','#!generator','generator/list.html','layui-icon-app',999,1,0),
('18',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'11','日志中心','#!log','system/log.html','layui-icon-engine',18,1,0),
('19',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'11','prometheus监控','#!prometheus','http://127.0.0.1:9090','layui-icon-engine',1111,1,0),
('20',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'-1','任务中心','javascript:;','','layui-icon-set',4,1,0),
('21',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'20','任务管理','#!jobinfo','http://127.0.0.1:8888/jobinfo','layui-icon-senior',1,1,0),
('22',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'20','调度日志','#!joblog','http://127.0.0.1:8888/joblog','layui-icon-senior',2,1,0),
('23',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'20','执行器管理','#!jobgroup','http://127.0.0.1:8888/jobgroup','layui-icon-senior',3,1,0),
('24',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'-1','用户地图','#!map','baiduMap/userMap.html','layui-icon-engine',111111,1,0),
('25',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'-1','路由管理','#!route','route/route.html','layui-icon-engine',111111,1,0);


#
# Structure for table "sys_permission"
#

DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
   id varchar(36) not null,
  create_date datetime,
  create_user_id varchar(255),
  create_user_name varchar(255),
  enable_flag bit not null,
  update_date datetime,
  update_user_id varchar(255),
  update_user_name varchar(255),
  `permission` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `permission` (`permission`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4;

#
# Data for table "sys_permission"
#
INSERT INTO `sys_permission` VALUES ('1', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'permission:post/permissions', '保存权限标识');
INSERT INTO `sys_permission` VALUES ('2', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'permission:put/permissions', '修改权限标识');
INSERT INTO `sys_permission` VALUES ('3', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'permission:delete/permissions/{id}', '删除权限标识');
INSERT INTO `sys_permission` VALUES ('4', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'permission:get/permissions', '查询权限标识');
INSERT INTO `sys_permission` VALUES ('5', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'/permissions/{roleId}/permissions','查看角色权限');
INSERT INTO `sys_permission` VALUES ('6', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'/permissions/granted','角色分配权限');
INSERT INTO `sys_permission` VALUES ('7', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'role:post/roles', '添加角色');
INSERT INTO `sys_permission` VALUES ('8', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'role:put/roles', '修改角色');
INSERT INTO `sys_permission` VALUES ('9', current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'role:delete/roles/{id}', '删除角色');
INSERT INTO `sys_permission` VALUES ('10',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'role:post/roles/{id}/permissions', '给角色分配权限');
INSERT INTO `sys_permission` VALUES ('11',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'role:get/roles', '查询角色');
INSERT INTO `sys_permission` VALUES ('12',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'role:get/roles/{id}/permissions', '获取角色的权限');
INSERT INTO `sys_permission` VALUES ('13',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'user:post/users/{id}/roles', '给用户分配角色');
INSERT INTO `sys_permission` VALUES ('14',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'user:post/users/{id}/resetPassword', '用户重置密码');
INSERT INTO `sys_permission` VALUES ('15',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'user:get/users', '用户查询');
INSERT INTO `sys_permission` VALUES ('16',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'user:put/users/me', '修改用户');
INSERT INTO `sys_permission` VALUES ('17',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'user:get/users/{id}/roles', '获取用户的角色');
INSERT INTO `sys_permission` VALUES ('18',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'user:post/users/saveOrUpdate', '新增用户');
INSERT INTO `sys_permission` VALUES ('19',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'user:post/users/exportUser', '导出用户');
INSERT INTO `sys_permission` VALUES ('20',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'user:get/users/updateEnabled', '用户状态修改');
INSERT INTO `sys_permission` VALUES ('21',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'user:put/users/password', '修改密码');
INSERT INTO `sys_permission` VALUES ('22',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'menu:get/menus/all', '查询菜单');
INSERT INTO `sys_permission` VALUES ('23',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'menu:post/menus/granted', '给角色分配菜单');
INSERT INTO `sys_permission` VALUES ('24',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'menu:get/menus/tree', '树形显示');
INSERT INTO `sys_permission` VALUES ('25',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'menu:get/menus/{roleId}/menus', '获取角色的菜单');
INSERT INTO `sys_permission` VALUES ('26',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'menu:post/menus', '添加菜单');
INSERT INTO `sys_permission` VALUES ('27',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'menu:put/menus', '修改菜单');
INSERT INTO `sys_permission` VALUES ('28',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'menu:delete/menus/{id}', '删除菜单');
INSERT INTO `sys_permission` VALUES ('29',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'menu:get/menus/current', '当前用户菜单');
INSERT INTO `sys_permission` VALUES ('30',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'menu:get/menus/findAlls', '所有菜单');
INSERT INTO `sys_permission` VALUES ('31',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'client:post/clients', '保存应用');
INSERT INTO `sys_permission` VALUES ('32',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'client:get/clients', '应用列表');
INSERT INTO `sys_permission` VALUES ('33',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'client:get/clients/{id}', '根据id获取应用');
INSERT INTO `sys_permission` VALUES ('34',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'client:delete/clients', '删除应用');
INSERT INTO `sys_permission` VALUES ('35',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'service:get/service/findAlls', '查询所有服务');
INSERT INTO `sys_permission` VALUES ('36',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'service:get/service/findOnes', '服务树');
INSERT INTO `sys_permission` VALUES ('37',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'menu:get/menus/findOnes', '获取菜单以及顶层菜单');
INSERT INTO `sys_permission` VALUES ('38',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'permission:get/permissions/{roleId}/permissions', '根据roleId获取权限');
INSERT INTO `sys_permission` VALUES ('39',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'file:query', '获取文件列表');
INSERT INTO `sys_permission` VALUES ('40',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0, 'file:del', '删除文件');

#
# Structure for table "sys_role"
#

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
   id varchar(36) not null,
  create_date datetime,
  create_user_id varchar(255),
  create_user_name varchar(255),
  enable_flag bit not null,
  update_date datetime,
  update_user_id varchar(255),
  update_user_name varchar(255),
  `code` varchar(32) NOT NULL COMMENT '角色code',
  `name` varchar(50) NOT NULL COMMENT '角色名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

#
# Data for table "sys_role"
#

INSERT INTO `sys_role` VALUES
('1',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'ADMIN','管理员'),
('3',current_timestamp(),'-1','system',1,current_timestamp(),'-1','system',0,'002','普通用户');

#
# Structure for table "sys_role_menu"
#

DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
   id varchar(36) not null,
  create_date datetime,
  create_user_id varchar(255),
  create_user_name varchar(255),
  enable_flag bit not null,
  update_date datetime,
  update_user_id varchar(255),
  update_user_name varchar(255),
  `role_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "sys_role_menu"
#

INSERT INTO `sys_role_menu`  (`role_id`,`menu_id`)  VALUES
(1,1),(1,2),(1,3),(1,4),(1,5),
(1,6),(1,7),(1,8),(1,9),(1,10),
(1,11),(1,12),(1,13),(1,14),(1,15),
(1,16),(1,17),(1,18),(1,19),(1,20),
(1,21),(1,22),(1,23),(1,24),(1,25);

#
# Structure for table "sys_role_permission"
#

DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
   id varchar(36) not null,
  create_date datetime,
  create_user_id varchar(255),
  create_user_name varchar(255),
  enable_flag bit not null,
  update_date datetime,
  update_user_id varchar(255),
  update_user_name varchar(255),
  `role_id` int(11) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Data for table "sys_role_permission"
#

INSERT INTO `sys_role_permission` (`role_id`,`permission_id`) VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25),(1,26),(1,27),(1,29),(1,30),(1,31),(1,32),(1,33),(1,34),(1,35),(1,36),(1,37),(1,38),(1,39);



#
# Structure for table "sys_role_user"
#


DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
   id varchar(36) not null,
  create_date datetime,
  create_user_id varchar(255),
  create_user_name varchar(255),
  enable_flag bit not null,
  update_date datetime,
  update_user_id varchar(255),
  update_user_name varchar(255),
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`user_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;

#
# Data for table "sys_role_user"
#

INSERT INTO `sys_role_user` VALUES ('1',1277137734524300032,1),(2,1277120261867529984,1),(3,1277056689447719936,1),(4,1275397643669949952,1);


#
# Structure for table "sys_user"
#
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
   id varchar(36) not null,
  create_date datetime,
  create_user_id varchar(255),
  create_user_name varchar(255),
  enable_flag bit not null,
  update_date datetime,
  update_user_id varchar(255),
  update_user_name varchar(255),
  `username` varchar(50) NOT NULL,
  `password` varchar(60) NOT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `head_img_url` varchar(1024) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `sex` tinyint(1) DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `type` varchar(16) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;

#
# Data for table "sys_user"
#

INSERT INTO `sys_user` VALUES
('1277137734524300032','admin','$2a$10$Wtw81uu43fGKw9lkOr1RAOTNWxQIZBsB3YDwc/5yDnr/yeG5x92EG','管理员','http://payo7kq4i.bkt.clouddn.com/耳机.jpg','13106975707',1,1,'BACKEND','2017-11-17 16:56:59','2018-09-15 03:12:44'),
('1275397643669949952','test','$2a$10$RD18sHNphJMmcuLuUX/Np.IV/7Ngbjd3Jtj3maFLpwaA6KaHVqPtq','测试账户','http://payo7kq4i.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720180819191900.jpg','13851539156',0,0,'APP','2017-11-17 16:56:59','2018-09-07 03:27:40'),
('1277056689447719936','user','$2a$10$fL/AfD4RDS0LxLJS7zpaZ.YUMfjNWKVvUn7oiA75L1K6PXazSTJPi','体验用户','http://payo7kq4i.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720180819191900.jpg',NULL,1,0,'APP','2017-11-17 16:56:59','2018-09-07 13:38:34'),
('1277120261867529984','owen','$2a$10$4WkpmB1jHncBCrzJ7hJRq.SsiEFiyE/FdgPF26hLs8vzPyoNpZjta','欧文','http://payo7kq4i.bkt.clouddn.com/QQ%E5%9B%BE%E7%89%8720180819191900.jpg','18579068166',1,0,'APP','2017-11-17 16:56:59','2018-09-12 06:00:31');

