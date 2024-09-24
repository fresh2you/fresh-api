package com.zb.fresh_api.api.service;

import com.zb.fresh_api.domain.entity.chat.ChatMessage;
import com.zb.fresh_api.domain.entity.chat.ChatRoomMember;
import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import com.zb.fresh_api.domain.repository.writer.ChatMessageWriter;
import com.zb.fresh_api.domain.repository.writer.ChatRoomMemberWriter;
import com.zb.fresh_api.domain.repository.writer.ChatRoomWriter;
import com.zb.fresh_api.domain.repository.reader.ChatRoomReader;
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
    private final ChatMessageWriter chatMessageWriter;

    public ChatRoom createOneToOneChatRoom(Long productId, Long buyerId, Long sellerId) {
        Optional<ChatRoom> existingRoom = chatRoomReader.findBySellerIdAndBuyerIdAndProductId(sellerId, buyerId, productId);
        if (existingRoom.isPresent()) {
            return existingRoom.get();
        }

        ChatRoom chatRoom = ChatRoom.createOneToOne(sellerId, buyerId);
        chatRoomWriter.save(chatRoom);

        addParticipant(chatRoom.getChatRoomId(), sellerId, true, true);
        addParticipant(chatRoom.getChatRoomId(), buyerId, false, true);

        return chatRoom;
    }

    public ChatRoom createOneToManyChatRoom(Long productId, Long sellerId) {
        int sequence = 1;
        String chatRoomId;
        Optional<ChatRoom> existingRoom;

        do {
            chatRoomId = sellerId + String.valueOf(sequence);
            existingRoom = chatRoomReader.findById(chatRoomId);
            sequence++;
        } while (existingRoom.isPresent());

        ChatRoom chatRoom = ChatRoom.createOneToMany(sellerId, (long) (sequence - 1), productId);
        chatRoomWriter.save(chatRoom);
        addParticipant(chatRoom.getChatRoomId(), sellerId, true, true);

        return chatRoom;
    }

    public void addParticipant(String chatRoomId, Long memberId, boolean isSeller, boolean isApproved) {
        ChatRoom chatRoom = chatRoomReader.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_CHATROOM));

        if (chatRoom.getMembers().size() >= chatRoom.getMaxParticipants()) {
            throw new CustomException(ResponseCode.MAX_PARTICIPANTS_EXCEEDED);
        }

        ChatRoomMember member = new ChatRoomMember(chatRoom, memberId, isSeller, isApproved);
        chatRoomMemberWriter.save(member);
    }

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

    // 메시지 저장
    public void saveMessage(ChatMessage chatMessage) {
        chatMessageWriter.save(chatMessage);
    }
}
