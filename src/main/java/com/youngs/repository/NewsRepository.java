package com.youngs.repository;

import com.youngs.entity.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<NewsArticle, Integer> {
    /**
     * 카테고리에 해당하는 보도자료 리스트 최신 순으로 가져오기
     * @author 이지은
     * @param categorySeq 보도자료 카테고리 인덱스. 1: 경제, 2: 증권, 3: 부동산, 4: 기업∙경영
     * */
    List<NewsArticle> findAllByNewsCategoryCategorySeqOrderByPubDateDesc(Long categorySeq);

    NewsArticle findByNewsCategoryCategorySeqAndNewsSeq(Long categorySeq, Long newsSeq);

    /**
     * 해당 보도자료 고유 번호의 보도자료 가져오기
     * @author : 박상희
     * @param newsSeq : 보도자료 고유 번호
     * @return 보도자료
     */
    NewsArticle findByNewsSeq(Long newsSeq);
}
