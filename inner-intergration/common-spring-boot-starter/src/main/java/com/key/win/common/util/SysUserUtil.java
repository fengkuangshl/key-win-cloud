package com.key.win.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.constant.UaaConstant;
import com.key.win.common.model.system.SysRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.*;

/**
 * @author 作者 owen
 * @version 创建时间：2017年11月12日 上午22:57:51 获取用户信息
 */
@SuppressWarnings("all")
public class SysUserUtil {

    /**
     * 获取登陆的 LoginAppUser
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static LoginAppUser getLoginAppUser() {
        
        // 当OAuth2AuthenticationProcessingFilter设置当前登录时，直接返回
        // 强认证时处理
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Auth = (OAuth2Authentication) authentication;
            authentication = oAuth2Auth.getUserAuthentication();

            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;

                if (authenticationToken.getPrincipal() instanceof LoginAppUser) {
                    return (LoginAppUser) authenticationToken.getPrincipal();
                } else if (authenticationToken.getPrincipal() instanceof Map) {

                    LoginAppUser loginAppUser = BeanUtil.mapToBean((Map) authenticationToken.getPrincipal(), LoginAppUser.class, true);
                    List<SysRole> roles = new ArrayList<>();
                    if (CollectionUtil.isNotEmpty(loginAppUser.getSysRoles())) {
    					for(Iterator<SysRole> it = loginAppUser.getSysRoles().iterator(); it.hasNext();){
    						SysRole role =  BeanUtil.mapToBean((Map) it.next() , SysRole.class, false);
    						roles.add(role) ;
    					}
                    }
                    loginAppUser.setSysRoles(roles); 
                    return loginAppUser;
                }
            } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
                // 刷新token方式
                PreAuthenticatedAuthenticationToken authenticationToken = (PreAuthenticatedAuthenticationToken) authentication;
                return (LoginAppUser) authenticationToken.getPrincipal();
            }
        }
        // 弱认证处理，当内部服务，不带token时，内部服务
        String accessToken = TokenUtil.getToken();
        if (accessToken != null) {
            RedisTemplate redisTemplate = SpringUtils.getBean(RedisTemplate.class);
            LoginAppUser loginAppUser = (LoginAppUser) redisTemplate.opsForValue().get(UaaConstant.TOKEN + ":" + accessToken);
            if (loginAppUser != null) {
                return loginAppUser;
            }
        }
       
        return null;
    }

    public static String getUserName() {
        LoginAppUser loginUser = getLoginAppUser();
        if (loginUser != null) {
            return loginUser.getUsername();
        }
        return KeyWinConstantUtils.SYSTEM_ANONYMOUS_USER;
    }

    public static Long getUserId() {
        LoginAppUser loginUser = getLoginAppUser();
        if (loginUser != null) {
            return loginUser.getId();
        }
        return KeyWinConstantUtils.SYSTEM_ANONYMOUS_USER_ID;
    }

    public static String getHeadImgUrl() {
        LoginAppUser loginUser = getLoginAppUser();
        if (loginUser != null) {
            return loginUser.getHeadImgUrl();
        }
        return "";
    }

    public static boolean isAnonymousUser() {
        LoginAppUser loginUser = getLoginAppUser();
        if (loginUser == null) {
            return true;
        }
        return false;
    }

    public static String getToken() {
        LoginAppUser loginUser = getLoginAppUser();
        if (loginUser != null) {
            return loginUser.getToken();
        }
        return "";
    }
}
