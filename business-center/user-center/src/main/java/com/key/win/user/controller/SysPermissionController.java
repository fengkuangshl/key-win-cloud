package com.key.win.user.controller;

import com.key.win.common.exception.controller.ControllerException;
import com.key.win.common.exception.service.ServiceException;
import com.key.win.common.model.system.SysPermission;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.common.web.Result;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.user.service.SysPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


/**
* @author 作者 owen 
* @version 创建时间：2017年11月12日 上午22:57:51
* 权限管理
 */
@RestController
@Api(tags = "PERMISSION API")
public class SysPermissionController {

	@Autowired
	private SysPermissionService sysPermissionService;

	/**
	 * 删除权限标识
	 * 参考 /permissions/1
	 * @param id
	 * @throws ControllerException
	 */
	@DeleteMapping("/permissions/{id}")
	@ApiOperation(value = "后台管理删除权限标识")
	@PreAuthorize("hasAuthority('permission:delete/permissions/{id}')")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public Result delete(@PathVariable String id) throws ControllerException {

		try {
			sysPermissionService.delete(id);
			return  Result.succeed("操作成功");
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}

	}


	/**
	 * 查询所有的权限标识
	 * 参考 ?start=0&length=10
	 * @return
	 * @throws ControllerException 
	 */
	@GetMapping("/permissions")
	@ApiOperation(value = "后台管理查询所有的权限标识")
	@PreAuthorize("hasAuthority('permission:get/permissions')")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
        @ApiImplicitParam(name = "limit",value = "分页结束位置", required = true, dataType = "Integer")
    })
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public PageResult<SysPermission> findPermissions(@RequestParam Map<String, Object> params) throws ControllerException {
		
		try {
			return sysPermissionService.findPermissions(params);
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}

	/**
	 * 权限新增或者更新
	 * @param sysPermission
	 * @return
	 * @throws ControllerException 
	 */
	@PostMapping("/permissions/saveOrUpdate")
	@PreAuthorize("hasAnyAuthority('permission:put/permissions','permission:post/permissions')")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public Result saveOrUpdate(@RequestBody SysPermission sysPermission) throws ControllerException {
		try{
			if (sysPermission.getId()!=null){
				sysPermissionService.update(sysPermission);
			}else {
				sysPermissionService.save(sysPermission);
			}
			return Result.succeed("操作成功");
		}catch (ServiceException e){
			throw new ControllerException(e);
		}
	}

	@ApiOperation(value = "根据roleId获取对应的权限")
	@GetMapping("/permissions/{roleId}/permissions")
	@PreAuthorize("hasAuthority('permission:get/permissions/{roleId}/permissions')")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public List<Map<String, Object>> findAuthByRoleId(@PathVariable String roleId) throws ControllerException {
		
		try {
			List<Map<String, Object>> authTrees = new ArrayList<>();
			Set<String> roleIds = new HashSet<String>();
			//初始化角色
			roleIds.add(roleId);
			Set<SysPermission> roleAuths = sysPermissionService.findByRoleIds(roleIds);//根据roleId获取对应的权限
			PageResult<SysPermission> allAuths = sysPermissionService.findPermissions(null);//根据roleId获取对应的权限


			Map<String,SysPermission> roleAuthsMap = roleAuths.stream().collect(Collectors.toMap(SysPermission::getId,SysPermission->SysPermission));

			for (SysPermission sysPermission : allAuths.getData() ){
				Map<String, Object> authTree = new HashMap<>();
				authTree.put("id",sysPermission.getId() + "");
				authTree.put("name",sysPermission.getName());
				authTree.put("open",true);
				authTree.put("checked", false);
				if (roleAuthsMap.get(sysPermission.getId())!=null){
					authTree.put("checked", true);
				}
				authTrees.add(authTree);
			}

			return authTrees;
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}

    @ApiOperation(value = "根据roleId获取对应的权限")
    @GetMapping("/permissions/{roleId}")
    @PreAuthorize("hasAuthority('permission:get/permissions/{roleId}')")
    @LogAnnotation(module="user-center",recordRequestParam=false)
    public Result getAuthByRoleId(@PathVariable String roleId) throws ControllerException {
       return Result.succeed(this.findAuthByRoleId(roleId),"");
    }

	/**
	 * 给角色分配权限
	 * @throws ControllerException 
	 */
	@ApiOperation(value = "角色分配权限")
	@PostMapping("/permissions/granted")
	@PreAuthorize("hasAuthority('permission:post/permissions/granted')")
	@LogAnnotation(module="user-center",recordRequestParam=false)
	public Result setPermissionToRole(@RequestBody SysPermission sysPermission) throws ControllerException {
		try {
			sysPermissionService.setPermissionToRole(sysPermission.getRoleId(),sysPermission.getAuthIds());
			return Result.succeed("操作成功");
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
	}

    @ApiOperation("分页")
    @LogAnnotation(module = "user-center", recordRequestParam = false)
    @PostMapping("/permissions/getSysPermissionByPaged")
    public PageResult<SysPermission> getSysPermissionByPaged(@RequestBody PageRequest<SysPermission> t) {
        return sysPermissionService.getSysPermissionByPaged(t);
    }

}
