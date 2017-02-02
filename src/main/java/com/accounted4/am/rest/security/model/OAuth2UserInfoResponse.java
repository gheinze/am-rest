package com.accounted4.am.rest.security.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.DigestUtils;

/**
 * A representation of the response object when querying an OAuth2 Resource Server for
 * information regarding an authenticated user.
 *
 * @author gheinze
 */
@Getter @Setter
public class OAuth2UserInfoResponse implements OAuth2UserInfo {

    // See: http://fr.gravatar.com/site/implement/images/
    private static final String GRAVATAR = "https://www.gravatar.com/avatar/%s?d=identicon";


    // Google, Github
    private String name;

    // Google, Github
    private String email;

    // Google
    @JsonProperty(value = "picture", access = Access.WRITE_ONLY)
    @JsonInclude(Include.NON_NULL)
    private String picture;

    // Github
    @JsonProperty(value = "avatar_url", access=Access.WRITE_ONLY)
    @JsonInclude(Include.NON_NULL)
    private String avatarUrl;


    @Override
    public String getImageUrl() {

        if (null != picture) {
            // Google
            return picture;

        } else if (null != avatarUrl) {
            // Github
            return avatarUrl;
        }

        // Default to Gravatar image baed on email, and if that is not found, Gravatar identicon
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(email.getBytes());

        return String.format(GRAVATAR, md5DigestAsHex);

    }


}
