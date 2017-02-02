package com.accounted4.am.rest.security.controller;

import com.accounted4.am.rest.security.RestAuthenticationException;
import com.accounted4.am.rest.security.model.ErrorResponse;
import com.accounted4.am.rest.security.AuthenticationExceptionTypes;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Intercept exceptions on behalf of controllers.
 *
 * @author gheinze
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * Send back a JSON object with an "error" message.
     *
     * @param serviceException
     * @return
     */
    @ExceptionHandler(RestAuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleServiceException(RestAuthenticationException serviceException) {
        return serviceException.getErrorResponse();
    }


    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse handleJsonProcessingException(JwtException ex) {
        log.warn("JWT processing exception", ex);
        return AuthenticationExceptionTypes.JwtProcessingError.getErrorResponse("");
    }


}
