package com.accounted4.am.rest.securityservice.oauth2;

import com.accounted4.am.rest.security.RestAuthenticationException;
import com.accounted4.am.rest.security.config.OAuth2AuthProviderProperties;
import com.accounted4.am.rest.security.model.AuthCodeResponse;
import com.accounted4.am.rest.security.model.OAuth2TokenResponse;
import com.accounted4.am.rest.security.AuthenticationExceptionTypes;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author gheinze
 */
@Service
public class AuthTokenHandler {


    /**
     * Retrieve the value for the Authorization header request parameter that would be used when accessing
     * OAuth2 secured services.
     *
     * @param provider The Authorization Server from which the token should be requested.
     * @param authCodeResponse Contains the authorization code to use when querying for the token.
     * @param registeredCallBackUrl The application's allback url registered with the Authorization Server.
     * @return
     */
    public String getAuthorizationHeader(OAuth2AuthProviderProperties provider, AuthCodeResponse authCodeResponse, String registeredCallBackUrl) {

        HttpEntity<String> httpEntityForTokenRequest = getHttpEntityForAuthProviderTokenRequest(
                 provider
                ,authCodeResponse.getCode()
                ,registeredCallBackUrl
        );

        RestTemplate authTokenRestTemplate = new RestTemplate();

        OAuth2TokenResponse tokenResponse = authTokenRestTemplate.postForObject(
                 provider.getTokenEndpoint()
                ,httpEntityForTokenRequest
                ,OAuth2TokenResponse.class
        );

        validateToken(tokenResponse);

        return tokenResponse.getAuthorizationHeader();
    }


    private void validateToken(OAuth2TokenResponse tokenResponse) {

        // TODO: github tokens don't expire. validity checked at https://developer.github.com/v3/oauth_authorizations/#check-an-authorization
        if (tokenResponse.isExpired()) {
            throw(new RestAuthenticationException(AuthenticationExceptionTypes.ExpiredOAuthToken.getErrorResponse("")));
        }
    }


    private HttpEntity<String> getHttpEntityForAuthProviderTokenRequest(OAuth2AuthProviderProperties provider, String authCode, String authProviderResponseRedirect) {
        HttpHeaders oauthHeaders = new HttpHeaders();
        oauthHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(
                buildOauthTokenQueryContentBody(provider, authCode, authProviderResponseRedirect)
                , oauthHeaders
        );
    }


    private static final String PARAM_SEPARATOR = "&";

    private String buildOauthTokenQueryContentBody(OAuth2AuthProviderProperties provider, String authCode, String authProviderResponseRedirect) {
        StringBuilder sb = new StringBuilder();
        sb.append("code=").append(authCode).append(PARAM_SEPARATOR);
        sb.append("client_id=").append(provider.getClientId()).append(PARAM_SEPARATOR);
        sb.append("client_secret=").append(provider.getClientSecret()).append(PARAM_SEPARATOR);
        sb.append("redirect_uri=").append(authProviderResponseRedirect).append(PARAM_SEPARATOR);
        sb.append("grant_type=").append("authorization_code");
        return sb.toString();
    }
}
