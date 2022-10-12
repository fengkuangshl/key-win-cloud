package com.key.win.user.controller;


import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.exception.business.BizException;
import com.key.win.common.exception.controller.ControllerException;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.model.system.SysUser;
import com.key.win.common.util.SysUserUtil;
import com.key.win.common.util.ValidatorUtil;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.user.service.SysUserService;
import com.key.win.user.vo.SysUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/*")
@Api("User相关的api")
public class SysUserCtrl {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final static String AUTHORITY_PREFIX = "system::user::SysUser::";
    @Autowired
    private SysUserService sysUserService;

    @GetMapping(value = "/login", params = "username")
    @ApiOperation(value = "根据用户名查询用户")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public LoginAppUser findByUsername(String username) throws ControllerException {
        try {
            return sysUserService.findByUsername(username);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    @GetMapping(value = "/mobile", params = "mobile")
    @ApiOperation(value = "根据用户名查询手机号")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public LoginAppUser findByMobile(String mobile) throws ControllerException {
        try {
            return sysUserService.findByMobile(mobile);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }


    @PostMapping("/findSysUserByPaged")
    @ApiOperation(value = "User分页")
    @LogAnnotation(module = "system", recordRequestParam = false)
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "QUERY::PAGED')")
    public PageResult<SysUser> findSysUserByPaged(@RequestBody PageRequest<SysUser> pageRequest) {
        return sysUserService.findSysUserByPaged(pageRequest);
    }


    @GetMapping("/getUserAll")
    @ApiOperation(value = "获取所有用户")
    @LogAnnotation(module = "system", recordRequestParam = false)
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "QUERY::LIST')")
    public Result getUserAll() {
        return Result.succeed(sysUserService.list());
    }


    @PostMapping("/register")
    @ApiOperation(value = "用户注册")
    @LogAnnotation(module = "system", recordRequestParam = true)
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "ADD')")
    public Result saveSysUser(@RequestBody SysUserVo sysUser) {
        if (StringUtils.isBlank(sysUser.getNickname())) {
            logger.error("用户名为空!");
            throw new BizException("用户名为空!");
        }

        if (ValidatorUtil.checkPhone(sysUser.getNickname())) {// 防止用手机号直接当用户名，手机号要发短信验证
            throw new BizException("用户名要包含英文字符");
        }

        if (sysUser.getNickname().contains("@")) {// 防止用邮箱直接当用户名，邮箱也要发送验证（暂未开发）
            throw new BizException("用户名不能包含@");
        }

        if (sysUser.getNickname().contains("|")) {
            throw new BizException("用户名不能包含|字符");
        }

        if (StringUtils.isBlank(sysUser.getPassword())) {
            throw new BizException("密码不能为空");
        }
        if (StringUtils.isBlank(sysUser.getPassword())) {
            logger.error("密码为空!");
            throw new BizException("密码为空");
        }
        boolean b = sysUserService.saveSysUser(sysUser);
        return Result.result(b);
    }

    @PostMapping("/updateSysUser")
    @ApiOperation(value = "用户更新")
    @LogAnnotation(module = "system", recordRequestParam = true)
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "MODIFY')")
    public Result updateSysUser(@RequestBody SysUserVo sysUser) {
        boolean b = sysUserService.updateSysUser(sysUser);
        return Result.result(b);
    }

    @GetMapping("/get/{id}")
    @ApiOperation(value = "获取用户")
    @LogAnnotation(module = "system", recordRequestParam = true)
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "QUERY::ID')")
    public Result get(@PathVariable Long id) {
        SysUser byId = sysUserService.getById(id);
        return Result.succeed(byId);
    }

    @GetMapping("/getUserFullById/{id}")
    @ApiOperation(value = "获取用户及自己的机构、设备、角色信息")
    @LogAnnotation(module = "system", recordRequestParam = false)
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "QUERY::ID')")
    public Result getUserFullById(@PathVariable Long id) {
        SysUser byId = sysUserService.getUserFullById(id);
        return Result.succeed(byId);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除")
    @LogAnnotation(module = "system", recordRequestParam = true)
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "DELETE')")
    public Result delete(@PathVariable Long id) {
        boolean b = sysUserService.deleteById(id);
        return Result.result(b);
    }

    @GetMapping("/current")
    @ApiOperation(value = "获取当前登录用户")
    @LogAnnotation(module = "system", recordRequestParam = false)
    public Result getLoginAppUser() {
        return Result.succeed(SysUserUtil.getLoginAppUser());
    }

    @GetMapping("/resetPassword/{id}")
    @ApiOperation(value = "重置登录用户密码")
    @LogAnnotation(module = "system", recordRequestParam = true)
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "USER::RESET::PASSWORD')")
    public Result resetPassword(@PathVariable Long id) {
        if (id == null) {
            logger.error("id为空！");
            throw new BizException("id为空！");
        }
        boolean b = sysUserService.resetPassword(id);
        return Result.result(b);
    }

    @PostMapping("/modifyMyPassword")
    @ApiOperation(value = "修改自己的密码")
    @LogAnnotation(module = "system", recordRequestParam = true)
    public Result modifyMyPassword(@RequestBody SysUserVo sysUser) {
        LoginAppUser loginApp = SysUserUtil.getLoginAppUser();
        if (sysUser.getId() == null || sysUser.getId() != loginApp.getId()) {
            logger.error("非法操作！");
            throw new BizException("非法操作！");
        }
        if (StringUtils.isBlank(sysUser.getPassword())) {
            logger.error("原密码为空！");
            throw new BizException("原密码为空！");
        }
        if (StringUtils.isBlank(sysUser.getNewPassword())) {
            logger.error("新密码为空！");
            throw new BizException("新密码为空！");
        }
        if (sysUser.getPassword().equals(sysUser.getNewPassword())) {
            logger.error("新密码不能和原密码一样！");
            throw new BizException("新密码不能和原密码一样！");
        }
        sysUser.setId(loginApp.getId());
        boolean b = sysUserService.modifyPassword(sysUser);
        return Result.result(b);
    }

    @PostMapping("/granted")
    @ApiOperation(value = "组分配用户")
    @LogAnnotation(module = "system", recordRequestParam = true)
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "USER::GRANTED::ROLE')")
    public Result setUserToGroup(@RequestBody SysUserVo sysUser) {
        if (CollectionUtils.isEmpty(sysUser.getUserIds())) {
            logger.error("授权用户为空！");
            throw new BizException("授权用户为空！");
        }
        if (sysUser.getGroupId() == null) {
            logger.error("组信息为空！");
            throw new BizException("组信息为空！");
        }
        sysUserService.setUserToGroup(sysUser.getGroupId(), sysUser.getUserIds());
        return Result.succeed("角色分配菜单成功");
    }

    @GetMapping("/getLoginApp")
    @ApiOperation(value = "获所当前用户")
    @LogAnnotation(module = "system", recordRequestParam = false)
    public Result getLoginApp(){
        Map<String, Object> userInfo = new HashMap<>();
        LoginAppUser loginApp = SysUserUtil.getLoginAppUser();
        userInfo.put("user", loginApp);
        userInfo.put("permissions", loginApp.getPermissions());
        return Result.succeed(userInfo);
    }

    /**
     * 新增or更新
     *
     * @param sysUser
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @LogAnnotation(module = "system", recordRequestParam = false)
    @PreAuthorize("hasAnyAuthority('" + AUTHORITY_PREFIX + "MODIFY','" + AUTHORITY_PREFIX + "ADD')")
    public Result saveOrUpdate(@RequestBody SysUserVo sysUser) {
        if (sysUser != null) {
            if (sysUser.getId() != null) {
                boolean b = sysUserService.updateSysUser(sysUser);
                return Result.result(b);
            } else {

                boolean b = sysUserService.saveSysUser(sysUser);
                return Result.result(b);
            }
        }

        return Result.failed("用户信息为空！");
    }

    /**
     * 修改用户状态
     *
     * @return
     * @author
     */
    @ApiOperation(value = "修改用户状态")
    @PostMapping("/updateEnabled")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('" + AUTHORITY_PREFIX + "UPDATE::ENABLED')")
    public Result updateEnabled(@RequestBody SysUser sysUser) {
        boolean b = sysUserService.updateEnabled(sysUser);
        return Result.result(b);
    }
}
