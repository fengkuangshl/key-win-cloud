package com.key.win.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.key.win.common.model.SysMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
/**
* @author 作者 owen 
* @version 创建时间：2017年11月13日 上午22:57:51
 * 菜单
 */
@Mapper
public interface SysMenuDao extends BaseMapper<SysMenu> {

	@Insert("insert into sys_menu(parent_id, name, url, path, css, sort, is_menu,hidden) "
			+ "values (#{parentId}, #{name}, #{url} , #{path} , #{css}, #{sort},#{isMenu},#{hidden})")
	int save(SysMenu menu);
	
	@Delete("delete from sys_menu where id = #{id}")
	int deleteByPrimaryKey(String id);

	@Delete("delete from sys_menu where parent_id = #{id}")
	int deleteByParentId(String id);
	
	int updateByPrimaryKey(SysMenu menu);

	@Select("select t.* from sys_menu t where t.id = #{id}")
	SysMenu findById(String id);
	
	int count(Map<String, Object> params);
	
	List<SysMenu> findList(Map<String, Object> params);
	
	
	
}
