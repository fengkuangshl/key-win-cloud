package com.key.win.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class MybatisCommonFleid extends MybatisVersion {
    /**
     *
     */
    private static final long serialVersionUID = 3803687288306105240L;

    @TableField(fill = FieldFill.INSERT)
    private Date createDate = new Date();

    @TableField(fill = FieldFill.UPDATE)
    private Date updateDate = new Date();

    @TableField(fill = FieldFill.INSERT)
    private String createUserId;

    @TableField(fill = FieldFill.UPDATE)
    private String updateUserId;

    @TableLogic(value = "0", delval = "1")
    private Boolean enableFlag = Boolean.FALSE;

    @TableField(fill = FieldFill.INSERT)
    private String createUserName;

    @TableField(fill = FieldFill.UPDATE)
    private String updateUserName;

}