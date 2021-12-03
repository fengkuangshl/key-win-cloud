package com.key.win.common.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.key.win.common.model.basic.MybatisID;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
