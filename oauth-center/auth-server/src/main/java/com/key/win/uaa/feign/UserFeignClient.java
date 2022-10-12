package com.key.win.uaa.feign;

import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.feign.FeignExceptionConfig;
import com.key.win.common.model.system.SysUser;
import com.key.win.common.web.PageResult;
import com.key.win.uaa.feign.fallback.UserFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
* @author 作者 owen 
* @version 创建时间：2017年11月12日 上午22:57:51
* 调用用户中心中的userdetail对象，用户oauth中的登录
* 获取的用户与页面输入的密码 进行BCryptPasswordEncoder匹配
 */

@FeignClient(value="user-center",configuration = FeignExceptionConfig.class ,fallbackFactory = UserFeignClientFallbackFactory.class, decode404 = true)
public interface UserFeignClient {

	/**
	 * feign rpc访问远程/users-anon/login接口
	 * @param username
	 * @return
	 */
    @GetMapping(value = "/user/login", params = "username")
    LoginAppUser findByUsername(@RequestParam("username") String username);


	@GetMapping(value = "/user/mobile", params = "mobile")
	LoginAppUser findByMobile(@RequestParam("mobile") String mobile);

	
//	@GetMapping(value = "/users", params = "params")
//    PageResult<SysUser> findUsers(@RequestParam  Map<String, Object> params);
    
}
