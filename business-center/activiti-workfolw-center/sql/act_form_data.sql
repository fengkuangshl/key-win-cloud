-- ----------------------------
-- 动态表单数据存储
-- ----------------------------
create table act_form_data (
    id varchar(36) not null,
    create_date datetime,
    create_user_id varchar(255),
    create_user_name varchar(255),
    enable_flag bit not null,
    update_date datetime,
    update_user_id varchar(255),
    update_user_name varchar(255),
    version bigint,
    PROC_DEF_ID_ varchar(64) DEFAULT NULL,
    PROC_INST_ID_ varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
    FORM_KEY_ varchar(255) DEFAULT NULL,
    Control_ID_ varchar(100) DEFAULT NULL,
    Control_VALUE_ varchar(2000) DEFAULT NULL,
    primary key (id)
) engine=InnoDB