package com.key.win.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.key.win.common.model.SysMenu;
import com.key.win.common.model.SysRoleMenu;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
* @author 作者 owen 
* @version 创建时间：2017年11月13日 上午22:57:51
 * 角色菜单
 */
@Mapper
public interface SysRoleMenuDao  extends BaseMapper<SysRoleMenu> {

	@Insert("insert into sys_role_menu(role_id, menu_id) values(#{roleId}, #{menuId})")
	int save( SysRoleMenu menu );

	
	// void saveBatch(@Param("roleId") String roleId, @Param("menuIds") Set<String> menuIds);
	
	int deleteBySelective(@Param("roleId") String roleId, @Param("menuId") String menuId);

	@Select("select t.menu_id menuId from sys_role_menu t where t.role_id = #{roleId}")
	Set<String> findMenuIdsByRoleId(String roleId);

	List<SysMenu> findMenusByRoleIds(@Param("roleIds") Set<String> roleIds);

	
}
