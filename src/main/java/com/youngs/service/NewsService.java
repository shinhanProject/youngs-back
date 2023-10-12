package com.youngs.service;

import com.youngs.dto.NewsArticleDTO;
import java.util.List;

public interface NewsService {

    /**
     * 카테고리에 해당하는 보도자료 조회
     * @author 이지은
     * @param categorySeq 보도자료 카테고리 인덱스. 1: 경제, 2: 증권, 3: 부동산
     * */
    List<NewsArticleDTO> selectByCategorySeq(Long categorySeq);
}
