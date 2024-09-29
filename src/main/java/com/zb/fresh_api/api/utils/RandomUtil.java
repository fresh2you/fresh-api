package com.zb.fresh_api.api.utils;

import com.zb.fresh_api.common.constants.AppConstants;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomUtil {

    private static final Random random = new Random();

    /**
     * @param list 요소가 들어있는 리스트
     * @param <T> 리스트의 타입
     * @return 리스트에서 랜덤하게 선택된 요소
     */
    public static <T> T getRandomElement(List<T> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            throw new CustomException(ResponseCode.INVALID_LIST);
        }

        final int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }

    /**
     * @param length 생성할 문자열 길이
     * @return 랜덤하게 생성된 문자열
     */
    public static String generateRandomSuffix(int length) {
        final String characters = AppConstants.CHARACTERS;
        final StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            randomString.append(characters.charAt(randomIndex));
        }

        return randomString.toString();
    }

}
