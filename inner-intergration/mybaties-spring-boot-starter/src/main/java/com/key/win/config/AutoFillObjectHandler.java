package com.key.win.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.util.SysUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

import static org.springframework.security.config.Elements.ANONYMOUS;

/**
 * 自动填充类，如果是采用mybatis作为数据库持久操作层，则会采用该拦截器进行公共自动填充 如果是匿名的方式登录，即：无法获取当前认证信息的，会将
 * 创建人/更新人 赋值为 匿名用户 即:ANONYMOUS
 * 
 * @author Mr.Chen
 *
 */
@Slf4j
@Configuration
public class AutoFillObjectHandler implements MetaObjectHandler {



	public AutoFillObjectHandler() {
		log.info("Mybatis 自动填充装置已启动....");
	}

	@Override
	public void insertFill(MetaObject metaObject) {
		log.info("mybatis数据持久....添加自动填充....");
		LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
        this.strictInsertFill(metaObject,"createDate",LocalDateTime.class, LocalDateTime.now());
		if (loginAppUser == null) {
		    this.strictInsertFill(metaObject,"createUserId",String.class,ANONYMOUS);
		    this.strictInsertFill(metaObject,"createUserName",String.class, ANONYMOUS);
		} else {
            this.strictInsertFill(metaObject,"createUserId",String.class,String.valueOf(loginAppUser.getId()));
            this.strictInsertFill(metaObject,"createUserName",String.class, loginAppUser.getUsername());
		}
		setFieldValByName("version", 0, metaObject);
		log.info("mybatis数据持久....填充完毕....");
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		log.info("mybatis数据持久....更新前自动填充....");
		LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
        this.strictUpdateFill(metaObject,"updateDate",LocalDateTime.class, LocalDateTime.now());
		if (loginAppUser == null) {
            this.strictUpdateFill(metaObject,"updateUserId",String.class,ANONYMOUS);
            this.strictUpdateFill(metaObject,"updateUserName",String.class, ANONYMOUS);
		} else {
            this.strictUpdateFill(metaObject,"updateUserId",String.class,String.valueOf(loginAppUser.getId()));
            this.strictUpdateFill(metaObject,"updateUserName",String.class, loginAppUser.getUsername());
		}
		log.info("mybatis数据持久....更新填充完毕....");
	}

}