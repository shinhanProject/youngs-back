package com.youngs.service;

import com.youngs.dto.SummaryDTO;
import org.springframework.http.ResponseEntity;

public interface SummaryService {
    /**
     * 요약 조회
     * @author : 박상희
     * @param userSeq : 요약을 조회할 사용자의 고유 번호
     * @param category : 조회할 요약의 카테고리
     * @param articleId : 조회할 요약이 작성되어 있는 글의 고유 번호
     * @return - 200 : 요약 조회 성공
     * @return - 403 : 로그인하지 않은 사용자의 요청이므로 요약 조회 실패 (Spring Security의 설정으로 로그인하지 않은 사용자의 접근 제한)
     * @return - 500 : 요약 조회 실패
     **/
    ResponseEntity<?> searchSummary(Long userSeq, String category, Long articleId);

    /**
     * 기초 지식 요약 작성
     * @author : 박상희
     * @param userSeq : 요약을 작성할 사용자의 고유 번호
     * @param summaryDTO : 작성할 요약 정보
     * @return - 200 : 기초 지식 요약 작성 성공
     * @return - 403 : 로그인하지 않은 사용자의 요청이므로 기초 지식 요약 작성 실패 (Spring Security의 설정으로 로그인하지 않은 사용자의 접근 제한)
     * @return - 500 : 기초 지식 요약 작성 실패
     **/
    ResponseEntity<?> writeBasicSummary(Long userSeq, SummaryDTO summaryDTO);

    /**
     * 보도자료 요약 작성
     * @author : 박상희
     * @param userSeq : 요약을 작성할 사용자의 고유 번호
     * @param summaryDTO : 작성할 요약 정보
     * @return - 200 : 보도자료 요약 작성 성공
     * @return - 403 : 로그인하지 않은 사용자의 요청이므로 보도자료 요약 작성 실패 (Spring Security의 설정으로 로그인하지 않은 사용자의 접근 제한)
     * @return - 500 : 보도자료 요약 작성 실패
     **/
    ResponseEntity<?> writeNewsSummary(Long userSeq, SummaryDTO summaryDTO);

    /**
     * 요약 수정
     * @author : 박상희
     * @param userSeq : 요약을 수정할 사용자의 고유 번호
     * @param summaryDTO : 수정할 요약 정보
     * @return - 200 : 요약 수정 성공
     * @return - 403 : 로그인하지 않은 사용자의 요청이므로 요약 수정 실패 (Spring Security의 설정으로 로그인하지 않은 사용자의 접근 제한)
     * @return - 500 : 요약 수정 실패
     **/
    ResponseEntity<?> editSummary(Long userSeq, SummaryDTO summaryDTO);
}
