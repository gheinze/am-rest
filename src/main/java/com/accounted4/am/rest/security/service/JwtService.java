package com.accounted4.am.rest.security.service;

import com.accounted4.am.rest.security.model.JwtPrivateClaims;


/**
 * Services for creating and validating JWTs.
 *
 * @author gheinze
 */
public interface JwtService {

    String generateJWt(JwtPrivateClaims customClaims);

    JwtPrivateClaims parseAuthorizationHeader(String authHeader);

    JwtPrivateClaims parseJwt(String jwt);

}
