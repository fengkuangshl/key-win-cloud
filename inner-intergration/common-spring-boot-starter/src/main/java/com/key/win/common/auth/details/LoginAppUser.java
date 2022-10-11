package com.key.win.common.auth.details;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.key.win.common.model.system.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 用户实体绑定spring security
 */
@Data
public class LoginAppUser extends SysUser implements UserDetails {

    private static final long serialVersionUID = -3685249101751401211L;

//    private Set<SysRole> sysRoles;

    @ApiModelProperty("登录时间")
    private Date loginTime = new Date();

    /***
     * 权限重写
     */
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new HashSet<>();
        Collection<GrantedAuthority> synchronizedCollection = Collections.synchronizedCollection(collection);
        if (!CollectionUtils.isEmpty(super.getSysRoles())) {
            super.getSysRoles().parallelStream().forEach(role -> {
                if (role.getCode().startsWith("ROLE_")) {
                    synchronizedCollection.add(new SimpleGrantedAuthority(role.getCode()));
                } else {
                    synchronizedCollection.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
                }
            });
        }

        if (!CollectionUtils.isEmpty(super.getPermissions())) {
            super.getPermissions().parallelStream().forEach(per -> {
                synchronizedCollection.add(new SimpleGrantedAuthority(per.getPermissionCode()));
            });
        }
        return collection;
    }

    @Override
    public String getUsername() {
        return super.getUserName();
    }


    @JsonIgnore
    public Collection<? extends GrantedAuthority> putAll(Collection<GrantedAuthority> collections) {
        Collection<GrantedAuthority> collection = new HashSet<>();
        collection.addAll(collections);
        return collection;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getEnabled();
    }

}
