package com.key.win.common.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.key.win.common.model.basic.MybatisID;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 类说明  角色菜单实体
*/
@Data
@TableName("sys_role_menu")
@EqualsAndHashCode(callSuper=true)
public class SysRoleMenu  extends MybatisID {

	@TableField(value="role_id")
	private String roleId;
	@TableField(value="menu_id")
    private String menuId;

}
