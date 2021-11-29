package com.key.win.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.google.common.collect.Maps;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.model.SysMenu;
import com.key.win.common.model.SysRole;
import com.key.win.common.model.SysUser;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.page.MybatiesPageServiceTemplate;
import com.key.win.user.dao.SysMenuDao;
import com.key.win.user.dao.SysRoleMenuDao;
import com.key.win.user.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
@Service
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuDao menuDao;
 	@Autowired
	private SysRoleMenuDao roleMenuDao; 

	@Transactional
	@Override
	public void save(SysMenu menu)  throws ServiceException {
		try {
			menu.setCreateTime(new Date());
			menu.setUpdateTime(menu.getCreateTime());

			menuDao.save(menu);
			log.info("新增菜单：{}", menu);
		} catch (Exception e) {
//			BizLog.info("菜单保存处理失败", LogEntry.builder().clazz(this.getClass().getName()).method("save").error(e.getMessage()).build());
			throw new ServiceException(e) ;
		}
	}

	@Transactional
	@Override
	public void update(SysMenu menu)  throws ServiceException {
		try {
			menu.setUpdateTime(new Date());

			menuDao.updateByPrimaryKey(menu);
			log.info("修改菜单：{}", menu);
		} catch (Exception e) {
//			BizLog.info("菜单修改处理失败", LogEntry.builder().clazz(this.getClass().getName()).method("update").error(e.getMessage()).build());
			throw new ServiceException(e) ;
		}
	}

	@Transactional
	@Override
	public void delete(Long id)  throws ServiceException{
		try {
			SysMenu menu = menuDao.findById(id);
			menuDao.deleteByPrimaryKey(id);
			log.info("删除菜单：{}", menu);
		} catch (Exception e) {
//			BizLog.info("菜单删除处理失败", LogEntry.builder().clazz(this.getClass().getName()).method("delete").error(e.getMessage()).build());
			throw new ServiceException(e) ;
		}
	}

	
	@Override
	@Transactional
	public void setMenuToRole(Long roleId, Set<Long> menuIds)  throws ServiceException {
		try {
			roleMenuDao.deleteBySelective(roleId, null);
			if (!CollectionUtils.isEmpty(menuIds)) {
				roleMenuDao.saveBatch(roleId , menuIds ) ;
			}
		} catch (Exception e) {
//			BizLog.info("菜单角色处理失败", LogEntry.builder().clazz(this.getClass().getName()).method("setMenuToRole").error(e.getMessage()).build());
			throw new ServiceException(e);
		}
	}

	@Override
	public List<SysMenu> findByRoles(Set<Long> roleIds)  throws ServiceException{
		try {
			return roleMenuDao.findMenusByRoleIds(roleIds);
		} catch (Exception e) {
//			BizLog.info("角色菜单处理失败", LogEntry.builder().clazz(this.getClass().getName()).method("findByRoles").error(e.getMessage()).build());
			throw new ServiceException(e);
		}
	}

	@Override
	public List<SysMenu> findAll()  throws ServiceException{
		try {
			return menuDao.findList(Maps.newHashMap()); //查询全部菜单
		} catch (Exception e) {
//			BizLog.info("菜单列表失败", LogEntry.builder().clazz(this.getClass().getName()).method("findAll").error(e.getMessage()).build());
			throw new ServiceException(e);
		}
	}

	@Override
	public SysMenu findById(Long id)  throws ServiceException{
		try {
			return menuDao.findById(id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Set<Long> findMenuIdsByRoleId(Long roleId)  throws ServiceException{
		try {
			return roleMenuDao.findMenuIdsByRoleId(roleId);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<SysMenu> findOnes()  throws ServiceException{
		try {
			HashMap<String, Object> params = Maps.newHashMap() ;
			params.put("isMenu", 1);
			return menuDao.findList(params);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

    @Override
    public PageResult<SysMenu> findSysRoleByPaged(PageRequest<SysMenu> t){
        MybatiesPageServiceTemplate<SysMenu, SysMenu> page = new MybatiesPageServiceTemplate<SysMenu, SysMenu>(menuDao) {
            @Override
            protected AbstractWrapper constructWrapper(SysMenu sysMenu) {
                LambdaQueryWrapper<SysMenu> lqw = new LambdaQueryWrapper<SysMenu>();
                if (sysMenu != null && StringUtils.isNotBlank(sysMenu.getName())) {
                    lqw.eq(SysMenu::getName, sysMenu.getName() == null ? "" : sysMenu.getName());
                }
                lqw.orderByDesc(SysMenu::getCreateTime);
                return lqw;
            }

            @Override
            protected List getDefaultQueryOrder(SysMenu sysMenu, String sortName) {
                List<SFunction<SysMenu,?>> list = new ArrayList<>();
                if("name".equals(sortName)){
                    list.add(SysMenu::getName);
                }
                if("path".equals(sortName)){
                    list.add(SysMenu::getPath);
                }
                if("url".equals(sortName)){
                    list.add(SysMenu::getUrl);
                }
                if("css".equals(sortName)){
                    list.add(SysMenu::getCss);
                }
                if("isMenu".equals(sortName)){
                    list.add(SysMenu::getIsMenu);
                }
                if("createTime".equals(sortName)){
                    list.add(SysMenu::getCreateTime);
                }
                if("sort".equals(sortName)){
                    list.add(SysMenu::getSort);
                }
                return list;
            }
        };
        return page.doPagingQuery(t);
    }

}
