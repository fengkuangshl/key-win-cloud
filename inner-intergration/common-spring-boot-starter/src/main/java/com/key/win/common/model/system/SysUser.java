package com.key.win.common.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.key.win.common.model.basic.MybatisID;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
