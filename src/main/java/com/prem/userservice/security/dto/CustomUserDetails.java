package com.prem.userservice.security.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.prem.userservice.model.Role;
import com.prem.userservice.model.User;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
@NoArgsConstructor
@JsonDeserialize
public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private List<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
        this.username = user.getEmail();
        this.password = user.getHashedPassword();
        this.authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add(new CustomGrantedAuthority(role));
        }
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }

}
