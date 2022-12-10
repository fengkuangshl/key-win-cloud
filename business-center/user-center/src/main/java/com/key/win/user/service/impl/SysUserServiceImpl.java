package com.key.win.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.exception.business.BizException;
import com.key.win.common.exception.illegal.UserIllegalException;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.util.BeanUtils;
import com.key.win.common.util.KeyWinConstantUtils;
import com.key.win.common.util.SysUserUtil;
import com.key.win.common.util.UUIDUtils;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.enums.UserTypeEnum;
import com.key.win.common.model.system.*;
import com.key.win.mybatis.page.MybatisPageServiceTemplate;
import com.key.win.user.dao.*;
import com.key.win.user.service.*;
import com.key.win.user.dao.SysUserDao;
import com.key.win.user.service.SysUserService;
import com.key.win.user.vo.SysUserVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Autowired
    private SysUserGroupDao sysUserGroupDao;

    @Autowired
    private SysRoleMenuPermissionService sysRoleMenuPermissionService;

    @Autowired
    private SysMenuPermissionService sysMenuPermissionService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private  SysRoleService sysRoleService;

    @Autowired
    private SysGroupService sysGroupService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Autowired(required = false)
    private TokenStore redisTokenStore;

    @Override
    public boolean updateSysUser(SysUserVo sysUser) {
        if (sysUser.getId() == null) {
            logger.error("用户id不存在");
            throw new IllegalArgumentException("用户id不存在!");
        }
        SysUser updateUser = this.getById(sysUser.getId());
        if (updateUser == null) {
            logger.error("用户不存在");
            throw new IllegalArgumentException("用户不存在!");
        }
        if (StringUtils.isNotBlank(sysUser.getUsername()) && !updateUser.getUsername().equals(sysUser.getUsername())) {
            logger.error("用户名非法，由{}被必为{}", updateUser.getUsername(), sysUser.getUsername());
            throw new IllegalArgumentException("用户名非法！");
        }
        setRoleToUser(sysUser);
        setGroupToUser(sysUser);

        updateUser.setNickname(sysUser.getNickname());
        updateUser.setPhone(sysUser.getPhone());
        updateUser.setType(sysUser.getType());
        updateUser.setSex(sysUser.getSex());
        updateUser.setHeadImgUrl(sysUser.getHeadImgUrl());
        boolean b = this.saveOrUpdate(updateUser);
        if (b) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof OAuth2Authentication) {
                OAuth2Authentication oAuth2Auth = (OAuth2Authentication) authentication;
                authentication = oAuth2Auth.getUserAuthentication();

                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) oAuth2Auth.getDetails();

                LoginAppUser user = SysUserUtil.getLoginAppUser();

                if (user != null) {

                    if (!ObjectUtils.notEqual(user.getId(), sysUser.getId())) {

                        OAuth2AccessToken token = redisTokenStore.readAccessToken(details.getTokenValue());

                        if (token != null) {

                            if (!StringUtils.isBlank(sysUser.getHeadImgUrl())) {
                                user.setHeadImgUrl(sysUser.getHeadImgUrl());
                            }

                            if (!StringUtils.isBlank(sysUser.getNewPassword())) {
                                user.setPassword(sysUser.getNewPassword());
                            }

                            if (!StringUtils.isBlank(sysUser.getNewPassword())) {
                                user.setPassword(sysUser.getNewPassword());
                            }

                            if (!StringUtils.isBlank(sysUser.getNickname())) {
                                user.setNickname(sysUser.getNickname());
                            }

                            if (!StringUtils.isBlank(sysUser.getPhone())) {
                                user.setPhone(sysUser.getPhone());
                            }

                            if (sysUser.getSex() != null) {
                                user.setSex(sysUser.getSex());
                            }

                            UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(user,
                                    null, user.getAuthorities());

                            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Auth.getOAuth2Request(), userAuthentication);
                            oAuth2Authentication.setAuthenticated(true);
                            redisTokenStore.storeAccessToken(token, oAuth2Authentication);

                        }

                    }

                }
            }
        }
        return b;
    }

    public LoginAppUser findByUsername(String username) throws ServiceException {
        try {
            List<SysUser> list = this.findSysUserByUserName(username);
            return getLoginAppUser(username, list);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public LoginAppUser findByMobile(String mobile) throws ServiceException {
        try {
            SysUser u = new SysUser();
            u.setPhone(mobile);
            List<SysUser> list = this.findSysUsers(u);
            return getLoginAppUser(mobile, list);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private LoginAppUser getLoginAppUser(String mobile, List<SysUser> list) throws AccountException {
        if (list == null || list.size() == 0) {
            logger.error("{}用户不存在！", mobile);
            throw new AccountNotFoundException("用户名或密码有误！");
        }
        if (list.size() > 1) {
            logger.error("{}用户存在{}个", mobile, list.size());
            throw new AccountException("帐号不唯一,请联系管理员！");
        }
        SysUser sysUser = list.get(0);
        if (sysUser != null) {
            LoginAppUser loginAppUser = new LoginAppUser();
            BeanUtils.copyProperties(sysUser, loginAppUser);
            setUserExtInfo(sysUser, loginAppUser);
            return loginAppUser;
        }
        return null;
    }

    private void setGroupToUser(SysUserVo sysUser) {
        sysUserGroupDao.deleteUserGroup(sysUser.getId(), null);
        if (!CollectionUtils.isEmpty(sysUser.getGroupIds())) {
            sysUserGroupDao.saveBatchUserIdAndGroupIds(sysUser.getId(), sysUser.getGroupIds());
        }
    }

    private void setRoleToUser(SysUserVo sysUser) {
        sysUserRoleDao.deleteUserRole(sysUser.getId(), null);
        if (!CollectionUtils.isEmpty(sysUser.getRoleIds())) {
            sysUserRoleDao.saveBatchUserIdAndRoleIds(sysUser.getId(), sysUser.getRoleIds());
        }
    }

    @Override
    public PageResult<SysUser> findSysUserByPaged(PageRequest<SysUser> t) {
        MybatisPageServiceTemplate<SysUser, SysUser> mybatiesPageServiceTemplate = new MybatisPageServiceTemplate<SysUser, SysUser>(this.baseMapper) {
            @Override
            protected AbstractWrapper constructWrapper(SysUser sysUser) {
                return buildLambdaQueryWrapper(sysUser);
            }
        };
        return mybatiesPageServiceTemplate.doPagingQuery(t);
    }

    private AbstractWrapper buildLambdaQueryWrapper(SysUser sysUser) {
        LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<SysUser>();
        if (sysUser != null) {
            if (StringUtils.isNotBlank(sysUser.getNickname())) {
                lqw.like(SysUser::getNickname, sysUser.getNickname());
            }
            if (StringUtils.isNotBlank(sysUser.getUsername())) {
                lqw.like(SysUser::getUsername, sysUser.getUsername());
            }
            if (StringUtils.isNotBlank(sysUser.getPhone())) {
                lqw.eq(SysUser::getPhone, sysUser.getPhone());
            }
        }
        return lqw;
    }

    public List<SysUser> findSysUsers(SysUser sysUser) {
        return super.list(this.buildLambdaQueryWrapper(sysUser));
    }

    public List<SysUser> findSysUserByUserName(String userName) {
        LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<SysUser>();
        lqw.eq(SysUser::getUsername, userName);
        List<SysUser> list = this.list(lqw);
        return list;
    }

    private void setUserExtInfo(SysUser dbUser, LoginAppUser loginUser) {
        BeanUtils.copyProperties(dbUser, loginUser);

        if (dbUser.getType() == UserTypeEnum.ADMIN) {
            List<SysGroup> groupList = sysGroupService.list();
            List<SysRole> roleList = sysRoleService.list();
            loginUser.setSysGroups(groupList);
            loginUser.setSysRoles(roleList);
            //List<SysMenuPermission> permissionDaoByRoleIds = sysMenuPermissionService.list();
            List<SysMenu> menus = sysMenuService.list();
            List<SysPermission> sysPermissions = sysPermissionService.list();
            loginUser.setPermissions(getMenuPermissions(menus, sysPermissions));
            loginUser.setMenus(menus);
        } else  {
            List<SysGroup> groupByUserId = sysUserGroupDao.findGroupByUserId(dbUser.getId());
            List<SysRole> rolesByUserId = sysUserRoleDao.findRolesByUserId(dbUser.getId());
            loginUser.setSysGroups(groupByUserId);
            loginUser.setSysRoles(rolesByUserId);
            if (!CollectionUtils.isEmpty(rolesByUserId)) {
                Set<Long> roleIds = rolesByUserId.stream().map(SysRole::getId).collect(Collectors.toSet());
                List<SysRoleMenuPermission> grantMenus = sysRoleMenuPermissionService.findGrantMenus(roleIds);
                Set<Long> menuIds = grantMenus.stream().map(SysRoleMenuPermission::getMenuId).collect(Collectors.toSet());
                List<SysMenu> menus = sysMenuService.findSysMenuByMenuIds(menuIds);
                List<SysRoleMenuPermission> grantMenuPermissions = sysRoleMenuPermissionService.findGrantMenuPermissions(roleIds);
                Set<Long> menuPermissionIds = grantMenuPermissions.stream().map(SysRoleMenuPermission::getMenuPermissionId).collect(Collectors.toSet());
                List<SysMenuPermission> sysMenuPermissionByIds = sysMenuPermissionService.findSysMenuPermissionByIds(menuPermissionIds);
                loginUser.setPermissions(sysMenuPermissionByIds);
                loginUser.setMenus(menus);
            }
        }
        if(!CollectionUtils.isEmpty(loginUser.getMenus())){
            Collections.sort(loginUser.getMenus(), new Comparator<SysMenu>() {
                @Override
                public int compare(SysMenu o1, SysMenu o2) {
                    return o1.getSort() - o2.getSort();
                }
            });
        }
    }

    private List<SysMenuPermission> getMenuPermissions(List<SysMenu> menus, List<SysPermission> sysPermissions) {
        List<SysMenuPermission> sysMenuPermissions = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (menu.getParentId() != -1) {
                for (SysPermission sysPermission : sysPermissions) {
                    SysMenuPermission sysMenuPermission = new SysMenuPermission();
                    sysMenuPermission.setChecked(Boolean.TRUE);
                    sysMenuPermission.setPermissionCode(menu.getPath().replaceAll("/", "::") + "::" + sysPermission.getPermission());
                    sysMenuPermission.setMenuId(menu.getId());
                    sysMenuPermission.setPermissionId(sysPermission.getId());
                    sysMenuPermissions.add(sysMenuPermission);
                }
            }
        }
        if (CollectionUtils.isEmpty(sysMenuPermissions)) {
            SysMenuPermission sysMenuPermission = new SysMenuPermission();
            sysMenuPermission.setChecked(Boolean.TRUE);
            sysMenuPermission.setPermissionCode("*::*::*");
            sysMenuPermissions.add(sysMenuPermission);
        }
        return sysMenuPermissions;
    }

    @Override
    public boolean saveSysUser(SysUserVo sysUserVo) {
        List<SysUser> existUsers = this.findSysUserByUserName(sysUserVo.getUsername());
        if (!CollectionUtils.isEmpty(existUsers)) {
            logger.error("{}用户已经存在!", sysUserVo.getUsername());
            throw new BizException("用户已经存在！");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserVo, sysUser);
        sysUser.setPassword(passwordEncoder.encode(KeyWinConstantUtils.RESET_PASSWORD));
        boolean b = this.saveOrUpdate(sysUser);
        if(b){
            sysUserVo.setId(sysUser.getId());
            setRoleToUser(sysUserVo);
        }
        return b;
    }

    @Override
    public boolean modifyPassword(SysUserVo sysUser) {
        SysUser user = this.getById(sysUser.getId());
        if (user == null) {
            logger.error("id为{}的用户不存在数据库中！", sysUser.getId());
            throw new IllegalArgumentException("用户不存在！");
        }
        user.setPassword(passwordEncoder.encode(sysUser.getNewPassword()));
        return this.saveOrUpdate(user);
    }

    @Override
    public boolean resetPassword(Long id) {
        SysUser user = this.getById(id);
        if (user == null) {
            logger.error("id为{}的用户不存在数据库中！", id);
            throw new BizException("用户不存在！");
        }
        user.setPassword(passwordEncoder.encode(KeyWinConstantUtils.RESET_PASSWORD));
        return this.saveOrUpdate(user);
    }

    @Override
    public void setUserToGroup(Long groupId, Set<Long> userIds) {
        sysUserGroupDao.deleteUserGroup(null, groupId);
        if (!CollectionUtils.isEmpty(userIds)) {
            sysUserGroupDao.saveBatchUserIdsAndGroupId(userIds, groupId);
        }
    }

    @Override
    public SysUserVo getUserFullById(Long id) {
        SysUserVo sysUserVo = new SysUserVo();
        SysUser byId = this.getById(id);
        BeanUtils.copyProperties(byId, sysUserVo);
        sysUserVo.setSysRoles(sysUserRoleDao.findRolesByUserId(id));
        sysUserVo.setSysGroups(sysUserGroupDao.findGroupByUserId(id));

        if (!CollectionUtils.isEmpty(byId.getSysGroups())) {
            Set<Long> collect = byId.getSysGroups().stream().map(SysGroup::getId).collect(Collectors.toSet());
            sysUserVo.setGroupIds(collect);

        }
        if (!CollectionUtils.isEmpty(byId.getSysRoles())) {
            Set<Long> collect = byId.getSysRoles().stream().map(SysRole::getId).collect(Collectors.toSet());
            sysUserVo.setRoleIds(collect);
        }
        return sysUserVo;
    }

    @Override
    public boolean deleteById(Long id) {
        SysUser userFullById = this.getUserFullById(id);
        List<SysRole> sysRoles = userFullById.getSysRoles();
        List<SysGroup> sysGroups = userFullById.getSysGroups();
        if (!CollectionUtils.isEmpty(sysRoles)) {
            logger.error("删除用户[{}]时，发现已关联SysRole信息！", id);
            throw new BizException("请先解除关联的角色信息！");
        }
        if (!CollectionUtils.isEmpty(sysGroups)) {
            logger.error("删除用户[{}]时，发现已关联SysGroup信息！", id);
            throw new BizException("请先解除关联的用户组信息！");
        }
        return this.removeById(id);
    }

    @Override
    public boolean updateEnabled(SysUser sysUser) {
        if (sysUser.getId() == null) {
            logger.error("用户id不存在");
            throw new IllegalArgumentException("用户id不存在!");
        }
        SysUser appUser = this.getById(sysUser.getId());
        if (appUser == null) {
            logger.error("用户不存在！");
            throw new BizException("用户不存在！");
        }
        appUser.setEnabled(sysUser.getEnabled());

        return this.saveOrUpdate(appUser);
    }
}
