package com.accounted4.am.rest.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Identity information gathered during the authentication process. Includes
 * user information (ex name, email, image) retrieved from the OAuth2 Resource Server
 * as well as the JWT to use for future client-server exchanges.
 *
 * @author gheinze
 */
@Getter
public class RestClientIdentity {

    private final OAuth2UserInfo userInfo;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final String jwt;


    public RestClientIdentity(OAuth2UserInfo userInfo, String jwt) {
        this.userInfo = userInfo;
        this.jwt = jwt;
    }


    public String getAuthorizationHeader() {
        return "Bearer " + jwt;
    }

}
