package com.key.win.activiti.feign;

import com.key.win.activiti.feign.fallback.UserFeignClientFallbackFactory;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.feign.FeignExceptionConfig;
import com.key.win.common.model.system.SysUser;
import com.key.win.common.web.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@FeignClient(value="user-center",configuration = FeignExceptionConfig.class ,fallbackFactory = UserFeignClientFallbackFactory.class, decode404 = true)
public interface UserFeignClient {

	/**
	 * feign rpc访问远程/users-anon/login接口
	 * @param username
	 * @return
	 */
    @GetMapping(value = "/users-anon/login", params = "username")
    LoginAppUser findByUsername(@RequestParam("username") String username);


	@GetMapping(value = "/users-anon/mobile", params = "mobile")
	LoginAppUser findByMobile(@RequestParam("mobile") String mobile);

	
	@GetMapping(value = "/users", params = "params")
    PageResult<SysUser> findUsers(@RequestParam Map<String, Object> params);
    
}
