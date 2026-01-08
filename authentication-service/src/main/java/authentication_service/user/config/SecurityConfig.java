package authentication_service.user.config;

import authentication_service.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class SecurityConfig {

    @Value("${spring.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.jwt.expiration}")
    private Long expiration;

    public String generateJwtToken(User user) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("email", user.getEmail());
        claims.put("name", user.getFirstName());
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("role", user.getRole());

        long currentTime = System.currentTimeMillis();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build().
                    parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new JwtException("Invalid or Expired Token");
        }
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    private boolean isTokenValid(String token, User user) {
        try {
            return extractClaim(token, claims -> claims.get("email", String.class))
                    .equals(user.getEmail())
                    && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }


    private Key getSignInKey() {
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secretKey);
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);

    }

    private Key setSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}

