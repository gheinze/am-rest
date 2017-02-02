package com.accounted4.am.rest.security.controller;

import com.accounted4.am.rest.security.model.JwtPrivateClaims;
import com.accounted4.am.rest.security.service.JwtService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A test controller used to validate a JWT by echoing back the user info
 * contained therein.
 *
 * @author gheinze
 */
@RestController
@RequestMapping("/api/jwt-echo")
public class HelloController {

    private final JwtService jwtService;


    @Autowired
    public HelloController(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    /**
     * Initiate authentication via a specified third party Authorization Service.
     * The response will be a redirect to the Authorization Service.
     * @param request
     * @return a redirect to the Authorization Service
     */
    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public JwtPrivateClaims loginViaOauthProvider(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.split(" ")[1];
        return jwtService.parseJwt(jwt);
    }



}
