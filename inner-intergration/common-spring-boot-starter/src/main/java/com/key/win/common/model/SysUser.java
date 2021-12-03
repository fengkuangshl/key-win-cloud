package com.key.win.common.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.key.win.common.model.base.MybatisID;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author 作者 owen 
 * @version 创建时间：2017年11月12日 上午22:57:51 
 * 类说明 用户实体
 */
@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper=true)
public class SysUser  extends MybatisID {

	private String username;
	private String password;
	@TableField(value="nick_name")
	private String nickname;
	@TableField(value="head_img_url")
	private String headImgUrl;
	private String phone;
	private Integer sex;
	private Boolean enabled;
	private String type;
	
	@TableField(exist=false)
	private List<SysRole> roles;
	
	@TableField(exist=false)
	private String roleId;

	@TableField(exist=false)
	private String oldPassword;
	@TableField(exist=false)
	private String newPassword;

}
