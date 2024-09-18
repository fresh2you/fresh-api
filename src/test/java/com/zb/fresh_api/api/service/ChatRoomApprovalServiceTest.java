package com.zb.fresh_api.api.service;

import com.zb.fresh_api.common.exception.UnauthorizedException;
import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ChatRoomApprovalServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatRoomApprovalService chatRoomApprovalService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("구매자의 채팅방 참여 요청 성공")
    void requestJoinChatRoom_success() {
        Long chatRoomId = 1L;
        Long buyerId = 2L;

        // Mock 채팅방 생성 및 스파이로 설정
        ChatRoom chatRoom = spy(new ChatRoom(1L, 1L, 1L, 1L));
        chatRoom.setMaxParticipants(10);

        // 기존 참여자 설정
        Set<Long> participants = new HashSet<>();
        participants.add(1L);  // 기존 참여자

        // 스파이된 ChatRoom에서 getParticipants() 모킹
        doReturn(participants).when(chatRoom).getParticipants();

        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));

        chatRoomApprovalService.requestJoinChatRoom(chatRoomId, buyerId);

        verify(chatRoomRepository, times(1)).save(chatRoom);
    }

    @Test
    @DisplayName("채팅방이 가득 찬 경우 참여 요청 실패")
    void requestJoinChatRoom_chatRoomFull() {
        Long chatRoomId = 1L;
        Long buyerId = 2L;

        // Mock 채팅방 생성 및 스파이로 설정
        ChatRoom chatRoom = spy(new ChatRoom(1L, 1L, 1L, 1L));
        chatRoom.setMaxParticipants(2);

        // 기존 참여자 설정
        Set<Long> participants = new HashSet<>();
        participants.add(1L);  // 기존 참여자
        participants.add(2L);  // 채팅방 가득참

        // 스파이된 ChatRoom에서 getParticipants() 모킹
        doReturn(participants).when(chatRoom).getParticipants();

        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));

        assertThrows(RuntimeException.class, () -> chatRoomApprovalService.requestJoinChatRoom(chatRoomId, buyerId));
    }

    @Test
    @DisplayName("구매자의 참여 요청 승인 성공")
    void approveBuyer_success() {
        Long chatRoomId = 1L;
        Long sellerId = 1L;
        Long buyerId = 2L;

        // Mock 채팅방 생성 및 스파이로 설정
        ChatRoom chatRoom = spy(new ChatRoom(1L, 1L, sellerId, buyerId));

        Set<Long> pendingBuyers = new HashSet<>();
        pendingBuyers.add(buyerId);

        // 스파이된 ChatRoom에서 getPendingBuyers(), getSellerId() 모킹
        doReturn(pendingBuyers).when(chatRoom).getPendingBuyers();
        doReturn(sellerId).when(chatRoom).getSellerId();

        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));

        chatRoomApprovalService.approveBuyer(chatRoomId, sellerId, buyerId);

        verify(chatRoomRepository, times(1)).save(chatRoom);
    }

    @Test
    @DisplayName("판매자 외의 사용자가 구매자 승인 시도 시 실패")
    void approveBuyer_unauthorized() {
        Long chatRoomId = 1L;
        Long sellerId = 1L;
        Long buyerId = 2L;
        Long unauthorizedUserId = 3L;  // 판매자가 아닌 다른 사용자

        // Mock 채팅방 생성
        ChatRoom chatRoom = new ChatRoom(1L, 1L, sellerId, buyerId);
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));

        assertThrows(UnauthorizedException.class, () -> chatRoomApprovalService.approveBuyer(chatRoomId, unauthorizedUserId, buyerId));
    }
}
