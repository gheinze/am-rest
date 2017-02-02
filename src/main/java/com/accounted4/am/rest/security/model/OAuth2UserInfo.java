package com.accounted4.am.rest.security.model;

/**
 * The representation of user information retrieved from an OAuth2 Resource Server during
 * the authentication process.
 * 
 * @author gheinze
 */
public interface OAuth2UserInfo {

    String getName();
    String getEmail();
    String getImageUrl();

}
