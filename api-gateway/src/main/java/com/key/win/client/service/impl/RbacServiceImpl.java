package com.key.win.client.service.impl;
/**
 * 
 */

import com.key.win.client.service.SysClientService;
import com.key.win.uaa.client.service.RbacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * API 级别权限认证
 * 网关实现应用服务API接口
 * @author 作者 owen
 * @version 创建时间：2017年12月4日 下午5:32:29 类说明 blog: https://blog.51cto.com/13005375
 * desc 需要开启uaa-client-spring-boot-starter中的com.key.win.uaa.client.authorize.OpenAuthorizeConfigManager开启
 * code: https://gitee.com/owenwangwen/key-win-cloud
 */

@Service("rbacService")
@SuppressWarnings("all")
public class RbacServiceImpl implements RbacService {

	@Autowired
	private SysClientService sysClientService;
	private AntPathMatcher antPathMatcher = new AntPathMatcher();

	/**
	 * @param request HttpServletRequest
	 * @param authentication 认证信息
	 * @return 是否有权限
	 */
	@Override
	public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
		Authentication user = SecurityContextHolder.getContext().getAuthentication();
		boolean hasPermission = false;
		if (user != null) {
			if (user instanceof OAuth2Authentication) {
				OAuth2Authentication athentication = (OAuth2Authentication) user;
				String clientId = athentication.getOAuth2Request().getClientId();
				Map map = sysClientService.getClient(clientId);
				if (map == null) {
					hasPermission = false ;
				} else {
					List<Map> list = sysClientService.listByClientId(Long.valueOf(String.valueOf(map.get("id"))));
					hasPermission = list.stream().anyMatch(item -> antPathMatcher.match(String.valueOf(item.get("path")), request.getRequestURI()));
				}
			}
		}
		return hasPermission;
	}

}
