package com.zb.fresh_api.api.service;

import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.dto.chat.ChatMessageDto;
import com.zb.fresh_api.domain.entity.chat.ChatMessage;
import com.zb.fresh_api.domain.entity.chat.ChatRoomMember;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.repository.reader.ChatMessageReader;
import com.zb.fresh_api.domain.repository.reader.ChatRoomMemberReader;
import com.zb.fresh_api.domain.repository.reader.MemberReader;
import com.zb.fresh_api.domain.repository.writer.ChatMessageWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageReader chatMessageReader;
    private final ChatRoomMemberReader chatRoomMemberReader;
    private final MemberReader memberReader;
    private final ChatMessageWriter chatMessageWriter;

    public List<ChatMessageDto> getChatMessages(Long chatRoomId) {
        return chatMessageReader.getMessagesByChatRoomId(chatRoomId)
                .stream()
                .map(message -> {
                    ChatRoomMember sender = chatRoomMemberReader.findByChatRoomIdAndMemberId(Long.valueOf(chatRoomId), message.senderId())
                            .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_CHATROOM_MEMBER));

                    Member member = memberReader.getById(sender.getMemberId());
                    String senderName = member != null ? member.getNickname() : "Unknown User";

                    return new ChatMessageDto(message.chatMessageId(), message.senderId(), senderName, message.message());
                })
                .collect(Collectors.toList());
    }

    public void saveMessage(ChatMessage message) {
        chatMessageWriter.save(message);
    }
}
