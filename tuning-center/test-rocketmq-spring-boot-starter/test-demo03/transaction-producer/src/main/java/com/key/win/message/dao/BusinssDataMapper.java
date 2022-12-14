package com.key.win.message.dao;

import com.key.win.message.model.BusinessData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
/**
 * 模拟业务数据入库
 * 业务表business_data
 */
@Mapper
public interface BusinssDataMapper  {
	
	@Insert("insert into business_data(id , username , sex ) values(#{id}, #{username}, #{sex})")
	int save(BusinessData businessData);
	
	
	@Select("select * from business_data t where t.id = #{id}")
	BusinessData findById(String id);

}