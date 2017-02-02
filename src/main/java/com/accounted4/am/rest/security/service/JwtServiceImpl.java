package com.accounted4.am.rest.security.service;

import com.accounted4.am.rest.security.model.JwtPrivateClaims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 *
 * @author gheinze
 */
@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private static final String ISSUER = "a4";
    private static final long EXPIRATION_MS = 12L * 60L * 60L * 1000L;
    private static final String USER_ID_KEY = "uid";
    private static final String ROLE_KEY = "rle";
    private SecretKey key;


    @PostConstruct
    protected void init() {
        key = MacProvider.generateKey(SignatureAlgorithm.HS256);
        //String base64Encoded = TextCodec.BASE64.encode(key.getEncoded());
    }


    @Override
    public String generateJWt(JwtPrivateClaims jwtUserInfo) {

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION_MS))
                .claim(USER_ID_KEY, jwtUserInfo.getUserId())
                .claim(ROLE_KEY, jwtUserInfo.getRole())
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

    }


    @Override
    public JwtPrivateClaims parseAuthorizationHeader(String authHeader) {
        String jwt = authHeader.split(" ")[1];
        return parseJwt(jwt);
    }


    @Override
    public JwtPrivateClaims parseJwt(String jwt) {

        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt)
                .getBody()
                ;

        JwtPrivateClaims jwtUserInfo = new JwtPrivateClaims();
        jwtUserInfo.setUserId(claims.get(USER_ID_KEY, Integer.class));
        jwtUserInfo.setRole(claims.get(ROLE_KEY, String.class));
        jwtUserInfo.setIssuer(claims.getIssuer());
        jwtUserInfo.setIssuedAt(claims.getIssuedAt());
        jwtUserInfo.setExpiration(claims.getExpiration());

        return jwtUserInfo;

    }

}
