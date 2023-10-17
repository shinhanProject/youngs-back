package com.youngs.controller;

import com.youngs.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockController {
    /**
     * 주식 차트 데이터 가져오기
     * @author 김태우
     * @param name 데이터를 가져올 종목 이름
     * @return 차트 데이터 전달
     */
    @GetMapping("/{name}/chart")
    public ResponseEntity<?> getChart(@PathVariable(value = "name") String name) {
        System.out.println(name);
        // 이름으로 stockCode조회 -> stockCode.corp_name = name -> corp_code 받아오기
        // corp_code로 s3에서 해당하는 차트 데이터 가져오기 -> 얼마나?
        // 해당 데이터 반환 (List 형태? - ResponseDTO가 list를 반환한다.
        return null;
    }

    /**
     * 주식 재무제표 데이터 가져오기
     * @author 김태우
     * @param name 데이터를 가져올 종목 이름
     * @return 재무제표 데이터 전달
     */
    @GetMapping("/{name}/stat")
    public ResponseEntity<?> getFnStatements(@PathVariable(value = "name") String name) {
        return null;
    }

}
