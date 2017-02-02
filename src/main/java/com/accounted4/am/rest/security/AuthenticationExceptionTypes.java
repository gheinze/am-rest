package com.accounted4.am.rest.security;

import com.accounted4.am.rest.security.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;


/**
 * Known error conditions the application may report.
 *
 * @author gheinze
 */
@Slf4j
public enum AuthenticationExceptionTypes {

     UnsupportedAuthorizationProvider("Unsupported Authorization Provider [provider = %s]")
    ,UnrecognizedAuthCodeState("The state returned from the OAuth2 auth code response is not recognized as originating from this server.")
    ,ExpiredOAuthToken("The OAuth token is expired.")
    ,JwtCreationError("Error creating JWS")
    ,JwtProcessingError("Unable to determine user identity.")
    ,JwtMissingError("A JWT must be provided to access services.")

    ;

    private final String errorTemplate;

    private AuthenticationExceptionTypes(String errorTemplate) {
        this.errorTemplate = errorTemplate;
    }



    /**
     * Generate an exception for a known error condition providing the parameters
     * for the message template of the known error.
     *
     * @param formatArgs
     * @return
     * @throws RestAuthenticationException
     */
    public RestAuthenticationException fail(Object... formatArgs) throws RestAuthenticationException {
        ErrorResponse errorResponse = getErrorResponse(formatArgs);
        log.warn(errorResponse.getMsg());
        return new RestAuthenticationException(errorResponse);
    }



    public ErrorResponse getErrorResponse(Object... formatArgs) {
        return new ErrorResponse(name(), String.format(errorTemplate, formatArgs));
    }


}
