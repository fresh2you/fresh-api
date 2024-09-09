package com.zb.fresh_api.api.utils;

import com.zb.fresh_api.api.enums.VerificationType;
import com.zb.fresh_api.common.constants.RedisConstants;
import java.time.Duration;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;


    // 인증 코드 저장 메서드
    public void saveVerificationCode(String phone, String verificationNumber,
        VerificationType verificationType) {
        redisTemplate.opsForValue().set(verificationType.getPrefix() + phone, verificationNumber,
            Duration.ofSeconds(RedisConstants.CODE_EXPIRATION_TIME));
    }

    //  인증 코드 찾는 메서드
    public String findSmsVerification(String phone, VerificationType verificationType) {
        return redisTemplate.opsForValue().get(verificationType.getPrefix() + phone);
    }

    //  인증 코드 제거 메서드
    public void removeSmsVerification(String phone, VerificationType verificationType) {
        redisTemplate.delete(verificationType.getPrefix() + phone);
        redisTemplate.delete(verificationType.getPrefix() + RedisConstants.ATTEMPT_PREFIX + phone);
    }


    // 인증 횟수 초과 확인 메서드
    public boolean hasExceededAttemptLimit(String to, VerificationType type) {
        String key = type.getPrefix() + RedisConstants.ATTEMPT_PREFIX + to;
        String attemptsData = redisTemplate.opsForValue().get(key);
        if (attemptsData != null) {
            String[] parts = attemptsData.split(":");
            int attempts = Integer.parseInt(parts[0]);
            LocalDate date = LocalDate.parse(parts[1]);
            if (date.isEqual(LocalDate.now())) {
                return attempts >= RedisConstants.MAX_ATTEMPTS;
            }
        }
        return false;
    }

    // 인증 횟수 기록 메서드
    public void recordAttempt(String phone, VerificationType verificationType) {
        String key = verificationType.getPrefix() + RedisConstants.ATTEMPT_PREFIX + phone;
        String attemptsData = redisTemplate.opsForValue().get(key);
        int attempts = 0;
        LocalDate date = LocalDate.now();
        if (attemptsData != null) {
            String[] parts = attemptsData.split(":");
            attempts = Integer.parseInt(parts[0]);
            LocalDate storedDate = LocalDate.parse(parts[1]);
            if (storedDate.isEqual(date)) {
                attempts++;
            } else {
                attempts = 1;
            }
        } else {
            attempts = 1;
        }
        redisTemplate.opsForValue().set(key, attempts + ":" + date,
            Duration.ofSeconds(RedisConstants.ATTEMPT_EXPIRATION_TIME));
    }
}
