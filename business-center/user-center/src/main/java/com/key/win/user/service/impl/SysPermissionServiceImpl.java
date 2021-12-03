package com.key.win.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.model.SysPermission;
import com.key.win.common.model.SysRolePermission;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.page.MybatiesPageServiceTemplate;
import com.key.win.user.dao.SysPermissionDao;
import com.key.win.user.dao.SysRolePermissionDao;
import com.key.win.user.service.SysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author 作者 owen
 * @version 创建时间：2017年11月12日 上午22:57:51
 */
@Slf4j
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    private SysPermissionDao sysPermissionDao;
    @Autowired
    private SysRolePermissionDao rolePermissionDao;

    @Override
    public Set<SysPermission> findByRoleIds(Set<String> roleIds) throws ServiceException {
        try {
            return rolePermissionDao.findByRoleIds(roleIds);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }


    @Override
    @Transactional
    public void save(SysPermission sysPermission) throws ServiceException {
        try {
            SysPermission permission = sysPermissionDao.findByPermission(sysPermission.getPermission());
            if (permission != null) {
                throw new IllegalArgumentException("权限标识已存在");
            }

            sysPermissionDao.insert(sysPermission);
            log.info("保存权限标识：{}", sysPermission);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }


    @Override
    @Transactional
    public void update(SysPermission sysPermission) throws ServiceException {
        try {
            sysPermissionDao.updateByPrimaryKey(sysPermission);
            log.info("修改权限标识：{}", sysPermission);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }


    @Override
    @Transactional
    public void delete(String id) throws ServiceException {
        try {
            SysPermission permission = sysPermissionDao.findById(id);
            if (permission == null) {
                throw new IllegalArgumentException("权限标识不存在");
            }

            sysPermissionDao.deleteByPrimaryKey(id);
            rolePermissionDao.deleteBySelective(null, id);
            log.info("删除权限标识：{}", permission);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PageResult<SysPermission> findPermissions(Map<String, Object> params) throws ServiceException {
        try {
            //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
            if (MapUtils.getInteger(params, "page") != null && MapUtils.getInteger(params, "limit") != null)
                PageHelper.startPage(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"), true);
            List<SysPermission> list = sysPermissionDao.findList(params);
            PageInfo<SysPermission> pageInfo = new PageInfo(list);

            return PageResult.<SysPermission>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }


    @Override
    @Transactional
    public void setPermissionToRole(String roleId, Set<String> permissions) throws ServiceException {
        try {
            rolePermissionDao.deleteBySelective(roleId, null);

            if (!CollectionUtils.isEmpty(permissions)) {
                for (String permission : permissions) {
                    SysRolePermission sysRolePermission = new SysRolePermission();
                    sysRolePermission.setPermissionId(permission);
                    sysRolePermission.setRoleId(roleId);
                    this.rolePermissionDao.insert(sysRolePermission);
                }
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public PageResult<SysPermission> getSysPermissionByPaged(PageRequest<SysPermission> t) {
        MybatiesPageServiceTemplate<SysPermission, SysPermission> page = new MybatiesPageServiceTemplate<SysPermission, SysPermission>(sysPermissionDao) {
            @Override
            protected AbstractWrapper constructWrapper(SysPermission sysPermission) {
                LambdaQueryWrapper<SysPermission> lqw = new LambdaQueryWrapper<SysPermission>();
                if (sysPermission != null && StringUtils.isNotBlank(sysPermission.getName())) {
                    lqw.like(SysPermission::getName, "%" + (sysPermission.getName() == null ? "" : sysPermission.getName()) + "%").or().
                            like(SysPermission::getPermission, "%" + (sysPermission.getName() == null ? "" : sysPermission.getName()) + "%");
                }
                return lqw;
            }

            @Override
            protected List getDefaultQueryOrder(SysPermission sysPermission, String sortName) {
                List<SFunction<SysPermission, ?>> list = new ArrayList<>();
                if ("name".equals(sortName)) {
                    list.add(SysPermission::getName);
                }
                if ("permission".equals(sortName)) {
                    list.add(SysPermission::getPermission);
                }
                if ("createDate".equals(sortName)) {
                    list.add(SysPermission::getCreateDate);
                }
                return list;
            }
        };
        return page.doPagingQuery(t);
    }
}
