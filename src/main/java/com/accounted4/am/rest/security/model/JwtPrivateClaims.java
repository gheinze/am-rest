package com.accounted4.am.rest.security.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Custom claims added to the JWT to identify the authenticated user with access rights.
 *
 * @author gheinze
 */
@Getter @Setter @NoArgsConstructor
public class JwtPrivateClaims {

    private static final String DEFAULT_JSON_DATE_FORMAT =  "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    // official claims
    private String issuer;

    @JsonFormat(pattern = DEFAULT_JSON_DATE_FORMAT)
    private Date issuedAt;

    @JsonFormat(pattern = DEFAULT_JSON_DATE_FORMAT)
    private Date expiration;

    // private claims
    private Integer userId;
    private String role;


    public JwtPrivateClaims(Integer userId, String role) {
        this.userId = userId;
        this.role = role;
    }

}
