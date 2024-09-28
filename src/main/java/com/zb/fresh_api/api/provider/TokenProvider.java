package com.zb.fresh_api.api.provider;

import com.zb.fresh_api.api.principal.CustomUserDetails;
import com.zb.fresh_api.api.principal.CustomUserDetailsService;
import com.zb.fresh_api.common.constants.AuthConstants;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.dto.token.Token;
import com.zb.fresh_api.domain.enums.member.Provider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TokenProvider {

    private static final String EMAIL_SEPARATOR = "|";
    public static final Long ACCESS_TOKEN_EXPIRE_TIME = Duration.ofHours(6).toMillis();
    private final SecretKey key;
    private final CustomUserDetailsService customUserDetailsService;

    public TokenProvider(
        @Value("${spring.jwt.secret}") String key,
        CustomUserDetailsService customUserDetailsService
    ) {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.customUserDetailsService = customUserDetailsService;
    }

    public Token getTokenByEmail(String email, Provider provider) {
        String accessToken = createAccessToken(email, provider);
        final Date accessExpiredAt = getExpirationByToken(accessToken);
        return new Token(accessToken, accessExpiredAt);
    }

    public String createAccessToken(String email, Provider provider) {
        Date now = new Date();
        long accessTokenExpireTime = now.getTime() + ACCESS_TOKEN_EXPIRE_TIME;

        return Jwts.builder()
                .subject(email + EMAIL_SEPARATOR + provider.name())
                .issuedAt(now)
                .expiration(new Date(accessTokenExpireTime))
                .signWith(key)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(getEmailByToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(String header) {
        return Optional.ofNullable(header)
            .orElseThrow(() -> new CustomException(ResponseCode.INVALID_AUTHENTICATION))
            .replace(AuthConstants.TOKEN_PREFIX, "");
    }

    public boolean isValidateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }

        return !getExpirationByToken(token).before(new Date());
    }

    public String getEmailByToken(String token) {
        return parseClaims(token).getSubject();
    }

    public Date getExpirationByToken(String token) {
        return parseClaims(token).getExpiration();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (MalformedJwtException ex) {
            throw new CustomException(ResponseCode.MALFORMED_JWT_EXCEPTION);
        } catch (ExpiredJwtException ex) {
            throw new CustomException(ResponseCode.EXPIRED_JWT_EXCEPTION);
        } catch (UnsupportedJwtException ex) {
            throw new CustomException(ResponseCode.UNSUPPORTED_JWT_EXCEPTION);
        } catch (IllegalArgumentException ex) {
            throw new CustomException(ResponseCode.ILLEGAL_ARGUMENT_EXCEPTION);
        }
    }

}
