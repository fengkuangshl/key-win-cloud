package com.key.win.uaa.service;

import com.key.win.common.model.auth.SysService;

import java.util.List;
import java.util.Set;

public interface SysServiceService {

	/**
	 * 添加服务
	 * @param service
	 */
	void save(SysService service);

	/**
	 * 更新服务
	 * @param service
	 */
	void update(SysService service);

	/**
	 * 删除服务
	 * @param id
	 */
	void delete(String id);

	/**
	 * 客户端分配服务
	 * @param clientId
	 * @param serviceIds
	 */
	void setMenuToClient(String clientId, Set<String> serviceIds);

	/**
	 * 客户端服务列表
	 * @param clientIds
	 * @return
	 */
	List<SysService> findByClient(Set<String> clientIds);

	/**
	 * 服务列表
	 * @return
	 */
	List<SysService> findAll();

	/**
	 * ID获取服务
	 * @param id
	 * @return
	 */
	SysService findById(String id);

	/**
	 * 角色ID获取服务
	 * @param clientId
	 * @return
	 */
	Set<String> findServiceIdsByClientId(String clientId);

	/**
	 * 一级服务
	 * @return
	 */
	List<SysService> findOnes();


}
