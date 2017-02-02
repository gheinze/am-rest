package com.accounted4.am.rest.security.config;

import com.accounted4.am.rest.securityservice.oauth2.OAuth2AuthProviderName;
import lombok.Getter;
import lombok.Setter;


/**
 * Configuration values used for communicating with an OAuth2 Authorization Provider.
 * Typically loaded from external configuration during container startup.
 *
 * @author gheinze
 */
@Getter @Setter
public class OAuth2AuthProviderProperties {

    private OAuth2AuthProviderName providerName;

    private String clientId;
    private String clientSecret;
    private String authorizationCodeEndpoint;
    private String tokenEndpoint;
    private String scope;
    private String userInfoEndpoint;


    public OAuth2AuthProviderProperties(OAuth2AuthProviderName providerName) {
        this.providerName = providerName;
    }

}
