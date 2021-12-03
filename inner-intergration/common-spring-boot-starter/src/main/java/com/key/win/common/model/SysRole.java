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
import java.util.Date;

/**
 * @author 作者 owen 
 * @version 创建时间：2017年11月12日 上午22:57:51
 * 类说明 角色
 */
@Data
@TableName("sys_role")
@EqualsAndHashCode(callSuper=true)
public class SysRole extends MybatisID {

	private String code ;
	private String name;
	
	
	@TableField(exist=false)
	private Long userId;
}
