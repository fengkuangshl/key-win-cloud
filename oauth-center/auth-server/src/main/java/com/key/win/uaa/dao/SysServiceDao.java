package com.key.win.uaa.dao;

import com.key.win.common.model.SysService;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: [gitgeek]
 * @Date: [2018-08-23 12:11]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.zzg]
 */
@Mapper
public interface SysServiceDao {
	
    @Insert("insert into sys_service(parent_id,name,path,sort,is_service) values (#{parentId}, #{name}, #{path},#{sort},#{isService})")
    int save(SysService service);

    int updateByPrimaryKey(SysService service);

    @Select("select t.* from sys_service t where t.id = #{id} and t.enable_flag = 1")
    SysService findById(String id);

    @Delete("delete from sys_service where id = #{id}  and t.enable_flag = 1")
    int delete(String id);

    @Delete("delete from sys_service where parent_id = #{id}  and t.enable_flag = 1")
    int deleteByParentId(String id);

    @Select("select t.*  from sys_service t where  t.enable_flag = 1 order by t.sort")
    List<SysService> findAll();

    @Select("select t.*  from sys_service t where t.is_service = 1  and t.enable_flag = 1 order by t.sort")
    List<SysService> findOnes();

}
