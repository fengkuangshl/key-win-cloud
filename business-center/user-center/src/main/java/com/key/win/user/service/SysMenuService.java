package com.key.win.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.model.SysMenu;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

import java.util.List;
import java.util.Set;

public interface SysMenuService extends IService<SysMenu> {

//	/**
//	 * 添加菜单
//	 * @param menu
//	 */
//	void save(SysMenu menu)  throws ServiceException;
//
//	/**
//	 * 更新菜单
//	 * @param menu
//	 */
//	void update(SysMenu menu)  throws ServiceException;
//
//	/**
//	 * 删除菜单
//	 * @param id
//	 */
//	void delete(String id)  throws ServiceException;

	/**
	 * 角色分配菜单
	 * @param roleId
	 * @param menuIds
	 */
	void setMenuToRole(String roleId, Set<String> menuIds)  throws ServiceException;

	/**
	 * 角色菜单列表
	 * @param roleIds
	 * @return
	 */
	List<SysMenu> findByRoles(Set<String> roleIds)  throws ServiceException;

	/**
	 * 菜单列表
	 * @return
	 */
	// List<SysMenu> findAll()  throws ServiceException;

	/**
	 * ID获取菜单
	 * @param id
	 * @return
	 */
	SysMenu findById(String id)  throws ServiceException;

	/**
	 * 角色ID获取菜单
	 * @param roleId
	 * @return
	 */
	Set<String> findMenuIdsByRoleId(String roleId)  throws ServiceException;

	List<SysMenu> findOnes()  throws ServiceException;


    PageResult<SysMenu> findSysRoleByPaged(PageRequest<SysMenu> t);
}
