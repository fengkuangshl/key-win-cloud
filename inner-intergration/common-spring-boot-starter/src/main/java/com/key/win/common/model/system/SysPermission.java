package com.key.win.common.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.key.win.common.model.basic.MybatisID;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;


/**
 * @author 作者 owen 
 * @version 创建时间：2017年11月12日 上午22:57:51
 * 类说明 权限标识
 */
@Data
@TableName("sys_permission")
@EqualsAndHashCode(callSuper=true)
public class SysPermission extends MybatisID {


	private String permission;
	private String name;
	@TableField(exist=false)
	private String roleId;
	
	
	@TableField(exist=false)
	private Set<String> authIds;

}
