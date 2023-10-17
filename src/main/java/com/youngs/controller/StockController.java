package com.youngs.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.youngs.dto.ResponseDTO;
import com.youngs.service.StockChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockController {
    private final StockChartService stockChartService;

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

}
