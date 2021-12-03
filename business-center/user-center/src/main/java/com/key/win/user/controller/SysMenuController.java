package com.key.win.user.controller;

import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.exception.controller.ControllerException;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.model.system.SysMenu;
import com.key.win.common.model.system.SysRole;
import com.key.win.common.util.SysUserUtil;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.user.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "MENU API")
@RequestMapping("/menus")
public class SysMenuController {

    @Autowired
    private SysMenuService menuService;

    @PreAuthorize("hasAuthority('menus:get/menus/{id}')")
    @GetMapping("/get/{id}")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result findSysMenuById(@PathVariable String id) throws ControllerException {
        try {
            SysMenu byId = menuService.findById(id);
            return Result.succeed(byId, "");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }


    /**
     * 删除菜单
     *
     * @param id
     * @throws ControllerException
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除菜单")
    @PreAuthorize("hasAuthority('menu:delete/menus/{id}')")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result delete(@PathVariable String id) throws ControllerException {
        try {
            menuService.removeById(id);
            return Result.succeed("操作成功");
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

    @GetMapping("/{roleId}/menus")
    @ApiOperation(value = "根据roleId获取对应的菜单")
    @PreAuthorize("hasAuthority('menu:get/menus/{roleId}/menus')")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public List<Map<String, Object>> findMenusByRoleId(@PathVariable String roleId) throws ControllerException {

        try {
            Set<String> roleIds = new HashSet<String>();
            // 初始化角色
            roleIds.add(roleId);
            List<SysMenu> roleMenus = menuService.findByRoles(roleIds); // 获取该角色对应的菜单
            List<SysMenu> allMenus = menuService.list(); // 全部的菜单列表
            List<Map<String, Object>> authTrees = new ArrayList<>();
            Map<String, SysMenu> roleMenusMap = roleMenus.stream()
                    .collect(Collectors.toMap(SysMenu::getId, SysMenu -> SysMenu));
            for (SysMenu sysMenu : allMenus) {
                Map<String, Object> authTree = new HashMap<>();
                authTree.put("id", sysMenu.getId());
                authTree.put("name", sysMenu.getName());
                authTree.put("pId", sysMenu.getParentId());
                authTree.put("open", true);
                authTree.put("checked", false);
                if (roleMenusMap.get(sysMenu.getId()) != null) {
                    authTree.put("checked", true);
                }
                authTrees.add(authTree);
            }
            return authTrees;
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    @GetMapping("/{roleId}")
    @ApiOperation(value = "根据roleId获取对应的菜单")
    @PreAuthorize("hasAuthority('menu:get/menus/{roleId}')")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result getMenusByRoleId(@PathVariable String roleId) throws ControllerException {
        return Result.succeed(this.findMenusByRoleId(roleId), "");
    }

    /**
     * 给角色分配菜单
     *
     * @throws ControllerException
     */
    @PostMapping("/granted")
    @ApiOperation(value = "角色分配菜单")
    @PreAuthorize("hasAuthority('menu:post/menus/granted')")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result setMenuToRole(@RequestBody SysMenu sysMenu) throws ControllerException {
        try {
            menuService.setMenuToRole(sysMenu.getRoleId(), sysMenu.getMenuIds());
            return Result.succeed("操作成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    @GetMapping("/findAlls")
    @ApiOperation(value = "查询所有菜单")
    @PreAuthorize("hasAuthority('menu:get/menus/findAlls')")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public PageResult<SysMenu> findAlls() throws ControllerException {

        try {
            List<SysMenu> list = menuService.list();
            return PageResult.<SysMenu>builder().data(list).code(0).count((long) list.size()).build();
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }


    @GetMapping("/getAll")
    @ApiOperation(value = "查询所有菜单")
    @PreAuthorize("hasAuthority('menu:get/menus/findAlls')")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result getAll() throws ControllerException {
        List<SysMenu> all = menuService.list();
        return Result.succeed(all, "");
    }

    @GetMapping("/findOnes")
    @ApiOperation(value = "获取菜单以及顶级菜单")
    @PreAuthorize("hasAuthority('menu:get/menus/findOnes')")
    public PageResult<SysMenu> findOnes() throws ControllerException {
        try {
            List<SysMenu> list = menuService.findOnes();
            return PageResult.<SysMenu>builder().data(list).code(0).count((long) list.size()).build();
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    @GetMapping("/getOnes")
    @ApiOperation(value = "获取菜单以及顶级菜单")
    @PreAuthorize("hasAuthority('menu:get/menus/findOnes')")
    public Result getOnes() throws ControllerException {
        List<SysMenu> list = menuService.findOnes();
        return Result.succeed(list, "");

    }

    /**
     * 添加菜单 或者 更新
     *
     * @param menu
     * @return
     * @throws ControllerException
     */
    @PostMapping("saveOrUpdate")
    @ApiOperation(value = "新增菜单")
    @PreAuthorize("hasAnyAuthority('menu:post/menus','menu:put/menus')")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result saveOrUpdate(@RequestBody SysMenu menu) throws ControllerException {
        try {
            menuService.saveOrUpdate(menu);
            return Result.succeed("操作成功");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 当前登录用户的菜单
     *
     * @return
     * @throws ControllerException
     */
    @GetMapping("/current")
    @ApiOperation(value = "查询当前用户菜单")
    @PreAuthorize("hasAuthority('menu:get/menus/current')")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public List<SysMenu> findMyMenu() throws ControllerException {
        try {
            LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
            Set<SysRole> roles = loginAppUser.getSysRoles();
            if (CollectionUtils.isEmpty(roles)) {
                return Collections.emptyList();
            }
            List<SysMenu> menus = menuService
                    .findByRoles(roles.parallelStream().map(SysRole::getId).collect(Collectors.toSet()));
            return treeBuilder(menus);
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    @GetMapping("/current/menus")
    @ApiOperation(value = "查询当前用户菜单")
    @PreAuthorize("hasAuthority('menu:get/menus/current')")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    public Result findMyMenus() throws ControllerException {
        try {
            LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
            Set<SysRole> roles = loginAppUser.getSysRoles();
            if (CollectionUtils.isEmpty(roles)) {
                return Result.succeed(Collections.emptyList(), "");
            }
            List<SysMenu> menus = menuService
                    .findByRoles(roles.parallelStream().map(SysRole::getId).collect(Collectors.toSet()));
            return Result.succeed(treeBuilder(menus), "");
        } catch (ServiceException e) {
            throw new ControllerException(e);
        }
    }

    /**
     * 两层循环实现建树
     *
     * @param sysMenus
     * @return
     * @throws ControllerException
     */
    public static List<SysMenu> treeBuilder(List<SysMenu> sysMenus) throws ControllerException {
        try {
            List<SysMenu> menus = new ArrayList<SysMenu>();
            for (SysMenu sysMenu : sysMenus) {
                if (ObjectUtils.equals("-1", sysMenu.getParentId())) {
                    menus.add(sysMenu);
                }
                for (SysMenu menu : sysMenus) {
                    if (menu.getParentId().equals(sysMenu.getId())) {
                        if (sysMenu.getSubMenus() == null) {
                            sysMenu.setSubMenus(new ArrayList<>());
                        }
                        sysMenu.getSubMenus().add(menu);
                    }
                }
            }
            return menus;
        } catch (Exception e) {
            throw new ControllerException(e);
        }
    }

    @ApiOperation("分页")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    @PostMapping("/getMenuByPaged")
    public PageResult<SysMenu> getSysRoleByPaged(@RequestBody PageRequest<SysMenu> t) {
        return menuService.findSysRoleByPaged(t);
    }
}
