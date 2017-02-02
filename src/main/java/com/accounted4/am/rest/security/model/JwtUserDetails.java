package com.accounted4.am.rest.security.model;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * Information about the user interacting with the application based on a JWT.
 * 
 * @author gheinze
 */
public class JwtUserDetails implements UserDetails {


    private final long userId;
    private final Collection<? extends GrantedAuthority> authorities;


    public JwtUserDetails(JwtPrivateClaims jwtPrivateClaims) {
        this.userId = jwtPrivateClaims.getUserId();
        authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(jwtPrivateClaims.getRole());
    }


    public long getUserId() {
        return userId;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
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
