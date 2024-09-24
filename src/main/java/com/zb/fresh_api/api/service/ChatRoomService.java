package com.zb.fresh_api.api.service;

import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import com.zb.fresh_api.domain.entity.chat.ChatRoomMember;
import com.zb.fresh_api.domain.repository.reader.ChatRoomReader;
import com.zb.fresh_api.domain.repository.writer.ChatRoomWriter;
import com.zb.fresh_api.domain.repository.writer.ChatRoomMemberWriter;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomReader chatRoomReader;
    private final ChatRoomWriter chatRoomWriter;
    private final ChatRoomMemberWriter chatRoomMemberWriter;

    public void leaveChatRoom(String chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomReader.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_CHATROOM));

        ChatRoomMember chatRoomMember = chatRoomMemberWriter.findByChatRoom_ChatRoomIdAndMemberId(chatRoomId, memberId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_CHATROOM_MEMBER));

        chatRoomMemberWriter.delete(chatRoomMember);

        if (chatRoom.getMembers().isEmpty()) {
            chatRoomWriter.delete(chatRoom);
        }
    }
}
