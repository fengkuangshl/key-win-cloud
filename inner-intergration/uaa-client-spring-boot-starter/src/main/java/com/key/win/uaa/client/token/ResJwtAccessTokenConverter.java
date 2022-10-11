package com.key.win.uaa.client.token;

import cn.hutool.core.bean.BeanUtil;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.model.system.SysRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.*;

@SuppressWarnings("all")
public class ResJwtAccessTokenConverter extends JwtAccessTokenConverter{
	
	
	public ResJwtAccessTokenConverter() {
		super();
		super.setAccessTokenConverter(new JwtUserAuthenticationConverter());
	}
	public  class JwtUserAuthenticationConverter extends DefaultAccessTokenConverter {
		
		
		public JwtUserAuthenticationConverter (){
			super.setUserTokenConverter(  new JWTfaultUserAuthenticationConverter());
		}
		
		
		public  class JWTfaultUserAuthenticationConverter extends DefaultUserAuthenticationConverter{
			
			public Authentication extractAuthentication(Map<String, ?> map) {
				
				if (map.containsKey(USERNAME)) {
					Object principal = map.get(USERNAME);
//					Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
					LoginAppUser loginUser = new LoginAppUser();
					if (principal instanceof Map) {

						loginUser = BeanUtil.mapToBean((Map) principal, LoginAppUser.class, true);
						 
						List<SysRole> roles = new ArrayList<>();
						
						for(Iterator<SysRole> it = loginUser.getSysRoles().iterator(); it.hasNext();){
							SysRole role =  BeanUtil.mapToBean((Map) it.next() , SysRole.class, false);
							roles.add(role) ;
						}
						loginUser.setSysRoles(roles); 
					} 
					return new UsernamePasswordAuthenticationToken(loginUser, "N/A", loginUser.getAuthorities());
				}
				
				
				 
				return null;
			}
			 
		}
		
	}
}
