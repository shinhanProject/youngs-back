package com.youngs.service;

import com.youngs.dto.NewsArticleDTO;
import com.youngs.entity.NewsArticle;
import com.youngs.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
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
    public NewsArticle getByArticle(Long categorySeq, Long newsSeq) throws RuntimeException {
        NewsArticle article =  newsRep.findByNewsCategoryCategorySeqAndNewsSeq(categorySeq, newsSeq);
        if(article == null){ //가져온 보도자료가 없다면
            throw new RuntimeException("조회할 보도자료가 없습니다");
        }
        return article;
    }
}
