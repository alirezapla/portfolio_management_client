package co.pla.portfoliomanagement.identity.infrastructure.util;

import co.pla.portfoliomanagement.identity.domain.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final SecretKey key;
    @Value("${app.jwt.token-expiration-minutes}")
    private int tokenExpirationMinutes;

    public JwtUtil(@Value("${app.jwt.secret-key}") String secretKey) {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user
                .getUserAuthorities()
                .stream()
                .map(a -> a.getAuthorityType().getTitle())
                .collect(Collectors.joining(" "))
        );
        return setTokenProperties(claims, user.getUsername());
    }

    private String setTokenProperties(Map<String, Object> claims, String subject) {
        Date issuedAt = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Date expireAt = Date.from(LocalDateTime.now()
                .plusMinutes(tokenExpirationMinutes)
                .atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(issuedAt)
                .expiration(expireAt)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}