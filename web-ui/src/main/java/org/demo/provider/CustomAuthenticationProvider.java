package org.demo.provider;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Data
@Log4j2
public class CustomAuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String username = auth.getName();
        String password = auth.getCredentials().toString();
        if(username == null || username.equals("")) {
            throw new BadCredentialsException("username is null");
        }
        UserDetails userDetails = new User(username, "default", AuthorityUtils.createAuthorityList("USER"));

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetails, userDetails, this.authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
        result.setDetails(auth.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}