package com.key.win.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.model.SysMenu;
import com.key.win.common.model.SysPermission;
import com.key.win.common.model.SysRole;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.page.MybatiesPageServiceTemplate;
import com.key.win.user.dao.SysRoleDao;
import com.key.win.user.dao.SysRoleMenuDao;
import com.key.win.user.dao.SysRolePermissionDao;
import com.key.win.user.dao.SysUserRoleDao;
import com.key.win.user.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysUserRoleDao userRoleDao;
    @Autowired
    private SysRolePermissionDao rolePermissionDao;

    @Autowired
    private SysRoleMenuDao roleMenuDao;


    @Transactional
    @Override
    public void save(SysRole sysRole) throws ServiceException {
        try {
            SysRole role = sysRoleDao.findByCode(sysRole.getCode());
            if (role != null) {
                throw new IllegalArgumentException("角色code已存在");
            }

            sysRole.setCreateTime(new Date());
            sysRole.setUpdateTime(sysRole.getCreateTime());

            sysRoleDao.save(sysRole);
            log.info("保存角色：{}", sysRole);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public void update(SysRole sysRole) throws ServiceException {
        try {
            sysRole.setUpdateTime(new Date());
            sysRoleDao.updateByPrimaryKey(sysRole);
            log.info("修改角色：{}", sysRole);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public void deleteRole(Long id) throws ServiceException {
        try {
            SysRole sysRole = sysRoleDao.findById(id);

            sysRoleDao.deleteByPrimaryKey(id);
            rolePermissionDao.deleteBySelective(id, null);
            roleMenuDao.deleteBySelective(id, null);
            userRoleDao.deleteUserRole(null, id);

            log.info("删除角色：{}", sysRole);
        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }


    @Override
    public SysRole findById(Long id) throws ServiceException {
        try {
            return sysRoleDao.findById(id);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PageResult<SysRole> findRoles(Map<String, Object> params) throws ServiceException {
        try {

//			BizLog.info("tttt", LogEntry.builder().msg("hello").build());
            //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
            if (MapUtils.getInteger(params, "page") != null && MapUtils.getInteger(params, "limit") != null)
                PageHelper.startPage(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"), true);
            List<SysRole> list = sysRoleDao.findList(params);
            PageInfo<SysRole> pageInfo = new PageInfo(list);

            return PageResult.<SysRole>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Set<SysPermission> findPermissionsByRoleId(Long roleId) throws ServiceException {
        try {
            return rolePermissionDao.findByRoleIds(Sets.newHashSet(roleId));
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result saveOrUpdate(SysRole sysRole) throws ServiceException {
        try {
            int i = 0;
            if (sysRole.getId() == null) {
                SysRole role = sysRoleDao.findByCode(sysRole.getCode());
                if (role != null) {
                    return Result.failed("角色code已存在");
                }
                sysRole.setCreateTime(new Date());
                sysRole.setUpdateTime(sysRole.getCreateTime());
                i = sysRoleDao.save(sysRole);
            } else {
                sysRole.setUpdateTime(new Date());
                i = sysRoleDao.updateByPrimaryKey(sysRole);
            }
            return i > 0 ? Result.succeed("操作成功") : Result.failed("操作失败");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PageResult<SysRole> findSysRoleByPaged(PageRequest<SysRole> t) {
        MybatiesPageServiceTemplate<SysRole, SysRole> page = new MybatiesPageServiceTemplate<SysRole, SysRole>(sysRoleDao) {
            @Override
            protected AbstractWrapper constructWrapper(SysRole role) {
                LambdaQueryWrapper<SysRole> lqw = new LambdaQueryWrapper<SysRole>();
                if (role != null && StringUtils.isNotBlank(role.getName())) {
                    lqw.eq(SysRole::getName, role.getName() == null ? "" : role.getName());
                }
                if (role != null && StringUtils.isNotBlank(role.getCode())) {
                    lqw.eq(SysRole::getCode, role.getCode() == null ? "" : role.getCode());
                }
                lqw.orderByDesc(SysRole::getCreateTime);
                return lqw;
            }
            @Override
            protected List getDefaultQueryOrder(SysRole role, String sortName) {
                List<SFunction<SysRole,?>> list = new ArrayList<>();
                if("name".equals(sortName)){
                    list.add(SysRole::getName);
                }
                if("code".equals(sortName)){
                    list.add(SysRole::getCode);
                }
                if("createTime".equals(sortName)){
                    list.add(SysRole::getCreateTime);
                }
                return list;
            }
        };
        return page.doPagingQuery(t);
    }

    public List<SysRole> findAllSysRole(){
        LambdaQueryWrapper<SysRole> lqw = new LambdaQueryWrapper<SysRole>();
        return sysRoleDao.selectList(lqw);
    }

}
