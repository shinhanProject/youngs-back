package com.youngs.repository;

import com.youngs.entity.BasicSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicSummaryRepository extends JpaRepository<BasicSummary, Long> {
    /**
     * 사용자 고유 번호와 기초 지식 글 고유 번호로 기초 지식 요약 조회
     * @author : 박상희
     * @param userSeq : 사용자 고유 번호
     * @param basicSeq : 기초 지식 글 고유 번호
     * @return 기초 지식 요약
     **/
    BasicSummary findByUserUserSeqAndBasicArticleBasicSeq(Long userSeq, Long basicSeq);

    /**
     * 사용자 고유 번호와 기초 지식 요약 고유 번호로 기초 지식 요약 조회
     * @author : 박상희
     * @param userSeq : 사용자 고유 번호
     * @param summarySeq : 기초 지식 요약 고유 번호
     * @return 기초 지식 요약
     **/
    BasicSummary findByUserUserSeqAndSummmarySeq(Long userSeq, Long summarySeq);
}
