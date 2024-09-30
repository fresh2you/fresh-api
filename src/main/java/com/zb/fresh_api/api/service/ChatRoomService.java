package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.request.ChatRoomRequest;
import com.zb.fresh_api.api.dto.response.ChatRoomResponse;
import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import com.zb.fresh_api.domain.repository.reader.ChatRoomReader;
import com.zb.fresh_api.domain.repository.writer.ChatRoomWriter;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomReader chatRoomReader;
    private final ChatRoomWriter chatRoomWriter;

    @Transactional
    public ChatRoomResponse createOneToOneChatRoom(ChatRoomRequest chatRoomRequest) {
        // 1:1 채팅방 ID 생성
        String chatRoomId = ChatRoom.createOneToOne(chatRoomRequest.sellerId(), chatRoomRequest.buyerId()).getChatRoomId();

        if (chatRoomReader.findById(chatRoomId).isPresent()) {
            throw new CustomException(ResponseCode.CHATROOM_ALREADY_EXISTS);
        }

        ChatRoom chatRoom = ChatRoom.createOneToOne(chatRoomRequest.sellerId(), chatRoomRequest.buyerId());
        chatRoomWriter.save(chatRoom);

        return new ChatRoomResponse(chatRoom.getChatRoomId(), "OPENED");
    }

    @Transactional
    public ChatRoomResponse createOneToManyChatRoom(ChatRoomRequest chatRoomRequest) {
        ChatRoom chatRoom = ChatRoom.createOneToMany(chatRoomRequest.sellerId(), chatRoomRequest.productId(), chatRoomRequest.categoryId());
        chatRoomWriter.save(chatRoom);

        return new ChatRoomResponse(chatRoom.getChatRoomId(), "OPENED");
    }

    @Transactional
    public void leaveChatRoom(String chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomReader.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_CHATROOM));

        // 멤버 삭제 로직 추가 등
        chatRoomWriter.delete(chatRoom);
    }
}
