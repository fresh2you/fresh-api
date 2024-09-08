package com.zb.fresh_api.api.utils;

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



    // 문자 인증의 인증 코드 저장 메서드
    public void saveCertificationCode(String phone, String certificationNumber) {
        redisTemplate.opsForValue()
            .set(RedisConstants.PHONE_PREFIX + phone, certificationNumber, Duration.ofSeconds(RedisConstants.CODE_EXPIRATION_TIME));
    }

    // 문자 인증 코드 찾는 메서드
    public String findSmsCertification(String phone) {
        return redisTemplate.opsForValue().get(RedisConstants.PHONE_PREFIX + phone);
    }

    // 문자 인증 코드 제거 메서드
    public void removeSmsCertification(String phone) {
        redisTemplate.delete(RedisConstants.PHONE_PREFIX + phone);
        redisTemplate.delete(RedisConstants.PHONE_PREFIX + RedisConstants.ATTEMPT_PREFIX + phone);
    }


    // 인증 횟수 초과 확인 메서드
    public boolean hasExceededAttemptLimit(String phone) {
        String key = RedisConstants.PHONE_PREFIX + RedisConstants.ATTEMPT_PREFIX + phone;
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

    // 문자 인증 횟수 기록 메서드
    public void recordAttempt(String phone) {
        String key = RedisConstants.PHONE_PREFIX + RedisConstants.ATTEMPT_PREFIX + phone;
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
        redisTemplate.opsForValue()
            .set(key, attempts + ":" + date, Duration.ofSeconds(RedisConstants.ATTEMPT_EXPIRATION_TIME));
    }
}
