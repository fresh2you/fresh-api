package com.zb.fresh_api.domain.repository;

import com.zb.fresh_api.domain.entity.terms.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsRepository extends JpaRepository<Terms, Long> {

}
