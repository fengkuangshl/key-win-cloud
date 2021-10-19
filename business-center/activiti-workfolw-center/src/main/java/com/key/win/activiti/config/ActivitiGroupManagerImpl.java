package com.key.win.activiti.config;

import org.activiti.api.runtime.shared.identity.UserGroupManager;
import org.activiti.core.common.spring.identity.ExtendedInMemoryUserDetailsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Primary
public class ActivitiGroupManagerImpl implements UserGroupManager {

    @Autowired
    @Qualifier("activitiUserDetailsManager")
    public  UserDetailsService userDetailsService;


    public List<String> getUserGroups(String username) {
        return (List)this.userDetailsService.loadUserByUsername(username).getAuthorities().stream().filter((a) -> {
            return a.getAuthority().startsWith("GROUP_");
        }).map((a) -> {
            return a.getAuthority().substring(6);
        }).collect(Collectors.toList());
    }

    public List<String> getUserRoles(String username) {
        return (List)this.userDetailsService.loadUserByUsername(username).getAuthorities().stream().filter((a) -> {
            return a.getAuthority().startsWith("ROLE_");
        }).map((a) -> {
            return a.getAuthority().substring(5);
        }).collect(Collectors.toList());
    }

    public List<String> getGroups() {
        return ((ActivitiUserDetailsManager)this.userDetailsService).getGroups();
    }

    public List<String> getUsers() {
        return ((ActivitiUserDetailsManager)this.userDetailsService).getUsers();
    }
}
