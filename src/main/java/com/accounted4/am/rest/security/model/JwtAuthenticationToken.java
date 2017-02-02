package com.accounted4.am.rest.security.model;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


/**
 * A Spring Security Authentication object based on a JWT. Spring Authentication objects
 * store the details of the principal currently interacting with the application. The
 * application can retrieve the authentication object from the security context and
 * access user details and grants.
 *
 * The ProviderManager accepts an authentication token and, if it can successfully
 * authenticate based on the information in the token, returns an Authentication object.
 *
 * @author gheinze
 */
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    @Getter
    private final String jwt;


    public JwtAuthenticationToken(String jwt) {
        super(null, null);
        this.jwt = jwt;
    }

}
