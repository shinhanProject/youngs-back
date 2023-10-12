package com.youngs.controller;

import com.youngs.dto.NewsArticleDTO;
import com.youngs.dto.ResponseDTO;
import com.youngs.entity.NewsArticle;
import com.youngs.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;

    /**
     * 카테고리에 해당하는 보도자료 조회 API
     * @author 이지은
     * @param categorySeq 보도자료 카테고리 인덱스. 1: 경제, 2: 증권, 3: 부동산
     * */
    @GetMapping("/{category_seq}")
    public ResponseEntity<?> searchCategory(@PathVariable("category_seq") Long categorySeq){
        try{
            // 해당하는 카테고리에 칼럼이 있는 지 조회
            List<NewsArticleDTO> newsArticleDTO = newsService.selectByCategorySeq(categorySeq);
            return ResponseEntity.ok().body(newsArticleDTO);
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }

    /**
     * 해당하는 보도자료 세부 조회 API
     * @author 이지은
     * @param categorySeq 카테고리 인덱스. 1: 경제, 2: 증권, 3: 부동산
     * @param newsSeq 보도자료 인덱스
     * */
    @GetMapping()
    public ResponseEntity<?> searchArticle(@RequestParam Long categorySeq, Long newsSeq){
        try{
            // 해당하는 세부 보도자료가 있는 지 조회
            NewsArticle newsArticle = newsService.getByArticle(categorySeq, newsSeq);
            return ResponseEntity.ok().body(newsArticle);
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }
}
