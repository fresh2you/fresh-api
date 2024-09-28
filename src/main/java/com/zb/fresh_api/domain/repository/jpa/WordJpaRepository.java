package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.member.Word;
import com.zb.fresh_api.domain.enums.member.WordType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordJpaRepository extends JpaRepository<Word, Long> {

    List<Word> findAllByType(WordType type);
}
