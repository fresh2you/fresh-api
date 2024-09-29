package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.entity.member.Word;
import com.zb.fresh_api.domain.enums.member.WordType;
import com.zb.fresh_api.domain.repository.jpa.WordJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class WordReader {

    private final WordJpaRepository wordJpaRepository;

    public List<Word> getAllAdjectiveWord() {
        return wordJpaRepository.findAllByType(WordType.ADJECTIVE);
    }

    public List<Word> getAllNounWord() {
        return wordJpaRepository.findAllByType(WordType.NOUN);
    }

}
