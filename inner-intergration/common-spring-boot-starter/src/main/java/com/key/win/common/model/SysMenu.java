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
import java.util.List;
import java.util.Set;

/**
* @author 作者 gitgeek 
* @version 创建时间：2018-08-06 21:29
* 类说明  菜单实体
*/
@Data
@TableName("sys_menu")
@EqualsAndHashCode(callSuper=true)
public class SysMenu  extends MybatisID {

	@TableField(value="parent_id")
	private String parentId;
	private String name;
	private String url;
	private String path;
	private String css;
	private Integer sort;
	@TableField(value="is_menu")
	private Integer isMenu;
	private Boolean hidden;
	
	@TableField(exist=false)
	private List<SysMenu> subMenus;
	@TableField(exist=false)
	private String roleId;
	@TableField(exist=false)
	private Set<String> menuIds;

}
