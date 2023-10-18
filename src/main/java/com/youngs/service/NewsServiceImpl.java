package com.youngs.service;

import com.youngs.dto.NewsArticleDTO;
import com.youngs.dto.ResponseDTO;
import com.youngs.entity.NewsArticle;
import com.youngs.entity.NewsSummary;
import com.youngs.repository.NewsRepository;
import com.youngs.repository.NewsSummaryRepository;
import com.youngs.security.PrincipalUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRep;
    private final NewsSummaryRepository newsSummaryRep;

    /**
     * 카테고리에 해당하는 보도자료 조회
     * @author 이지은
     * @param categorySeq 보도자료 카테고리 인덱스. 1: 경제, 2: 증권, 3: 부동산
     * @exception RuntimeException 가져온 보도자료가 없다면 throw
     * @return 카테고리에 해당하는 보도자료 리스트
     * */
    @Override
    public List<NewsArticleDTO> selectByCategorySeq(Long categorySeq) throws RuntimeException {
        List<NewsArticle> newsList = newsRep.findAllByNewsCategoryCategorySeq(categorySeq);
        if(newsList.isEmpty()){ //가져온 보도자료가 없다면
            throw new RuntimeException("조회할 보도자료가 없습니다");
        }

        List<NewsArticleDTO> newsDTOList = new ArrayList<>();
        for(NewsArticle news : newsList){
            String pubDate = news.getPubDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            newsDTOList.add(new NewsArticleDTO(news.getNewsSeq(), news.getTitle(), news.getUrl(), news.getDescription(), pubDate));
        }
        return newsDTOList;
    }

    /**
     * 세부 보도자료 조회
     * @author 이지은
     * @param categorySeq  카테고리 인덱스. 1: 경제, 2: 증권, 3: 부동산
     * @param newsSeq 보도자료 인덱스
     * @exception RuntimeException 가져온 보도자료가 없다면 throw
     * @return 카테고리와 보조자료인덱스에 해당하는 보도자료
     * */
    public ResponseEntity<?> getByArticle(PrincipalUserDetails currentUserDetails, Long categorySeq, Long newsSeq) throws RuntimeException {
        try{
            NewsArticle article =  newsRep.findByNewsCategoryCategorySeqAndNewsSeq(categorySeq, newsSeq);
            if(article == null){ //가져온 보도자료가 없다면
                throw new RuntimeException("조회할 보도자료가 없습니다");
            }

            boolean wasWritten = false; //초기 false
            if(currentUserDetails != null){ //로그인한 사용자일 때
                Long currentUserSeq = currentUserDetails.getUserSeq(); //로그인한 사용자 고유 번호
                NewsSummary newsSummary = newsSummaryRep.findByUserUserSeqAndNewsArticleNewsSeq(currentUserSeq, newsSeq);
                wasWritten = (newsSummary != null); // true : 이미 요약된 기록 존재, false: 요약된 적이 없음
            }
            String pubDate = article.getPubDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            NewsArticleDTO newsArticleDTO = new NewsArticleDTO(article.getNewsSeq(), article.getTitle(), article.getUrl(), pubDate, wasWritten);
            return ResponseEntity.ok().body(newsArticleDTO);
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }
}
