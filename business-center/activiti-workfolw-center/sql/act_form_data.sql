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
    `PROC_DEF_ID_` VARCHAR(64) DEFAULT NULL COMMENT '流程定义ID',
    `PROC_INST_ID_`VARCHAR(64) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI DEFAULT NULL COMMENT '流程实例ID',
    `PROC_TASK_ID_` VARCHAR(64) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI DEFAULT NULL COMMENT '任务ID',
    `FORM_KEY_` VARCHAR(255) DEFAULT NULL COMMENT '表单KEY',
    `CONTROL_ID_`VARCHAR(100) DEFAULT NULL COMMENT '控件ID',
    `CONTROL_VALUE_` VARCHAR(2000) DEFAULT NULL COMMENT '控件值',
    `CONTROL_VALUE_TYPE_` VARCHAR(64) DEFAULT NULL COMMENT '控件值类型',
    `CONTROL_LABEL_` VARCHAR(255) DEFAULT NULL COMMENT '控件名称',
    `CONTROL_VALUE_PARAM_TYPE_` VARCHAR(255) DEFAULT NULL COMMENT '控件值做为其它控件的参数时类型',
    `READ_ONLY_CONTROL_` bit(1) DEFAULT NULL COMMENT '控件是否为只读1:只读，0：可写',
    primary key (id)
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;