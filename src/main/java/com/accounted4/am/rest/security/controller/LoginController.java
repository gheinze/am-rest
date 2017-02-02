package com.accounted4.am.rest.security.controller;

import com.accounted4.am.rest.security.model.AuthCodeResponse;
import com.accounted4.am.rest.security.RestAuthenticationException;
import com.accounted4.am.rest.securityservice.oauth2.OAuth2AuthProviderName;
import com.accounted4.am.rest.security.model.ErrorResponse;
import com.accounted4.am.rest.security.model.OAuth2UserInfo;
import com.accounted4.am.rest.security.model.RestClientIdentity;
import com.accounted4.am.rest.security.service.OAuth2Service;
import com.accounted4.am.rest.security.AuthenticationExceptionTypes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Endpoints for establishing OAuth2 authentication.
 *
 * @author gheinze
 */
@RestController
@RequestMapping("/login")
public class LoginController {


    private final OAuth2Service oAuth2Service;


    @Autowired
    public LoginController(OAuth2Service oAuth2Service) {
        this.oAuth2Service = oAuth2Service;
    }



    /**
     * Initiate authentication via a specified third party Authorization Service.
     * The response will be a redirect to the Authorization Service.
     * @param providerName
     * @param model
     * @return a redirect to the Authorization Service
     */
    @GetMapping(path = "/{provider}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView loginViaOauthProvider(@PathVariable("provider") OAuth2AuthProviderName providerName, ModelMap model) {
        String redirectUriAsString = oAuth2Service.getRedirect(providerName);
        return new ModelAndView("redirect:" + redirectUriAsString, model);
    }


    /**
     * Complete the OAuth2 authentication process by Validating the auth code sent by the Authorization server
     * (which includes retrieving the OAuth Token and the username).
     * @param providerName
     * @param authCodeResponse
     * @param response
     * @return a JWT token representing the authenticated user that should be passed in the header of all subsequent
     * requests to the application server.
     */
    @GetMapping(path = "/{provider}/oauth2Callback", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestClientIdentity oauth2AuthCodeCallback(
            @PathVariable("provider") OAuth2AuthProviderName providerName
            ,AuthCodeResponse authCodeResponse
            ,HttpServletResponse response
    ) {

        return oAuth2Service.getRestClientIdentity(providerName, authCodeResponse);
    }


    /**
     * Error response for a request to an unrecognized Authorization Provider.
     * @param req
     * @return
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ConversionFailedException.class)
    public ErrorResponse msgConversionFailedException(HttpServletRequest req) {
        String[] pathInfo = req.getRequestURI().split("/");
        String provider = (pathInfo.length <= 0) ? "No provider found on url" : pathInfo[pathInfo.length - 1];
        return AuthenticationExceptionTypes.UnsupportedAuthorizationProvider.getErrorResponse(provider);
    }


    /**
     * Error response for errors during the authentication process.
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(RestAuthenticationException.class)
    public ErrorResponse authenticationFailure(RestAuthenticationException ex) {
        return ex.getErrorResponse();
    }


}
