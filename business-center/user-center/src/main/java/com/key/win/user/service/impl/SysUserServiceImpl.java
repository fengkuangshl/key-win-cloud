package com.key.win.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.constant.UserType;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.model.system.SysPermission;
import com.key.win.common.model.system.SysRole;
import com.key.win.common.model.system.SysUser;
import com.key.win.common.model.system.SysUserRole;
import com.key.win.common.util.PageUtil;
import com.key.win.common.util.SysUserUtil;
import com.key.win.common.util.ValidatorUtil;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.page.MybatiesPageServiceTemplate;
import com.key.win.user.dao.SysUserDao;
import com.key.win.user.dao.SysUserRoleDao;
import com.key.win.user.model.SysUserExcel;
import com.key.win.user.service.SysPermissionService;
import com.key.win.user.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 作者 owen
 * @version 创建时间：2017年11月12日 上午22:57:51
 */
@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private SysUserRoleDao userRoleDao;

    @Autowired(required = false)
    private TokenStore redisTokenStore;


    @Override
    @Transactional
    public void addSysUser(SysUser sysUser) throws ServiceException {
        try {
            String username = sysUser.getUsername();
            if (StringUtils.isBlank(username)) {
                throw new IllegalArgumentException("用户名不能为空");
            }

            if (ValidatorUtil.checkPhone(username)) {// 防止用手机号直接当用户名，手机号要发短信验证
                throw new IllegalArgumentException("用户名要包含英文字符");
            }

            if (username.contains("@")) {// 防止用邮箱直接当用户名，邮箱也要发送验证（暂未开发）
                throw new IllegalArgumentException("用户名不能包含@");
            }

            if (username.contains("|")) {
                throw new IllegalArgumentException("用户名不能包含|字符");
            }

            if (StringUtils.isBlank(sysUser.getPassword())) {
                throw new IllegalArgumentException("密码不能为空");
            }

            if (StringUtils.isBlank(sysUser.getNickname())) {
                sysUser.setNickname(username);
            }

            if (StringUtils.isBlank(sysUser.getType())) {
                sysUser.setType(UserType.APP.name());
            }

            SysUser persistenceUser = sysUserDao.findByUsername(sysUser.getUsername());
            if (persistenceUser != null && persistenceUser.getUsername() != null) {
                throw new IllegalArgumentException("用户名已存在");

            }

            sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
            sysUser.setEnabled(Boolean.TRUE);
            sysUserDao.save(sysUser);
            log.info("添加用户：{}", sysUser);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }


    @Override
    @Transactional
    public Result updateSysUser(SysUser sysUser) throws ServiceException {
        try {

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

            sysUserDao.updateByPrimaryKey(sysUser);
            log.info("修改用户：{}", sysUser);
            return Result.succeed(sysUser,"");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }


    @Override
    @Transactional
    public LoginAppUser findByUsername(String username) throws ServiceException {
        try {
            SysUser sysUser = sysUserDao.findUserByUsername(username);
            if (sysUser != null) {
                LoginAppUser loginAppUser = new LoginAppUser();
                BeanUtils.copyProperties(sysUser, loginAppUser);

                Set<SysRole> sysRoles = userRoleDao.findRolesByUserId(sysUser.getId());
                loginAppUser.setSysRoles(sysRoles);// 设置角色

                if (!CollectionUtils.isEmpty(sysRoles)) {
                    Set<String> roleIds = sysRoles.parallelStream().map(r -> r.getId()).collect(Collectors.toSet());
                    Set<SysPermission> sysPermissions = sysPermissionService.findByRoleIds(roleIds);
                    if (!CollectionUtils.isEmpty(sysPermissions)) {
                        Set<String> permissions = sysPermissions.parallelStream().map(p -> p.getPermission())
                                .collect(Collectors.toSet());

                        loginAppUser.setPermissions(permissions);// 设置权限集合
                    }

                }

                return loginAppUser;
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }

        return null;
    }


    @Override
    @Transactional
    public LoginAppUser findByMobile(String mobile) throws ServiceException {
        try {
            SysUser sysUser = sysUserDao.findUserByMobile(mobile);
            if (sysUser != null) {
                LoginAppUser loginAppUser = new LoginAppUser();
                BeanUtils.copyProperties(sysUser, loginAppUser);

                Set<SysRole> sysRoles = userRoleDao.findRolesByUserId(sysUser.getId());
                loginAppUser.setSysRoles(sysRoles);// 设置角色

                if (!CollectionUtils.isEmpty(sysRoles)) {
                    Set<String> roleIds = sysRoles.parallelStream().map(r -> r.getId()).collect(Collectors.toSet());
                    Set<SysPermission> sysPermissions = sysPermissionService.findByRoleIds(roleIds);
                    if (!CollectionUtils.isEmpty(sysPermissions)) {
                        Set<String> permissions = sysPermissions.parallelStream().map(p -> p.getPermission())
                                .collect(Collectors.toSet());

                        loginAppUser.setPermissions(permissions);// 设置权限集合
                    }

                }

                return loginAppUser;
            }

            return null;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result findById(String id) throws ServiceException {
        try {
            SysUser byId = sysUserDao.findById(id);
            if(byId!=null){
                List<String> userIds = new ArrayList<>();
                userIds.add(id);
                byId.setRoles(userRoleDao.findRolesByUserIds(userIds));
            }
            return Result.succeed(byId,"");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 给用户设置角色
     */

    @Override
    @Transactional
    public void setRoleToUser(String id, Set<String> roleIds) throws ServiceException {
        try {
            SysUser sysUser = sysUserDao.findById(id);
            if (sysUser == null) {

                throw new IllegalArgumentException("用户不存在");
            }

            userRoleDao.deleteUserRole(id, null);
            if (!CollectionUtils.isEmpty(roleIds)) {
                roleIds.forEach(roleId -> {
                    SysUserRole ur = new SysUserRole();
                    ur.setRoleId(roleId);
                    ur.setUserId(id);
                    // userRoleDao.saveUserRoles(id, roleId);
                    userRoleDao.insert(ur);
                });
            }

            log.info("修改用户：{}的角色，{}", sysUser.getUsername(), roleIds);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }


    @Override
    @Transactional
    public Result updatePassword(String id, String oldPassword, String newPassword) throws ServiceException {
        try {
            SysUser sysUser = sysUserDao.findById(id);
            if (StringUtils.isNoneBlank(oldPassword)) {
                if (!passwordEncoder.matches(oldPassword, sysUser.getPassword())) {
                    return Result.failed("旧密码错误");
                }
            }

            SysUser user = new SysUser();
            user.setId(id);
            user.setPassword(passwordEncoder.encode(newPassword));

            updateSysUser(user);
            log.info("修改密码：{}", user);
            return Result.succeed("修改成功");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PageResult<SysUser> findUsers(Map<String, Object> params) throws ServiceException {
        try {
            int total = sysUserDao.count(params);
            List<SysUser> list = Collections.emptyList();
            if (total > 0) {
                PageUtil.pageParamConver(params, true);
                list = sysUserDao.findList(params);

                List<String> userIds = list.stream().map(SysUser::getId).collect(Collectors.toList());

                List<SysRole> sysRoles = userRoleDao.findRolesByUserIds(userIds);

                list.forEach(u -> {
                    u.setRoles(sysRoles.stream().filter(r -> !ObjectUtils.notEqual(u.getId(), r.getUserId()))
                            .collect(Collectors.toList()));
                });
            }
            return PageResult.<SysUser>builder().data(list).code(0).count((long) total).build();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PageResult<SysUser> findSysUserByPaged(PageRequest<SysUser> t) {
        MybatiesPageServiceTemplate<SysUser, SysUser> page = new MybatiesPageServiceTemplate<SysUser, SysUser>(sysUserDao) {
            @Override
            protected AbstractWrapper constructWrapper(SysUser user) {
                LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<SysUser>();
                if (user != null && StringUtils.isNotBlank(user.getUsername())) {
                    SFunction<SysUser,String> s = SysUser::getUsername;
                    lqw.like(s, user.getUsername() == null ? "" : user.getUsername());
                }
                return lqw;
            }

            protected List getDefaultQueryOrder(SysUser user , String sortName){
                List<SFunction<SysUser,?>> list = new ArrayList<>();
                if("username".equals(sortName)){
                    list.add(SysUser::getUsername);
                }
                if("nickname".equals(sortName)){
                    list.add(SysUser::getNickname);
                }
                if("phone".equals(sortName)){
                    list.add(SysUser::getPhone);
                }
                if("sex".equals(sortName)){
                    list.add(SysUser::getSex);
                }
                if("createDate".equals(sortName)){
                    list.add(SysUser::getCreateDate);
                }
                if("enabled".equals(sortName)){
                    list.add(SysUser::getEnabled);
                }
                return list;
            }
        };
        return page.doPagingQuery(t);
    }

    @Override
    public Set<SysRole> findRolesByUserId(String userId) throws ServiceException {
        try {
            return userRoleDao.findRolesByUserId(userId);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result updateEnabled(Map<String, Object> params) throws ServiceException {
        try {
            String id = MapUtils.getString(params, "id");
            Boolean enabled = MapUtils.getBoolean(params, "enabled");

            SysUser appUser = sysUserDao.findById(id);
            if (appUser == null) {
                return Result.failed("用户不存在");
                //throw new IllegalArgumentException("用户不存在");
            }
            appUser.setEnabled(enabled);

            int i = sysUserDao.updateByPrimaryKey(appUser);
            log.info("修改用户：{}", appUser);

            return i > 0 ? Result.succeed(appUser, "更新成功") : Result.failed("更新失败");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }


    @Override
    @Transactional
    public Result saveOrUpdate(SysUser sysUser) throws ServiceException {
        try {
            String username = sysUser.getUsername();
            if (StringUtils.isBlank(username)) {
                //throw new IllegalArgumentException("用户名不能为空");
                return Result.failed("用户名不能为空");
            }

            if (ValidatorUtil.checkPhone(username)) {// 防止用手机号直接当用户名，手机号要发短信验证
                //throw new IllegalArgumentException("用户名要包含英文字符");
                return Result.failed("用户名要包含英文字符");
            }
            if (username.contains("@")) {// 防止用邮箱直接当用户名，邮箱也要发送验证（暂未开发）
                //throw new IllegalArgumentException("用户名不能包含@");
                return Result.failed("用户名不能包含@");
            }

            if (username.contains("|")) {
                //throw new IllegalArgumentException("用户名不能包含|字符");
                return Result.failed("用户名不能包含|字符");
            }

            if (StringUtils.isBlank(sysUser.getNickname())) {
                sysUser.setNickname(username);
            }

            if (StringUtils.isBlank(sysUser.getType())) {
                sysUser.setType(UserType.BACKEND.name());
            }

            if (!StringUtils.isBlank(sysUser.getPhone())) {

                if (!ValidatorUtil.checkPhone(sysUser.getPhone())) {// 防止用手机号直接当用户名，手机号要发短信验证
                    //throw new IllegalArgumentException("用户名要包含英文字符");
                    return Result.failed("手机号格式不正确");
                }

            }


            sysUser.setPassword(passwordEncoder.encode("123456"));
            sysUser.setEnabled(Boolean.TRUE);

            int i = 0;

            if (sysUser.getId() == null) {
                SysUser persistenceUser = sysUserDao.findByUsername(sysUser.getUsername());
                if (persistenceUser != null && persistenceUser.getUsername() != null) {
                    //throw new IllegalArgumentException("用户名已存在");
                    return Result.failed("用户名已存在");
                }
                i = sysUserDao.insert(sysUser);
            } else {
                SysUser byId = sysUserDao.findById(sysUser.getId());
                sysUser.setPassword(byId.getPassword());
                i = sysUserDao.updateByPrimaryKey(sysUser);
            }

            userRoleDao.deleteUserRole(sysUser.getId(), null);
            List<String> roleIds = Arrays.asList(sysUser.getRoleId().split(","));
            if (!CollectionUtils.isEmpty(roleIds)) {
                roleIds.forEach(roleId -> {
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setUserId(sysUser.getId());
                    sysUserRole.setRoleId(roleId);
                    userRoleDao.insert(sysUserRole);
                });
            }

            return i > 0 ? Result.succeed(sysUser, "操作成功") : Result.failed("操作失败");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<SysUserExcel> findAllUsers(Map<String, Object> params) throws ServiceException {
        try {
            List<SysUserExcel> sysUserExcels = new ArrayList<>();
            List<SysUser> list = sysUserDao.findList(params);

            for (SysUser sysUser : list) {
                SysUserExcel sysUserExcel = new SysUserExcel();
                BeanUtils.copyProperties(sysUser, sysUserExcel);
                sysUserExcels.add(sysUserExcel);
            }
            return sysUserExcels;
        } catch (BeansException e) {
            throw new ServiceException(e);
        }
    }


}