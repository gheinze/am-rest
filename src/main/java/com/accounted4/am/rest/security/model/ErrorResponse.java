package com.accounted4.am.rest.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Generic error response expected to be sent as the content body of intentionally
 * generated exceptions (non-200 status code).
 * 
 * @author gheinze
 */
@Data @AllArgsConstructor
public class ErrorResponse {

    private String error;
    private String msg;

}
