package com.youngs.service;

import com.youngs.entity.Word;

public interface WordService {
    /**
     * 단어에 해당하는 설명이 있는 지 조회
     * @author 이지은
     * @param name 검색할 단어
     * */
    Word getByName(String name);

    /**
     * 단어에 해당하는 설명이 있는 지 조회
     * @author 이지은
     * */
    void insertWord(Word word);

    /**
     * chatGPT로부터 단어에 해당하는 내용을 받아오기
     * @author 이지은
     * @param name 검색할 단어
     * */
    String getWordDefinition(String name);
}
