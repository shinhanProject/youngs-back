package com.youngs.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.querydsl.jpa.OpenJPATemplates;
import com.youngs.dto.ResponseDTO;
import com.youngs.service.StockChartService;
import com.youngs.service.StockService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockController {
    private final StockChartService stockChartService;
    private final StockService stockService;
    /**
     * 주식 차트 데이터 가져오기
     *
     * @param name 데이터를 가져올 종목 이름
     * @return 차트 데이터 전달
     * @author 김태우
     */
    @GetMapping("/chart/{name}")
    public ResponseEntity<?> getChart(@PathVariable(value = "name") String name) throws IOException {
        try { // 정상 결과
            String ChartData = stockChartService.getStockChart(name);
            return ResponseEntity.ok().body(ChartData);
        } catch (Exception e) { // S3 정보 없음을 제외한 오류
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message("일치하는 정보 없음.").build();
            return ResponseEntity
                    .internalServerError()
                    .body(responseDTO);
        }
    }

    /**
     * 주식 재무제표 데이터 가져오기
     *
     * @param name 데이터를 가져올 종목 이름
     * @return 재무제표 데이터 전달
     * @author 김태우
     */
    @GetMapping("/stat/{name}")
    public ResponseEntity<Object> getFnStatements(@PathVariable(value = "name") String name) {
        return null;
    }

    /**
     * 전체 주식 리스트 가져오기
     * @return 전체 주식 리스트
     */
    @GetMapping()
    public ResponseEntity<?> getStocks() {
        try {
            List<Map<String, Object>> result = stockService.getStocks();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError()
                    .body(responseDTO);
        }
    }

    /**
     * 등락률 기준 오늘의 종목 데이터 가져오기
     * @return 오늘의 종목 데이터 3개
     */
    @GetMapping("/today-stock")
    public ResponseEntity<?> getTodayStocks() {
        try {
            List<Map<String, Object>> result = stockService.getTodayStock();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError()
                    .body(responseDTO);
        }
    }

}
