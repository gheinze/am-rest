package com.accounted4.am.rest.security;

import com.accounted4.am.rest.security.model.JwtAuthenticationToken;
import com.accounted4.am.rest.security.model.JwtPrivateClaims;
import com.accounted4.am.rest.security.model.JwtUserDetails;
import com.accounted4.am.rest.security.service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


/**
 * Spring Security AuthenticationProvider which will examine a JWT for authentication.
 * AbstractUserDetailsAuthenticationProvider operates on tokens of type UsernamePasswordAuthenticationToken,
 * so the token class is extended to support JWT.
 *
 * @author gheinze
 */
@Component
@Slf4j
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {


    private final JwtService jwtService;


    @Autowired
    public JwtAuthenticationProvider(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }


    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }


    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken)authentication;
        String jwt = jwtAuthenticationToken.getJwt();
        try {
            JwtPrivateClaims jwtPrivateClaims = jwtService.parseJwt(jwt);
            return new JwtUserDetails(jwtPrivateClaims);
        } catch(JwtException jwte) {
            log.error("Error parsing jwt", jwte);
            throw(AuthenticationExceptionTypes.JwtProcessingError.fail(""));
        }
    }

}
