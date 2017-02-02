package com.accounted4.am.rest.security.config;

import com.accounted4.am.rest.securityservice.oauth2.OAuth2AuthProviderName;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Creation of beans related to OAuth2 Authentication services.
 *
 * @author gheinze
 */
@Configuration
public class OAuth2AuthProviderConfig {


    /**
     * Configuration and utilities for accessing a Google Authorization Server.
     * @return
     */
    @Bean
    @ConfigurationProperties("security.oauth2.provider.google.client")
    OAuth2AuthProviderProperties google() {
        return new OAuth2AuthProviderProperties(OAuth2AuthProviderName.google);
    }

    /**
     * Configuration and utilities for accessing a Facebook Authorization Server.
     * @return
     */
    @Bean
    @ConfigurationProperties("security.oauth2.provider.facebook.client")
    OAuth2AuthProviderProperties facebook() {
        return new OAuth2AuthProviderProperties(OAuth2AuthProviderName.facebook);
    }

    /**
     * Configuration and utilities for accessing a Github Authorization Server.
     * @return
     */
    @Bean
    @ConfigurationProperties("security.oauth2.provider.github.client")
    OAuth2AuthProviderProperties github() {
        return new OAuth2AuthProviderProperties(OAuth2AuthProviderName.github);
    }


    /**
     * Convenience mechanism to provide access by name to an Authorization Server.
     * @return
     */
    @Bean
    Map<OAuth2AuthProviderName, OAuth2AuthProviderProperties> oauth2AuthProviders() {
        Map<OAuth2AuthProviderName, OAuth2AuthProviderProperties> map = new EnumMap<>(OAuth2AuthProviderName.class);
        map.put(OAuth2AuthProviderName.google, google());
        map.put(OAuth2AuthProviderName.github, github());
        map.put(OAuth2AuthProviderName.facebook, facebook());
        return map;
    }

}
