package com.key.win.user.service;

import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.model.SysPermission;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

import java.util.Map;
import java.util.Set;

/**
* @author 作者 owen 
* @version 创建时间：2017年11月12日 上午22:57:51
 */
public interface SysPermissionService {

	/**
	 * 根绝角色ids获取权限集合
	 * 
	 * @param roleIds
	 * @return
	 */
	Set<SysPermission> findByRoleIds(Set<Long> roleIds)  throws ServiceException;

	/**
	 * 保存权限
	 * @param sysPermission
	 */
	void save(SysPermission sysPermission)  throws ServiceException;

	/**
	 * 修改权限
	 * @param sysPermission
	 */
	void update(SysPermission sysPermission)  throws ServiceException;

	/**
	 * 删除权限
	 * @param id
	 */
	void delete(Long id)  throws ServiceException;

	/**
	 * 权限列表
	 * @param params
	 * @return
	 */
	PageResult<SysPermission> findPermissions(Map<String, Object> params)  throws ServiceException;

	/**
	 * 授权
	 * @param roleId
	 * @param permissions
	 */
	void setPermissionToRole(Long roleId, Set<Long> permissions)  throws ServiceException;

    PageResult<SysPermission> getSysPermissionByPaged(PageRequest<SysPermission> t);
}
