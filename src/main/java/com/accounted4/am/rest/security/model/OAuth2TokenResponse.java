package com.accounted4.am.rest.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


/**
/**
 * A representation of an OAuth token received from an identity provider.
 *
 * See: https://docs.microsoft.com/en-us/azure/active-directory/active-directory-protocols-oauth-code (Azure example)
 *      https://tools.ietf.org/html/rfc6749 (OAuth 2.0 spec)
 *
 * @author gheinze
 */
@Getter
@Slf4j
public class OAuth2TokenResponse {


    private static final Long EXPIRY_SAFETY_MARGIN_SECONDS = 5L;


    // Expected value: Bearer
    @JsonProperty("token_type")
    private String tokenType;

    // CALCULATED FIELDS:
    //    The number of seconds after which the token expires and is no longer valid.
    //    Based on "expires_in". Optional.
    private Long expiresInSeconds;
    private Long calculatedExpiryEpoch;

    // NOT USED: Unknown meaning
    @JsonProperty("ext_expires_in")
    private String extExpiresIn;

    // The time at which the token expires expressed in epoch time (seconds since 1970). Optional.
    // This may be Microsoft/Azure specific. Don't see this in the OAuth 2.0 spec
    @JsonProperty("expires_on")
    private Long expiresOnEpoch;

    // NOT USED. The time at which the token is considered valid. Don't use before this time.
    @JsonProperty("not_before")
    private String notBeforeEpoch;

    // uri of the secured resource
    @JsonProperty("resource")
    private String resource;

    // The requested token
    @JsonProperty("access_token")
    private String accessToken;


    /**
     * If given a number of seconds till expiry, calculate the expiry time based on the system clock.
     * This is the most likely way expiry will be sent. It is part of the OAuth 2.0 spec.
     *
     * @param expiresInSeconds
     */
    @JsonProperty("expires_in")
    public void setExpiresInSeconds(long expiresInSeconds) {
        this.expiresInSeconds = expiresInSeconds;
        Long nowAsEpoch = System.currentTimeMillis() / 1000L;
        calculatedExpiryEpoch = nowAsEpoch + expiresInSeconds - EXPIRY_SAFETY_MARGIN_SECONDS;
    }


    /**
     * Returns a string to be used as the value of the "Authorization" header. Includes the
     * token type (typically "Bearer"). For example:
     *
     *     "Bearer {{base64EncodedTokenHere}}"
     *
     * @return
     */
    public String getAuthorizationHeader() {
        return String.format("%s %s", tokenType, accessToken);
    }



    /**
     * Is this token considered expired when compared to the system time?
     * Determine based on the expiry time provided.  If it is not provided,
     * use the number of seconds to expiry based on when this token was created.
     *
     * @return
     */
    public boolean isExpired() {

        Long nowAsEpoch = System.currentTimeMillis() / 1000L;

        if (null != expiresOnEpoch) {
            return (expiresOnEpoch - EXPIRY_SAFETY_MARGIN_SECONDS) <= nowAsEpoch;
        }

        if (null != calculatedExpiryEpoch) {
            return calculatedExpiryEpoch <= nowAsEpoch;
        }

        log.warn("No explict expiry set for oauth token. Assuming valid.");

        return false;

    }

}
