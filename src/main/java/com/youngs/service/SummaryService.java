package com.youngs.service;

import com.youngs.dto.SummaryDTO;
import org.springframework.http.ResponseEntity;

public interface SummaryService {
    /**
     * 기초 지식 요약 작성
     * @author : 박상희
     * @param userSeq : 요약을 작성할 사용자의 고유 번호
     * @param summaryDTO : 작성할 요약 정보
     * @return - 200 : 기초 지식 요약 작성 성공
     * @return - 500 : 기초 지식 요약 작성 실패
     **/
    public ResponseEntity<?> writeBasicSummary(Long userSeq, SummaryDTO summaryDTO);
}
