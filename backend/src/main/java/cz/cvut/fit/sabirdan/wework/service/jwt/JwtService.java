package cz.cvut.fit.sabirdan.wework.service.jwt;

import cz.cvut.fit.sabirdan.wework.domain.InvalidJwtToken;
import cz.cvut.fit.sabirdan.wework.domain.User;
import cz.cvut.fit.sabirdan.wework.repository.InvalidJwtTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {
    private final String secretKey;
    @Getter
    private final long jwtTokenExpiration;

    private final InvalidJwtTokenRepository invalidJwtTokenRepository;
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    @Autowired
    public JwtService(
            @Value("${application.security.jwt.secret-key}") String secretKey,
            @Value("${application.security.jwt.expiration}") long jwtTokenExpiration,
            InvalidJwtTokenRepository invalidJwtTokenRepository) {
        this.secretKey = secretKey;
        this.jwtTokenExpiration = jwtTokenExpiration;
        this.invalidJwtTokenRepository = invalidJwtTokenRepository;
    }

    public Long extractId(String jwtToken) {
        try {
            return Long.parseLong(extractClaim(jwtToken, Claims::getSubject));
        } catch (NumberFormatException ignore) {
            return null;
        }
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimResolver) {
        return claimResolver.apply(extractAllClaims(jwtToken));
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(jwtToken).getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateJwtToken(Map<String, Object> extraClaims, User user) {
        return generateJwtToken(extraClaims, user, getJwtTokenExpiration());
    }

    public String generateJwtToken(User user) {
        return generateJwtToken(user, getJwtTokenExpiration());
    }

    public String generateJwtToken(User user, long jwtTokenExpiration) {
        return buildJwtToken(new HashMap<>(), user, jwtTokenExpiration);
    }

    public String generateJwtToken(Map<String, Object> extraClaims, User user, long jwtTokenExpiration) {
        return buildJwtToken(extraClaims, user, jwtTokenExpiration);
    }

    private String buildJwtToken(Map<String, Object> extraClaims, User user, long jwtTokenExpiration) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(user.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtTokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isJwtTokenInvalid(String jwtToken, User user) {
        return !user.getId().equals(extractId(jwtToken)) // id matches
                || isJwtTokenExpired(jwtToken) // jwt token in not expired
                || getIssuedAt(jwtToken).isBefore(user.getLastFullLogoutDate()) // the user did not change password or make full logout since the token was issued
                || invalidJwtTokenRepository.existsByJwtToken(jwtToken);
    }

    public LocalDateTime getIssuedAt(String jwtToken) {
        return extractIssuedAt(jwtToken).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public LocalDateTime getExpirationDate(String jwtToken) {
        return extractExpirationDate(jwtToken).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private boolean isJwtTokenExpired(String jwtToken) {
        return Objects.requireNonNull(extractExpirationDate(jwtToken)).before(new Date());
    }

    private Date extractExpirationDate(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    private Date extractIssuedAt(String jwtToken) {
        return extractClaim(jwtToken, Claims::getIssuedAt);
    }

    public void logout(String jwtToken, User user) {
        // No reason to store an invalid token and tell the user about it
        try {
            if (isJwtTokenInvalid(jwtToken, user)) {
                log.info("User {} provided invalid token: {}. This should not normally happen.", user.getUsername(), jwtToken);
                return;
            }
        } catch (SignatureException | ExpiredJwtException ignore) {
            log.info("User {} provided bad or expired token: {}. This should not normally happen.", user.getUsername(), jwtToken);
            return;
        }

        invalidJwtTokenRepository.save(new InvalidJwtToken(jwtToken, getExpirationDate(jwtToken)));
    }

    @Scheduled(fixedRate = 86400000)
    public void cleanupExpiredTokens() {
        log.info("Cleaning up expired tokens at: {}", LocalDateTime.now());
        invalidJwtTokenRepository.deleteExpiredTokens();
    }
}
