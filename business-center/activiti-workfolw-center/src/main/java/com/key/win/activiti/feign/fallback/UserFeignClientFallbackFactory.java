package com.key.win.activiti.feign.fallback;

import com.key.win.activiti.feign.UserFeignClient;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.model.SysUser;
import com.key.win.common.web.PageResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class UserFeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {
	@Override
	public UserFeignClient create(Throwable throwable) {
		return new UserFeignClient() {

			@Override
			public LoginAppUser findByUsername(String username) {
				log.error("通过用户名查询用户异常:{}", username, throwable);
				return new LoginAppUser() ;
			}

			@Override
			public LoginAppUser findByMobile(String mobile) {
				log.error("通过手机号查询用户异常:{}", mobile, throwable);
				return new LoginAppUser();
			}

			@Override
			public PageResult<SysUser> findUsers(Map<String, Object> params) {
				log.error("查询用户列表异常:{}");
				return null;
			}

		};
	}
}
