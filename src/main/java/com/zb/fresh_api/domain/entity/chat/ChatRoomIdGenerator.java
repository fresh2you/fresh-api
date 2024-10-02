package com.zb.fresh_api.domain.entity.chat;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ChatRoomIdGenerator {

    public static long generateSha256ChatRoomId(String sellerName, String buyerName) {
        try {
            String[] nicknames = {sellerName, buyerName};
            Arrays.sort(nicknames);
            String combinedName = nicknames[0] + nicknames[1];

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(combinedName.getBytes(StandardCharsets.UTF_8));

            long chatRoomId = 0;
            for (int i = 0; i < Math.min(8, hashBytes.length); i++) {
                chatRoomId = (chatRoomId << 8) | (hashBytes[i] & 0xFF);
            }
            return Math.abs(chatRoomId);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating SHA-256 ID", e);
        }
    }
}

