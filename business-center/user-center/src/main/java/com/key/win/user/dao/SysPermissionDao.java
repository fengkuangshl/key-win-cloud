package com.key.win.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.key.win.common.model.SysPermission;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
* @author 作者 owen 
* @version 创建时间：2017年11月12日 上午22:57:51
 * 权限
 */
@Mapper
public interface SysPermissionDao  extends BaseMapper<SysPermission> {

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into sys_permission(permission, name) values(#{permission}, #{name})")
	int save(SysPermission sysPermission);

	@Delete("delete from sys_permission where id = #{id}")
	int deleteByPrimaryKey(String id);
	
	@Update("update sys_permission t set t.name = #{name}, t.permission = #{permission} where t.id = #{id}")
	int updateByPrimaryKey(SysPermission sysPermission);

	
	@Select("select t.* from sys_permission t where t.id = #{id}")
	SysPermission findById(String id);

	@Select("select t.* from sys_permission t where t.permission = #{permission}")
	SysPermission findByPermission(String permission);

	int count(Map<String, Object> params);

	List<SysPermission> findList(Map<String, Object> params);

}
