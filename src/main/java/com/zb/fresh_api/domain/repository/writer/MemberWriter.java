package com.zb.fresh_api.domain.repository.writer;

import com.zb.fresh_api.domain.annotation.Writer;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.repository.jpa.MemberJpaRepository;
import com.zb.fresh_api.domain.repository.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class MemberWriter {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberQueryRepository memberQueryRepository;

    public Member store(Member member) {
        return memberJpaRepository.save(member);
    }

}
