package com.youngs.dto;

public interface SummaryListDTO {
    String getContext(); //한줄 요약 내용
    String getCategory(); // //카테고리 명 - 기초지식 or 보도자료
    String getTitle(); //기초지식 or 보도자료 타이틀
    Long getCategorySeq(); //기초지식 or 보도자료 인덱스
    Long getArticleSeq(); // 세부 기초지식 or 보도자료 인덱스
    String getModifiedAt(); //수정일
}
