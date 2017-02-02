package com.accounted4.am.rest.security.config;

import com.accounted4.am.rest.security.JwtAuthenticationProvider;
import com.accounted4.am.rest.security.JwtAuthenticationProcessingFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Container initialization beans dealing with web components.
 * Moving JWT filter into a Spring Security format, used example:
 * https://gitlab.com/palmapps/jwt-spring-security-demo/blob/master/src/main/java/nl/palmapps/myawesomeproject/security/config/WebSecurityConfig.java
 * @author gheinze
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private JwtAuthenticationProvider authenticationProvider;

    /**
     * Authentication to be performed by a Provider that checks the JWT.
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList(authenticationProvider));
    }


    @Bean
    public JwtAuthenticationProcessingFilter authenticationProcessingFilterBean() throws Exception {
        JwtAuthenticationProcessingFilter jwtFilter = new JwtAuthenticationProcessingFilter();
        jwtFilter.setAuthenticationManager(authenticationManager());
        jwtFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
        return jwtFilter;
    }



    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // we don't need CSRF because we are trusting the signed JWT
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/index.html", "/js/**", "/img/**", "/login/**").permitAll()
                // All urls must be authenticated (filter for token always fires (/**)
                .anyRequest().authenticated()
                .and()
                // Call our errorHandler if authentication/authorisation fails
                .exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .and()
                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ;

        // Custom JWT based security filter
        httpSecurity
                .addFilterBefore(authenticationProcessingFilterBean(), UsernamePasswordAuthenticationFilter.class)
                ;

        // disable page caching
        httpSecurity.headers().cacheControl();

    }



    private static class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
            // No redirection required on successful authentication of a REST call
        }
    }


    private static class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

        private static final long serialVersionUID = 3798723588865329956L;

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
                throws IOException {
            // Invoked when user tries to access a secured REST resource without supplying any credentials
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }



}
