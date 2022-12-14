package com.key.win.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.constant.UserType;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.model.SysPermission;
import com.key.win.common.model.SysRole;
import com.key.win.common.model.SysUser;
import com.key.win.common.web.OrderDir;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.common.util.PageUtil;
import com.key.win.common.util.SysUserUtil;
import com.key.win.common.util.ValidatorUtil;
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

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ?????? owen
 * @version ???????????????2017???11???12??? ??????22:57:51
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
                throw new IllegalArgumentException("?????????????????????");
            }

            if (ValidatorUtil.checkPhone(username)) {// ??????????????????????????????????????????????????????????????????
                throw new IllegalArgumentException("??????????????????????????????");
            }

            if (username.contains("@")) {// ??????????????????????????????????????????????????????????????????????????????
                throw new IllegalArgumentException("?????????????????????@");
            }

            if (username.contains("|")) {
                throw new IllegalArgumentException("?????????????????????|??????");
            }

            if (StringUtils.isBlank(sysUser.getPassword())) {
                throw new IllegalArgumentException("??????????????????");
            }

            if (StringUtils.isBlank(sysUser.getNickname())) {
                sysUser.setNickname(username);
            }

            if (StringUtils.isBlank(sysUser.getType())) {
                sysUser.setType(UserType.APP.name());
            }

            SysUser persistenceUser = sysUserDao.findByUsername(sysUser.getUsername());
            if (persistenceUser != null && persistenceUser.getUsername() != null) {
                throw new IllegalArgumentException("??????????????????");

            }

            sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
            sysUser.setEnabled(Boolean.TRUE);
            sysUser.setCreateTime(new Date());
            sysUser.setUpdateTime(sysUser.getCreateTime());
            sysUserDao.save(sysUser);
            log.info("???????????????{}", sysUser);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }


    @Override
    @Transactional
    public Result updateSysUser(SysUser sysUser) throws ServiceException {
        try {
            sysUser.setUpdateTime(new Date());

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
            log.info("???????????????{}", sysUser);
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
                loginAppUser.setSysRoles(sysRoles);// ????????????

                if (!CollectionUtils.isEmpty(sysRoles)) {
                    Set<Long> roleIds = sysRoles.parallelStream().map(r -> r.getId()).collect(Collectors.toSet());
                    Set<SysPermission> sysPermissions = sysPermissionService.findByRoleIds(roleIds);
                    if (!CollectionUtils.isEmpty(sysPermissions)) {
                        Set<String> permissions = sysPermissions.parallelStream().map(p -> p.getPermission())
                                .collect(Collectors.toSet());

                        loginAppUser.setPermissions(permissions);// ??????????????????
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
                loginAppUser.setSysRoles(sysRoles);// ????????????

                if (!CollectionUtils.isEmpty(sysRoles)) {
                    Set<Long> roleIds = sysRoles.parallelStream().map(r -> r.getId()).collect(Collectors.toSet());
                    Set<SysPermission> sysPermissions = sysPermissionService.findByRoleIds(roleIds);
                    if (!CollectionUtils.isEmpty(sysPermissions)) {
                        Set<String> permissions = sysPermissions.parallelStream().map(p -> p.getPermission())
                                .collect(Collectors.toSet());

                        loginAppUser.setPermissions(permissions);// ??????????????????
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
    public Result findById(Long id) throws ServiceException {
        try {
            SysUser byId = sysUserDao.findById(id);
            if(byId!=null){
                List<Long> userIds = new ArrayList<>();
                userIds.add(id);
                byId.setRoles(userRoleDao.findRolesByUserIds(userIds));
            }
            return Result.succeed(byId,"");
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * ?????????????????????
     */

    @Override
    @Transactional
    public void setRoleToUser(Long id, Set<Long> roleIds) throws ServiceException {
        try {
            SysUser sysUser = sysUserDao.findById(id);
            if (sysUser == null) {

                throw new IllegalArgumentException("???????????????");
            }

            userRoleDao.deleteUserRole(id, null);
            if (!CollectionUtils.isEmpty(roleIds)) {
                roleIds.forEach(roleId -> {
                    userRoleDao.saveUserRoles(id, roleId);
                });
            }

            log.info("???????????????{}????????????{}", sysUser.getUsername(), roleIds);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }


    @Override
    @Transactional
    public Result updatePassword(Long id, String oldPassword, String newPassword) throws ServiceException {
        try {
            SysUser sysUser = sysUserDao.findById(id);
            if (StringUtils.isNoneBlank(oldPassword)) {
                if (!passwordEncoder.matches(oldPassword, sysUser.getPassword())) {
                    return Result.failed("???????????????");
                }
            }

            SysUser user = new SysUser();
            user.setId(id);
            user.setPassword(passwordEncoder.encode(newPassword));

            updateSysUser(user);
            log.info("???????????????{}", user);
            return Result.succeed("????????????");
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

                List<Long> userIds = list.stream().map(SysUser::getId).collect(Collectors.toList());

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
                if("createTime".equals(sortName)){
                    list.add(SysUser::getCreateTime);
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
    public Set<SysRole> findRolesByUserId(Long userId) throws ServiceException {
        try {
            return userRoleDao.findRolesByUserId(userId);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Result updateEnabled(Map<String, Object> params) throws ServiceException {
        try {
            Long id = MapUtils.getLong(params, "id");
            Boolean enabled = MapUtils.getBoolean(params, "enabled");

            SysUser appUser = sysUserDao.findById(id);
            if (appUser == null) {
                return Result.failed("???????????????");
                //throw new IllegalArgumentException("???????????????");
            }
            appUser.setEnabled(enabled);
            appUser.setUpdateTime(new Date());

            int i = sysUserDao.updateByPrimaryKey(appUser);
            log.info("???????????????{}", appUser);

            return i > 0 ? Result.succeed(appUser, "????????????") : Result.failed("????????????");
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
                //throw new IllegalArgumentException("?????????????????????");
                return Result.failed("?????????????????????");
            }

            if (ValidatorUtil.checkPhone(username)) {// ??????????????????????????????????????????????????????????????????
                //throw new IllegalArgumentException("??????????????????????????????");
                return Result.failed("??????????????????????????????");
            }
            if (username.contains("@")) {// ??????????????????????????????????????????????????????????????????????????????
                //throw new IllegalArgumentException("?????????????????????@");
                return Result.failed("?????????????????????@");
            }

            if (username.contains("|")) {
                //throw new IllegalArgumentException("?????????????????????|??????");
                return Result.failed("?????????????????????|??????");
            }

            if (StringUtils.isBlank(sysUser.getNickname())) {
                sysUser.setNickname(username);
            }

            if (StringUtils.isBlank(sysUser.getType())) {
                sysUser.setType(UserType.BACKEND.name());
            }

            if (!StringUtils.isBlank(sysUser.getPhone())) {

                if (!ValidatorUtil.checkPhone(sysUser.getPhone())) {// ??????????????????????????????????????????????????????????????????
                    //throw new IllegalArgumentException("??????????????????????????????");
                    return Result.failed("????????????????????????");
                }

            }


            //sysUser.setPassword(passwordEncoder.encode("123456"));
            sysUser.setEnabled(Boolean.TRUE);
            sysUser.setCreateTime(new Date());

            int i = 0;

            if (sysUser.getId() == null) {
                SysUser persistenceUser = sysUserDao.findByUsername(sysUser.getUsername());
                if (persistenceUser != null && persistenceUser.getUsername() != null) {
                    //throw new IllegalArgumentException("??????????????????");
                    return Result.failed("??????????????????");
                }
                sysUser.setUpdateTime(sysUser.getCreateTime());
                i = sysUserDao.insert(sysUser);
            } else {
                sysUser.setUpdateTime(new Date());
                SysUser byId = sysUserDao.findById(sysUser.getId());
                sysUser.setPassword(byId.getPassword());
                i = sysUserDao.updateByPrimaryKey(sysUser);
            }

            userRoleDao.deleteUserRole(sysUser.getId(), null);
            List roleIds = Arrays.asList(sysUser.getRoleId().split(","));
            if (!CollectionUtils.isEmpty(roleIds)) {
                roleIds.forEach(roleId -> {
                    userRoleDao.saveUserRoles(sysUser.getId(), Long.parseLong(roleId.toString()));
                });
            }

            return i > 0 ? Result.succeed(sysUser, "????????????") : Result.failed("????????????");
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