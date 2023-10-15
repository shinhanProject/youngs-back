package com.youngs.repository;

import com.youngs.entity.BasicArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BasicRepository extends JpaRepository<BasicArticle, Integer> {
    List<BasicArticle> findAllByBasicCategoryCategorySeq(Long categorySeq);

    /**
     * 유저가 공부하지 않은 기초 자료 리스트
     * @author 이지은
     * @param categorySeq 기초지식 카테고리 인덱스
     * @param userSeq 사용자 인덱스
     * */
    @Query(value = "SELECT a.* FROM basic_article a WHERE a.category_seq = ?1 AND a.basic_seq NOT IN (SELECT s.basic_seq FROM basic_summary s WHERE s.user_seq = ?2)", nativeQuery = true)
    List<BasicArticle> findAllByUserNotStudy(Long categorySeq, Long userSeq);

}
