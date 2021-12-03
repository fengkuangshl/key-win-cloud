package com.key.win.common.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.key.win.common.model.basic.MybatisID;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
