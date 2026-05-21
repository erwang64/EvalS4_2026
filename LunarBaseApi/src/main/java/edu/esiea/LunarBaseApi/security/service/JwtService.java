package edu.esiea.LunarBaseApi.security.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
    private static final String SECRET_KEY_STR = "t8fd52selopv,dsef4bt8ui*fds+erhgytjty5cxscds.cvdsf!<#@#-8e431fds123";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STR.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME_MS = 1000 * 60 * 20; 

    
    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME_MS);

        return Jwts.builder()
                .subject(userDetails.getUsername()) 
                .claim("role", userDetails.getAuthorities()) 
                .issuedAt(now) 
                .expiration(expiration) 
                .signWith(SECRET_KEY) 
                .compact(); 
    }

    
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

}
