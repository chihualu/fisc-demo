package org.demo.entity.common;

import lombok.extern.log4j.Log4j2;
import org.demo.db.entity.WebRoleInfo;
import org.demo.db.entity.WebUserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Log4j2
public class WebUserDetails implements UserDetails {
    public WebUserDetails(WebUserInfo webUserInfo, Collection<GrantedAuthority> authorities) {
        this.webUserInfo = webUserInfo;
        this.authorities = authorities;
    }

    private WebUserInfo webUserInfo;
    private Collection<GrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return webUserInfo.getPasswd();
    }

    @Override
    public String getUsername() {
        return webUserInfo.getUserId();
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
        return webUserInfo.isEnabled();
    }
}
