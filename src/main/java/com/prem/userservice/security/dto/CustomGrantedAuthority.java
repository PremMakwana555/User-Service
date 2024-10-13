package com.prem.userservice.security.dto;

import com.prem.userservice.model.Role;
import org.springframework.security.core.GrantedAuthority;

public class CustomGrantedAuthority implements GrantedAuthority {

    private String authority;

    public CustomGrantedAuthority(Role role){
        this.authority = role.name();
    }
    @Override
    public String getAuthority() {
        return authority;
    }
}
