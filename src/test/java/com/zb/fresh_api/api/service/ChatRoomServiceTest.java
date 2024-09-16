package com.zb.fresh_api.api.service;

import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import com.zb.fresh_api.domain.entity.chat.ChatRoomMember;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomMemberRepository;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ChatRoomServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatRoomMemberRepository chatRoomMemberRepository;

    @InjectMocks
    private ChatRoomService chatRoomService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 1:1 채팅방 생성 테스트
    @Test
    public void testCreateOneToOneChatRoom_Success() {
        Long productId = 1L;
        Long buyerId = 2L;
        Long sellerId = 3L;

        // 기존 채팅방이 없는 경우
        when(chatRoomRepository.findBySellerIdAndBuyerIdAndProductId(sellerId, buyerId, productId))
                .thenReturn(Optional.empty());

        // 새 채팅방 생성
        ChatRoom savedRoom = new ChatRoom(productId, null, sellerId, buyerId);
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(savedRoom);

        // chatRoomRepository.findById() 모킹 설정
        when(chatRoomRepository.findById(savedRoom.getId())).thenReturn(Optional.of(savedRoom));

        ChatRoom chatRoom = chatRoomService.createOneToOneChatRoom(productId, buyerId, sellerId);

        // 검증
        assertNotNull(chatRoom);
        assertEquals(productId, chatRoom.getProductId());
        assertEquals(buyerId, chatRoom.getBuyerId());
        assertEquals(sellerId, chatRoom.getSellerId());

        // 참여자 추가 검증
        verify(chatRoomMemberRepository, times(2)).save(any(ChatRoomMember.class));
    }

    // 1:10 채팅방 생성 테스트
    @Test
    public void testCreateOneToManyChatRoom_Success() {
        Long productId = 1L;
        Long sellerId = 3L;

        // 새 1:10 채팅방 생성
        when(chatRoomRepository.save(any(ChatRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChatRoom chatRoom = chatRoomService.createOneToManyChatRoom(productId, sellerId);

        // 검증
        assertNotNull(chatRoom);
        assertEquals(productId, chatRoom.getProductId());
        assertEquals(sellerId, chatRoom.getSellerId());

        // 채팅방 생성 검증
        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
    }

    // 기존 채팅방 존재하는 경우 1:1 채팅방 생성 테스트
    @Test
    public void testCreateOneToOneChatRoom_ExistingRoom() {
        Long productId = 1L;
        Long buyerId = 2L;
        Long sellerId = 3L;

        // 기존 채팅방이 존재하는 경우
        ChatRoom existingRoom = new ChatRoom(productId, null, sellerId, buyerId);
        when(chatRoomRepository.findBySellerIdAndBuyerIdAndProductId(sellerId, buyerId, productId))
                .thenReturn(Optional.of(existingRoom));

        ChatRoom chatRoom = chatRoomService.createOneToOneChatRoom(productId, buyerId, sellerId);

        // 기존 채팅방이 반환되는지 확인
        assertEquals(existingRoom, chatRoom);

        // 새 채팅방이 저장되지 않았음을 확인
        verify(chatRoomRepository, times(0)).save(any(ChatRoom.class));
    }

    // 채팅방 참여자 추가 테스트
    @Test
    public void testAddParticipant_Success() {
        Long chatRoomId = 1L;
        Long memberId = 2L;

        ChatRoom chatRoom = new ChatRoom(1L, null, 3L, 2L);
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));
        when(chatRoomMemberRepository.countByChatRoomId(chatRoomId)).thenReturn(0L);

        chatRoomService.addParticipant(chatRoomId, memberId, false, true);

        // 참여자 추가가 잘 동작하는지 확인
        verify(chatRoomMemberRepository, times(1)).save(any(ChatRoomMember.class));
    }

    // 채팅방 참여자 추가 시 최대 인원 초과 테스트
    @Test
    public void testAddParticipant_MaxParticipantsExceeded() {
        Long chatRoomId = 1L;
        Long memberId = 2L;

        ChatRoom chatRoom = new ChatRoom(1L, null, 3L, 2L);
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));
        when(chatRoomMemberRepository.countByChatRoomId(chatRoomId)).thenReturn(10L); // 최대 인원 초과

        assertThrows(IllegalStateException.class, () -> chatRoomService.addParticipant(chatRoomId, memberId, false, true));

        // 참여자가 저장되지 않았음을 확인
        verify(chatRoomMemberRepository, times(0)).save(any(ChatRoomMember.class));
    }

    // 채팅방 참여 요청 승인 테스트
    @Test
    public void testApproveParticipant_Success() {
        Long chatRoomId = 1L;
        Long memberId = 2L;

        ChatRoomMember chatRoomMember = new ChatRoomMember(chatRoomId, memberId, false, false);
        when(chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId))
                .thenReturn(chatRoomMember);

        chatRoomService.approveParticipant(chatRoomId, memberId);

        // 승인 상태 업데이트 확인
        assertTrue(chatRoomMember.isApproved());
        verify(chatRoomMemberRepository, times(1)).save(chatRoomMember);
    }

    // 채팅방 참여 요청 승인 실패 테스트 (존재하지 않는 참여자)
    @Test
    public void testApproveParticipant_NotFound() {
        Long chatRoomId = 1L;
        Long memberId = 2L;

        when(chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> chatRoomService.approveParticipant(chatRoomId, memberId));

        // 저장되지 않았음을 확인
        verify(chatRoomMemberRepository, times(0)).save(any(ChatRoomMember.class));
    }
}
