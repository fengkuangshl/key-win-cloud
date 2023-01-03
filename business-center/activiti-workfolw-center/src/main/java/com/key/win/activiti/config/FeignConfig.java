package com.key.win.activiti.config;

import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.util.SysUserUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * Feign配置
 * 使用FeignClient进行服务间调用，传递headers信息
 */
@Configuration
public class FeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //添加token,设置进请求头
        LoginAppUser loginApp = SysUserUtil.getLoginAppUser();
        requestTemplate.header("Authorization", "Bearer "+loginApp.getToken());
    }
}
