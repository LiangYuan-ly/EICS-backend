package com.emergency.auth.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000L;

    public static String generateToken(Integer adminId, String adminPhone) {
        return Jwts.builder()
                .claim("id", adminId)
                .claim("phone", adminPhone)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(KEY)
                .compact();
    }
}
