package com.accounted4.am.rest.security.service;

import com.accounted4.am.rest.security.model.AuthCodeResponse;
import com.accounted4.am.rest.securityservice.oauth2.AuthCodeHandler;
import com.accounted4.am.rest.security.config.OAuth2AuthProviderProperties;
import com.accounted4.am.rest.security.model.JwtPrivateClaims;
import com.accounted4.am.rest.security.model.OAuth2UserInfo;
import com.accounted4.am.rest.security.model.OAuth2UserInfoResponse;
import com.accounted4.am.rest.security.model.RestClientIdentity;
import com.accounted4.am.rest.securityservice.oauth2.OAuth2AuthProviderName;
import com.accounted4.am.rest.securityservice.oauth2.AuthTokenHandler;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * Services for authenticating via OAuth2 Providers.
 *
 * The pattern for authorization:
 * <ul>
 *     <li>Get url for the Auth Provider in order to redirect the client to obtain an Auth Code</li>
 *     <li>Query the Auth Provider for a Auth Token using the Auth Code received from the client</li>
 *     <li>Use the Auth Token to query the Auth Provider's Resource Server for user information</li>
 *     <li>Lookup local user information based on the user information retrieved from the Resoure Server</li>
 *     <li>Generate a JWT for the authenticated user</li>
 * </ul>
 * @author gheinze
 */
@Service
@Slf4j
public class OAuth2ServiceImpl implements OAuth2Service {


    private final Map<OAuth2AuthProviderName, OAuth2AuthProviderProperties> knownAuthProviders;
    private final AuthCodeHandler authCodeHandler;
    private final AuthTokenHandler authTokenHandler;
    private final JwtService jwtService;


    @Autowired
    public OAuth2ServiceImpl(
             Map<OAuth2AuthProviderName, OAuth2AuthProviderProperties> knownAuthProviders
            ,AuthCodeHandler authCodeHandler
            ,AuthTokenHandler authTokenHandler
            ,JwtService jwtService
    ) {
        this.knownAuthProviders = knownAuthProviders;
        this.authCodeHandler = authCodeHandler;
        this.authTokenHandler = authTokenHandler;
        this.jwtService = jwtService;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public String getRedirect(OAuth2AuthProviderName providerName) {
        OAuth2AuthProviderProperties provider = knownAuthProviders.get(providerName);
        return authCodeHandler.getRedirectClientToAuthProvider(provider);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public RestClientIdentity getRestClientIdentity(OAuth2AuthProviderName providerName, AuthCodeResponse authCodeResponse) {

        OAuth2AuthProviderProperties provider = knownAuthProviders.get(providerName);
        String authorizationHeader = getAuthorizationHeader(provider, authCodeResponse);

        OAuth2UserInfo oauthUserInfo = getUserInfo(provider, authorizationHeader);


        JwtPrivateClaims jwtUserInfo = new JwtPrivateClaims(400, "DBA,ADMIN");

        String jwt = jwtService.generateJWt(jwtUserInfo);

        RestClientIdentity identity = new RestClientIdentity(oauthUserInfo, jwt);

        return identity;

    }


    private String getAuthorizationHeader(OAuth2AuthProviderProperties provider, AuthCodeResponse authCodeResponse) {
        authCodeHandler.validateAuthCodeStateToken(authCodeResponse);
        return authTokenHandler.getAuthorizationHeader(
                 provider
                ,authCodeResponse
                ,authCodeHandler.getRedirectClientToAppServer(provider.getProviderName())
        );
    }


    private OAuth2UserInfo getUserInfo(OAuth2AuthProviderProperties provider, String authorizationHeader) {


        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authorizationHeader);

        HttpEntity entity = new HttpEntity("", headers);

        RestTemplate userInfoRestTemplate = new RestTemplate();

        ResponseEntity<OAuth2UserInfoResponse> response = userInfoRestTemplate.exchange(provider.getUserInfoEndpoint()
                ,HttpMethod.GET
                ,entity
                ,OAuth2UserInfoResponse.class
        );

        return response.getBody();
    }

}
