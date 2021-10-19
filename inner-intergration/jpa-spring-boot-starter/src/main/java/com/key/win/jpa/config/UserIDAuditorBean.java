package com.key.win.jpa.config;

import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.util.SysUserUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

import static org.springframework.security.config.Elements.ANONYMOUS;

@Configuration
public class UserIDAuditorBean implements AuditorAware<String>{

	@Override
    public Optional<String> getCurrentAuditor() {
        LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
        String userId = "";
        if (loginAppUser != null) {
            userId = loginAppUser.getId()+"";
        } else {
            userId = ANONYMOUS;
        }
        return  Optional.ofNullable(userId);
    }
}
