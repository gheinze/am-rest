package com.accounted4.am.rest.security;

import com.accounted4.am.rest.security.model.JwtAuthenticationToken;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;


/**
 * A Spring AuthenticationProcessing filter to authenticate by Verifing the validity
 * of the JWT received in the Authorization request header.
 *
 * @author gheinze
 */
public class JwtAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_ENDPOINTS_TO_SECURE = "/api/**";



    public JwtAuthenticationProcessingFilter() {
        super(DEFAULT_ENDPOINTS_TO_SECURE);
    }


    /**
     * Attempt to authenticate request - basically just pass over to another method to authenticate request headers
     * @param request
     * @param response
     * @return
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw(AuthenticationExceptionTypes.JwtMissingError.fail(""));
        }

        String jwt = authHeader.substring(7);
        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(jwt);

        return getAuthenticationManager().authenticate(authRequest);
    }


    /**
     * Make sure the rest of the filterchain is satisfied
     *
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }


}
