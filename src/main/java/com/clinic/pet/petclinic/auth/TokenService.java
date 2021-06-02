package com.clinic.pet.petclinic.auth;

import com.clinic.pet.petclinic.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;

@Service
public class TokenService {
    private final SecretKey secretKey = Keys.secretKeyFor(HS512);
    @Value("${security.jwt.token.expire-length:3600000}")
    private final long validityInMilliseconds = 3600000; // 1h

    String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    String generateNewToken(UserDetails userDetails, List<Role> roles) {
        var claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("auth", roles.stream()
                .filter(Objects::nonNull)
                .map(s -> new SimpleGrantedAuthority(s.getAuthority()))
                .collect(Collectors.toList()));
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(key(), HS512)
                .compact();
    }

    boolean isValidForUser(String token, UserDetails userDetails) {
        var username = getUsernameFromToken(token);
        return !isTokenExpired(token) && username.equals(userDetails.getUsername());
    }

    private Boolean isTokenExpired(String token) {
        return getAllClaimsFromToken(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(secretKey.getEncoded());
    }
}

