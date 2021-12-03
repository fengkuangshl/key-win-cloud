package com.key.win.log.dao;

import com.key.win.common.model.log.SysLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import javax.sql.DataSource;

/**
 * @author owen
 * @create 2017年7月2日
 * 保存日志
 * eureka-server配置不需要datasource,不会装配bean
 * blog: https://blog.51cto.com/13005375 
 * code: https://gitee.com/owenwangwen/key-win-cloud
 * 需要配置多数据源才可以支持
 */
@Mapper
@ConditionalOnBean(DataSource.class)
public interface LogDao {

	@Insert("insert into sys_log(username, module, params, remark, flag) values(#{username}, #{module}, #{params}, #{remark}, #{flag})")
	int save(SysLog log);

}
