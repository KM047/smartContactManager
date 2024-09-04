package com.scm.smartContactManager.security;

import com.scm.smartContactManager.models.UserModel;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private UserModel userModel;

    public UserPrincipal(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userModel.getRoleList().stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

    }

    @Override
    public String getPassword() {
        return userModel.getPassword();
    }

    @Override
    public String getUsername() {
        return userModel.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return userModel.isActive();
    }

}
