package com.key.win.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.key.win.common.model.base.MybatisID;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
 
/**
* @author 作者 gitgeek 
* @version 创建时间：2018-08-06 21:29
* 类说明  用户角色实体
*/
@Data
@TableName("sys_role_user")
@EqualsAndHashCode(callSuper=true)
public class SysUserRole  extends MybatisID {


	@TableField(value="user_id")
	private String userId;
	@TableField(value="role_id")
    private String roleId;

}
