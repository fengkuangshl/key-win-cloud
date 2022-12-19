-- ----------------------------
-- 动态表单数据存储
-- ----------------------------
DROP TABLE IF EXISTS `act_form_data`;
create table act_form_data (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(11) NOT NULL COMMENT '创建者Id',
  `create_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者名称',
  `enable_flag` bit(1) NOT NULL COMMENT '是否删除1:正常，0：删除',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_user_id` int(11) NULL DEFAULT NULL COMMENT '更新者Id',
  `update_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者名称',
  `version` int(3) NOT NULL COMMENT '版本号',
    PROC_DEF_ID_ varchar(64) DEFAULT NULL,
    PROC_INST_ID_ varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
    FORM_KEY_ varchar(255) DEFAULT NULL,
    Control_ID_ varchar(100) DEFAULT NULL,
    Control_VALUE_ varchar(2000) DEFAULT NULL,
    primary key (id)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;