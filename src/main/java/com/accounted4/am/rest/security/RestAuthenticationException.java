package com.accounted4.am.rest.security;

import com.accounted4.am.rest.security.model.ErrorResponse;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * Exceptions which may be thrown by the REST authentication processes.
 * Contains an error response to allow for consistent error responses
 * to the client.
 *
 * @author gheinze
 */
@Getter
public class RestAuthenticationException extends AuthenticationException {

    private final ErrorResponse errorResponse;

    public RestAuthenticationException(ErrorResponse errorResponse) {
        super(errorResponse.getMsg());
        this.errorResponse = errorResponse;
    }

}
