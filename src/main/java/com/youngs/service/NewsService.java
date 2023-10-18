package com.youngs.service;

import com.youngs.dto.NewsArticleDTO;
import com.youngs.security.PrincipalUserDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NewsService {

    /**
     * 카테고리에 해당하는 보도자료 조회
     * @author 이지은
     * @param categorySeq 보도자료 카테고리 인덱스. 1: 경제, 2: 증권, 3: 부동산
     * */
    List<NewsArticleDTO> selectByCategorySeq(Long categorySeq);

    /**
     * 세부 보도자료 조회
     * @author 이지은
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param categorySeq        : 조회할 사용자 고유 번호
     * @param newsSeq            : 세부 보도자료 인덱스
     * */
    ResponseEntity<?> getByArticle(PrincipalUserDetails currentUserDetails, Long categorySeq, Long newsSeq);
}
