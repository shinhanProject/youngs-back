package com.youngs.repository;

import com.youngs.entity.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsRepository extends JpaRepository<NewsArticle, Integer> {

    /**
     * 카테고리에 해당하는 보도자료 리스트 가져오기
     * @author 이지은
     * @param categorySeq 보도자료 카테고리 인덱스. 1: 경제, 2: 증권, 3: 부동산
     * */
    @Query(value = "select n from NewsArticle n where n.categorySeq=?1")
    List<NewsArticle> selectByCategorySeq(Long categorySeq);
}
