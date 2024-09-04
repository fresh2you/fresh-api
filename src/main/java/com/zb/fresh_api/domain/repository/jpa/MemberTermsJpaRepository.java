package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.member.MemberTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTermsJpaRepository extends JpaRepository<MemberTerms,Long> {

}
