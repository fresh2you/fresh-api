package com.zb.fresh_api.api.service;

import java.time.Duration;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "sms:";
    private static final String ATTEMPT_PREFIX = "attempt:";
    private static final int CODE_EXPIRATION_TIME = 5 * 60;
    private static final int ATTEMPT_EXPIRATION_TIME = 30 * 60;
    private static final int MAX_ATTEMPTS = 5;

    // 문자 인증의 인증 코드 저장 메서드
    public void saveCertificationCode(String phone, String certificationNumber) {
        redisTemplate.opsForValue()
            .set(PREFIX + phone, certificationNumber, Duration.ofSeconds(CODE_EXPIRATION_TIME));
    }

    // 문자 인증 코드 찾는 메서드
    public String findSmsCertification(String phone) {
        return redisTemplate.opsForValue().get(PREFIX + phone);
    }

    // 문자 인증 코드 제거 메서드
    public void removeSmsCertification(String phone) {
        redisTemplate.delete(PREFIX + phone);
        redisTemplate.delete(PREFIX + ATTEMPT_PREFIX + phone);
    }

    // 인증 횟수 초과 확인 메서드
    public boolean hasExceededAttemptLimit(String phone) {
        String key = PREFIX + ATTEMPT_PREFIX + phone;
        int attempts = redisTemplate.opsForValue().get(key) != null ? Integer.parseInt(
            Objects.requireNonNull(redisTemplate.opsForValue().get(key))) : 0;
        return attempts >= MAX_ATTEMPTS;
    }

    // 문자 인증 횟수 기록 메서드
    public void recordAttempt(String phone) {
        String key = PREFIX + ATTEMPT_PREFIX + phone;
        int attempts = redisTemplate.opsForValue().get(key) != null ? Integer.parseInt(
            Objects.requireNonNull(redisTemplate.opsForValue().get(key))) : 0;
        attempts++;
        redisTemplate.opsForValue()
            .set(key, String.valueOf(attempts), Duration.ofSeconds(ATTEMPT_EXPIRATION_TIME));
    }
}
