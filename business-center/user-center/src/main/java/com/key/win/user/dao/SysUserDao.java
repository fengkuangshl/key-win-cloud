package com.key.win.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.key.win.common.model.system.SysUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
* @author 作者 owen 
* @version 创建时间：2017年11月12日 上午22:57:51
 * 用户管理
 */
@Mapper
public interface SysUserDao  extends BaseMapper<SysUser> {

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into sys_user(username, password, nick_name , head_img_url, phone, sex, enabled, type) "
			+ "values(#{username}, #{password}, #{nickname}, #{headImgUrl}, #{phone}, #{sex}, #{enabled}, #{type})")
	int save(SysUser sysUser);

	int updateByPrimaryKey(SysUser sysUser);

	@Select("select t.* from sys_user t where t.username = #{username} and t.enable_flag = 1")
	SysUser findByUsername(String username);

	@Select("select t.* from sys_user t where t.id = #{id} and t.enable_flag = 1")
	SysUser findById(String id);

	@Select("select *  from sys_user u   where u.username = #{username} and u.enable_flag = 1")
	SysUser findUserByUsername(String username);

	@Select("select u.*  from sys_user u   where u.phone = #{mobile} and u.enable_flag = 1")
	SysUser findUserByMobile(String mobile);
	
	int count(Map<String, Object> params);

	List<SysUser> findList(Map<String, Object> params);
	
	
	
	

}
