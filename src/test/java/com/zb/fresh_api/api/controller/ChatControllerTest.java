package com.zb.fresh_api.api.controller;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.zb.fresh_api.api.service.ChatRoomService;
import com.zb.fresh_api.common.response.ApiResponse;
import com.zb.fresh_api.domain.entity.chat.ChatRoomMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ChatControllerTest {

    @Mock
    private ChatRoomService chatRoomService;

    @InjectMocks
    private ChatRoomController chatRoomController;

    private FixtureMonkey fixtureMonkey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fixtureMonkey = FixtureMonkey.builder().build();
    }

    @Test
    void testApproveParticipant() {
        // ChatRoomMember 생성
        ChatRoomMember chatRoomMember = fixtureMonkey.giveMeOne(ChatRoomMember.class);

        // approveParticipant 테스트
        ResponseEntity<ApiResponse<Void>> response = chatRoomController.approveParticipant(chatRoomMember.getChatRoomId(), chatRoomMember.getMemberId());

        // 응답 검증
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        // 서비스 메서드 호출 검증
        verify(chatRoomService, times(1)).approveParticipant(chatRoomMember.getChatRoomId(), chatRoomMember.getMemberId());
    }

    @Test
    void testGetPendingRequests() {
        // ChatRoomMember 리스트 생성
        List<ChatRoomMember> pendingRequests = fixtureMonkey.giveMe(ChatRoomMember.class, 3);
        when(chatRoomService.getPendingRequests(anyLong())).thenReturn(pendingRequests);

        // getPendingRequests 테스트
        ResponseEntity<ApiResponse<List<ChatRoomMember>>> response = chatRoomController.getPendingRequests(pendingRequests.get(0).getChatRoomId());

        // 응답 검증
        assertThat(response.getBody().data()).isEqualTo(pendingRequests);

        // 서비스 메서드 호출 검증
        verify(chatRoomService, times(1)).getPendingRequests(anyLong());
    }
}
