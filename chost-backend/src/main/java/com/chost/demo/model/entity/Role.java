package com.chost.demo.model.entity;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER,ROLE_MODERATOR,ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
