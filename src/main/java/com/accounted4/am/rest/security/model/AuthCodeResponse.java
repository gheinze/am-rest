package com.accounted4.am.rest.security.model;

import lombok.Getter;
import lombok.Setter;


/**
 * A model representing the parameters retrieved from a response to an
 * Authorization Code request to an Authorization Provider.
 * @author gheinze
 */
@Getter @Setter
public class AuthCodeResponse {

    private String code;
    private String state;

}
