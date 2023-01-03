package com.key.win.activiti.feign.fallback;

import com.key.win.activiti.feign.UserFeignClient;
import com.key.win.common.model.system.SysUser;
import com.key.win.common.web.Result;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserFeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {
	@Override
	public UserFeignClient create(Throwable throwable) {
		return new UserFeignClient() {

			@Override
			public Result<List<SysUser>> getUserByGroupId(String groupId) {
				log.error("通过组Id查询用户异常:{}", groupId, throwable);
				return Result.failed("查询失败");
			}

			@Override
			public Result<List<SysUser>> getUserByGroupCode(String code) {
				log.error("通过组code查询用户异常:{}", code, throwable);
				return Result.failed("查询失败");
			}
		};
	}
}
