package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.request.ChatRoomRequest;
import com.zb.fresh_api.api.dto.response.ChatRoomResponse;
import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import com.zb.fresh_api.domain.entity.chat.ChatRoomIdGenerator;
import com.zb.fresh_api.domain.entity.chat.ChatRoomMember;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.repository.jpa.MemberJpaRepository;
import com.zb.fresh_api.domain.repository.reader.ChatRoomReader;
import com.zb.fresh_api.domain.repository.writer.ChatRoomMemberWriter;
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
    private final ChatRoomMemberWriter chatRoomMemberWriter;
    private final MemberJpaRepository memberJpaRepository;

    @Transactional
    public ChatRoomResponse createOneToOneChatRoom(ChatRoomRequest chatRoomRequest) {
        Member seller = memberJpaRepository.findByNickname(chatRoomRequest.sellerName())
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_MEMBER));
        Member buyer = memberJpaRepository.findByNickname(chatRoomRequest.buyerName())
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_MEMBER));

        // 1:1 채팅방 ID 생성
        long chatRoomId = ChatRoomIdGenerator.generateSha256ChatRoomId(seller.getNickname(), buyer.getNickname());

        if (chatRoomReader.findById(chatRoomId).isPresent()) {
            throw new CustomException(ResponseCode.CHATROOM_ALREADY_EXISTS);
        }

        // 생성된 ID로 1:1 채팅방 생성
        ChatRoom chatRoom = ChatRoom.createOneToOne(seller.getNickname(), buyer.getNickname());
        chatRoomWriter.save(chatRoom);

        chatRoomMemberWriter.save(new ChatRoomMember(chatRoom, seller.getId(), true, true));
        chatRoomMemberWriter.save(new ChatRoomMember(chatRoom, buyer.getId(), false, true));

        return new ChatRoomResponse(chatRoom.getChatRoomId(), "OPENED");
    }

    @Transactional
    public ChatRoomResponse createOneToManyChatRoom(ChatRoomRequest chatRoomRequest) {
        // 닉네임을 통해 판매자 정보 조회
        Member seller = memberJpaRepository.findByNickname(chatRoomRequest.sellerName())
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_MEMBER));

        // 1:10 그룹 채팅방 ID 생성
        long chatRoomId = ChatRoomIdGenerator.generateSha256ChatRoomId(seller.getNickname(), "GROUP_CHAT");

        ChatRoom chatRoom = ChatRoom.createOneToMany(seller.getNickname(), chatRoomRequest.productId(), chatRoomRequest.categoryId());
        chatRoomWriter.save(chatRoom);

        chatRoomMemberWriter.save(new ChatRoomMember(chatRoom, seller.getId(), true, true));

        return new ChatRoomResponse(chatRoom.getChatRoomId(), "OPENED");
    }

    @Transactional
    public void leaveChatRoom(Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomReader.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_CHATROOM));

        // 멤버 삭제 로직 추가 등
        chatRoomWriter.delete(chatRoom);
    }
}
