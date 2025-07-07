package com.example.TODO.Utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JWTUtil {
    private static final String SECRET = "your_secret_key";

    public static String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hr
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }
}
