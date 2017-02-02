package com.accounted4.am.rest.security.service;

import com.accounted4.am.rest.security.model.AuthCodeResponse;
import com.accounted4.am.rest.security.model.RestClientIdentity;
import com.accounted4.am.rest.securityservice.oauth2.OAuth2AuthProviderName;

/**
 * Services to support authentication via OAuth2.
 *
 * @author gheinze
 */
public interface OAuth2Service {

    /**
     * The url a client should use for retrieving the Authorization Code from the
     * given Authorization Provider.
     *
     * @param providerName Name of the Authorization Provider (google, github, ...)
     * @return a String comprising the query to be used to request an auth code from an Auth Provider.
     */
    String getRedirect(OAuth2AuthProviderName providerName);


    /**
     * Retrieve a JWT for the authenticated user that should be used for all future authenticated
     * requests to the REST server.
     *
     * @param providerName
     * @param authCodeResponse
     * @return
     */
    RestClientIdentity getRestClientIdentity(OAuth2AuthProviderName providerName, AuthCodeResponse authCodeResponse);


}
