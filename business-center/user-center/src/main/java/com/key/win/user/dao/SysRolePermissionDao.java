package com.key.win.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.key.win.common.model.system.SysPermission;
import com.key.win.common.model.system.SysRolePermission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

 
/**
* @author 作者 owen 
* @version 创建时间：2017年11月12日 上午22:57:51
 * 角色权限关系
 */
@Mapper
public interface SysRolePermissionDao extends BaseMapper<SysRolePermission> {

	@Insert("insert into sys_role_permission(role_id, permission_id) values(#{roleId}, #{permissionId})")
	int save(@Param("roleId") String roleId, @Param("permissionId") String permissionId);

	void saveBatch(@Param("roleId") String roleId, @Param("permissions") Set<String> permissions);
	
	int deleteBySelective(@Param("roleId") String roleId, @Param("permissionId") String permissionId);

	Set<SysPermission> findByRoleIds(@Param("roleIds") Set<String> roleIds);

}
