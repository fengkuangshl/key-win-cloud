package com.key.win.activiti.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.key.win.common.model.basic.MybatisID;
import lombok.Data;

/**
 * CREATE TABLE `formdata` (
 *   `PROC_DEF_ID_` varchar(64) DEFAULT NULL,
 *   `PROC_INST_ID_` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
 *   `FORM_KEY_` varchar(255) DEFAULT NULL,
 *   `Control_ID_` varchar(100) DEFAULT NULL,
 *   `Control_VALUE_` varchar(2000) DEFAULT NULL
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */

@Data
@TableName("act_form_data")
public class FormData extends MybatisID {
    @TableField("PROC_DEF_ID_")
    private String procDefId;
    @TableField("PROC_INST_ID_")
    private String procInstId;
    @TableField("PROC_TASK_ID_")
    private String procTaskId;
    @TableField("FORM_KEY_")
    private String formKey;
    @TableField("CONTROL_ID_")
    private String controlId;
    @TableField("CONTROL_LABEL_")
    private String controlLabel;
    @TableField("CONTROL_VALUE_")
    private String controlValue;
    @TableField("CONTROL_TYPE_")
    private String controlType;
    @TableField("CONTROL_VALUE_PARAM_TYPE_")
    private String controlValueParamType;
    @TableField("READ_ONLY_CONTROL_")
    private Boolean isReadOnlyControl = Boolean.FALSE;
    @TableField("SHOW_CONTROL_")
    private Boolean isShowControl = Boolean.TRUE;
    @TableField("CONTROL_VALUE_OPTIONS")
    private String controlValueOptions;
    @TableField("CONTROL_EVENT")
    private String controlEvent;
    @TableField("CONTROL_EVENT_TYPE")
    private String controlEventType;
}
