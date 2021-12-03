package com.key.win.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.key.win.common.model.SysRole;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
* @author 作者 owen 
* @version 创建时间：2017年11月12日 上午22:57:51
 * 角色
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRole>{

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into sys_role(code, name) values(#{code}, #{name})")
	int save(SysRole sysRole);

	
	@Delete("delete from sys_role where id = #{id}")
	int deleteByPrimaryKey(String id);
	
	@Update("update sys_role t set t.name = #{name}  where t.id = #{id}")
	int updateByPrimaryKey(SysRole sysRole);

	@Select("select t.* from sys_role t where t.id = #{id}")
	SysRole findById(String id);

	@Select("select t.* from sys_role t where t.code = #{code} and t.enable_flag = 1")
	SysRole findByCode(String code);

	int count(Map<String, Object> params);

	List<SysRole> findList(Map<String, Object> params);

}
