package com.key.win.uaa.service.impl;

import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.model.SysService;
import com.key.win.uaa.dao.SysClientServiceDao;
import com.key.win.uaa.dao.SysServiceDao;
import com.key.win.uaa.service.SysServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: [gitgeek]
 * @Date: [2018-08-23 15:18]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.zzg]
 */

@Slf4j
@Service
@Transactional
public class SysServiceServiceImpl implements SysServiceService {


    @Autowired
    private SysServiceDao sysServiceDao;

    @Autowired
    private SysClientServiceDao sysClientServiceDao;


    /**
     * 添加服务
     *
     * @param service
     */
    @Override
    public void save(SysService service) {
        try {
        	service.setCreateTime(new Date());
			service.setUpdateTime(new Date());
			sysServiceDao.save(service);
			log.info("添加服务：{}", service);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
    }

    /**
     * 更新服务
     *
     * @param service
     */
    @Override
    public void update(SysService service) {
        try {
			service.setUpdateTime(new Date());
			service.setUpdateTime(new Date());
			sysServiceDao.updateByPrimaryKey(service);
			log.info("更新服务：{}", service);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
    }

    /**
     * 删除服务
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        try {
			SysService sysService = sysServiceDao.findById(id);
			sysServiceDao.deleteByParentId(sysService.getId());
			sysServiceDao.delete(id);
			log.info("删除服务:{}",sysService);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
    }

    /**
     * 客户端分配服务
     *
     * @param clientId
     * @param serviceIds
     */
    @Override
    public void setMenuToClient(Long clientId, Set<Long> serviceIds) {
        try {
			sysClientServiceDao.delete(clientId,null);

			if (!CollectionUtils.isEmpty(serviceIds)){
			    serviceIds.forEach(serviceId -> {
			        sysClientServiceDao.save(clientId,serviceId);
			    });

			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

    }

    /**
     * 客户端服务列表
     *
     * @param clientIds
     * @return
     */
    @Override
    public List<SysService> findByClient(Set<Long> clientIds) {
        try {
			return sysClientServiceDao.findServicesBySlientIds(clientIds);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
    }

    /**
     * 服务列表
     *
     * @return
     */
    @Override
    public List<SysService> findAll() {
        try {
			return sysServiceDao.findAll();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
    }

    /**
     * ID获取服务
     *
     * @param id
     * @return
     */
    @Override
    public SysService findById(Long id) {
        try {
			return sysServiceDao.findById(id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
    }

    /**
     * 角色ID获取服务
     *
     * @param clientId
     * @return
     */
    @Override
    public Set<Long> findServiceIdsByClientId(Long clientId) {
        try {
			return sysClientServiceDao.findServiceIdsByClientId(clientId);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
    }

    /**
     * 一级服务
     *
     * @return
     */
    @Override
    public List<SysService> findOnes() {
        try {
			return sysServiceDao.findOnes();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
    }

}
