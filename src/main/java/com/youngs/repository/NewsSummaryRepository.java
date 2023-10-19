package com.youngs.repository;

import com.youngs.dto.SummaryListDTO;
import com.youngs.entity.NewsSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsSummaryRepository extends JpaRepository<NewsSummary, Long> {
    /**
     * 사용자 고유 번호와 보도자료 글 고유 번호로 보도자료 요약 조회
     * @author : 박상희
     * @param userSeq : 사용자 고유 번호
     * @param newsSeq : 보도자료 글 고유 번호
     * @return 보도자료 요약
     **/
    NewsSummary findByUserUserSeqAndNewsArticleNewsSeq(Long userSeq, Long newsSeq);

    /**
     * 사용자 고유 번호와 보도자료 요약 고유 번호로 보도자료 요약 조회
     * @author : 박상희
     * @param userSeq : 사용자 고유 번호
     * @param summarySeq : 보도자료 요약 고유 번호
     * @return 보도자료 요약
     **/
    NewsSummary findByUserUserSeqAndSummarySeq(Long userSeq, Long summarySeq);

    /**
     * 사용자 요약 정보 최신 순 조회
     * @author 이지은
     * @param userSeq 사용자 고유 번호
     * @return 최신순으로 정렬한 요약 정보 리스트
     * */
    @Query(value = "select s.context, 'news' as category, c.category_name as title, c.category_seq as categorySeq, s.news_seq as articleSeq, DATE_FORMAT(s.modified_at,'%Y-%m-%d') as modifiedAt\n" +
            "  from news_summary s\n" +
            " join news_article a on s.news_seq = a.news_seq\n" +
            " join news_category c on a.category_seq = c.category_seq\n" +
            "where s.user_seq=?1\n" +
            "union\n" +
            "select s.context, 'basic' as category, c.category_name as title, c.category_seq as categorySeq, s.basic_seq as articleSeq, DATE_FORMAT(s.modified_at,'%Y-%m-%d') as modifiedAt\n" +
            "from basic_summary s\n" +
            "join basic_article a on s.basic_seq = a.basic_seq\n" +
            "join basic_category c on a.category_seq = c.category_seq\n" +
            "where s.user_seq=?1\n" +
            "order by modifiedAt desc", nativeQuery = true)
    List<SummaryListDTO> findSummaryList(Long userSeq);
}
