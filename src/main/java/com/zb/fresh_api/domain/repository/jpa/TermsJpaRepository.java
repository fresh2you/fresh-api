package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.terms.Terms;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsJpaRepository extends JpaRepository<Terms,Long> {
    List<Terms> findAllByIsRequired(boolean isRequired);
}
