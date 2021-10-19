package com.key.win.message.dao;

import com.key.win.message.model.MqTransactionBusinessLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MqTransactionBusinssLogMapper  {
	
	@Insert("insert into mq_trancation_business_log(id , transaction_id , description  ) values(#{id}, #{transactionId}, #{description})")
	int save(MqTransactionBusinessLog mqTransactionBusinessLog);
	
	
	@Select("select * from mq_trancation_business_log t where t.id = #{id}")
	MqTransactionBusinessLog findById(String id);

}