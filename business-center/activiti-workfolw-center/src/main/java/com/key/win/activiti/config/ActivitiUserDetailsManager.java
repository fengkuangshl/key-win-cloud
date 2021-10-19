package com.key.win.activiti.config;

import com.key.win.activiti.feign.UserFeignClient;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.model.SysRole;
import com.key.win.common.util.SysUserUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.memory.UserAttribute;
import org.springframework.security.core.userdetails.memory.UserAttributeEditor;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Component("activitiUserDetailsManager")
public class ActivitiUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {

    protected final Log logger = LogFactory.getLog(this.getClass());
    private final Map<String, LoginAppUser> users = new HashMap();
    private AuthenticationManager authenticationManager;
    private List<String> userList = new ArrayList();
    private List<String> groups = new ArrayList();


    public ActivitiUserDetailsManager() {
    }

    public ActivitiUserDetailsManager(Collection<UserDetails> users) {
        Iterator var2 = users.iterator();

        while (var2.hasNext()) {
            UserDetails user = (UserDetails) var2.next();
            this.createUser(user);
        }

    }

    public ActivitiUserDetailsManager(UserDetails... users) {
        UserDetails[] var2 = users;
        int var3 = users.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            UserDetails user = var2[var4];
            this.createUser(user);
        }

    }

    public ActivitiUserDetailsManager(Properties users) {
        Enumeration<?> names = users.propertyNames();
        UserAttributeEditor editor = new UserAttributeEditor();

        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            editor.setAsText(users.getProperty(name));
            UserAttribute attr = (UserAttribute) editor.getValue();
            UserDetails user = new User(name, attr.getPassword(), attr.isEnabled(), true, true, true, attr.getAuthorities());
            this.createUser(user);
        }

    }

    public void createUser(UserDetails user) {
        Assert.isTrue(!this.userExists(user.getUsername()), "user should not exist");
        this.users.put(user.getUsername().toLowerCase(), (LoginAppUser) user);
        this.userList.add(user.getUsername());
        this.groups = (List) user.getAuthorities().stream().filter((x) -> {
            return x.getAuthority().contains("GROUP");
        }).map((x) -> {
            return x.getAuthority();
        }).collect(Collectors.toList());
    }

    public void deleteUser(String username) {
        this.users.remove(username.toLowerCase());
    }

    public void updateUser(UserDetails user) {
        Assert.isTrue(this.userExists(user.getUsername()), "user should exist");
        this.users.put(user.getUsername().toLowerCase(), (LoginAppUser) user);
    }

    public boolean userExists(String username) {
        return this.users.containsKey(username.toLowerCase());
    }

    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("Can't change password as no Authentication object found in context for current user.");
        } else {
            String username = currentUser.getName();
            this.logger.debug("Changing password for user '" + username + "'");
            if (this.authenticationManager != null) {
                this.logger.debug("Reauthenticating user '" + username + "' for password change request.");
                this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
            } else {
                this.logger.debug("No authentication manager set. Password won't be re-checked.");
            }

            LoginAppUser user = (LoginAppUser) this.users.get(username);
            if (user == null) {
                throw new IllegalStateException("Current user doesn't exist in database.");
            } else {
                user.setPassword(newPassword);
            }
        }
    }

    public UserDetails updatePassword(UserDetails user, String newPassword) {
        String username = user.getUsername();
        LoginAppUser mutableUser = (LoginAppUser) this.users.get(username.toLowerCase());
        mutableUser.setPassword(newPassword);
        return mutableUser;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();

        Set<SimpleGrantedAuthority> collect = Arrays.stream(loginAppUser.getSysRoles().toArray(new SysRole[loginAppUser.getSysRoles().size()])).map(e -> new SimpleGrantedAuthority(e.getCode())).collect(Collectors.toSet());

        return new User(loginAppUser.getUsername(), loginAppUser.getPassword(), loginAppUser.isEnabled(), loginAppUser.isAccountNonExpired(), loginAppUser.isCredentialsNonExpired(), loginAppUser.isAccountNonLocked(), collect);
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public List<String> getUsers() {
        return this.userList;
    }

    public List<String> getGroups() {
        return this.groups;
    }
}
