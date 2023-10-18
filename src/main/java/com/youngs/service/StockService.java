package com.youngs.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.youngs.repository.StockCorpCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@Service
public class StockService {
    private final StockCorpCodeRepository stockCodeRepository;
    private final S3Service s3Service;
    @Transactional
    public List<Map<String, Object>> getStocks() throws IOException {
        LocalDate today = LocalDate.now();
        String target_date = today.minusDays(1).format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
        int currentYear = today.getYear();

        // 종목 리스트 전체 - 이름, code
        List<ArrayList<String>> stockList = stockCodeRepository.findAllList();
        List<Map<String, Object>> dataList = new ArrayList<>();

        // 종목의 전일 종가 가져와서 추가
        for (int i = 0; i < stockList.size(); i++) {
            Map<String, Object> stockData = new HashMap<>();

            String corp_code = stockList.get(i).get(1);
            String corp_name = stockList.get(i).get(0);
            String key = corp_code + "/chart/" + currentYear + ".json"; // s3 키 파일 값

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                InputStream objectContent = s3Service.downloadObject(key);
                JsonNode jsonData = objectMapper.readTree(objectContent);
                String closeValue = jsonData.get(target_date).get("close").asText();
                stockData.put("name", corp_name);
                stockData.put("stockId", corp_code);
                stockData.put("price", closeValue);
                dataList.add(stockData);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        return dataList;
    }

    public List<Map<String, Object>> getTodayStock() {
        LocalDate today = LocalDate.now();
        String target_date = today.format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
        int currentYear = today.getYear();

        List<ArrayList<String>> stockList = stockCodeRepository.findAllList();
        List<Map<String, Object>> dataList = new ArrayList<>();

        // 각 종목별 등락률을 저장한 객체 만들기
        for (int i = 0; i < stockList.size(); i++) {
            Map<String, Object> stockData = new HashMap<>();

            String corp_code = stockList.get(i).get(1);
            String corp_name = stockList.get(i).get(0);
            String key = corp_code + "/morning/" + currentYear + ".json"; // s3 키 파일 값

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                InputStream objectContent = s3Service.downloadObject(key);
                JsonNode jsonData = objectMapper.readTree(objectContent);
                String closeValue = jsonData.get(target_date).get("fltRt").asText();
                stockData.put("name", corp_name);
                stockData.put("stockId", corp_code);
                stockData.put("rate", closeValue);
                dataList.add(stockData);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        dataList.sort((data1,data2) -> {
            String rateStr1 = data1.get("rate").toString();
            String rateStr2 = data2.get("rate").toString();

            // 문자열을 수치로 변환하여 정렬
            Double rate1 = Double.parseDouble(rateStr1);
            Double rate2 = Double.parseDouble(rateStr2);

            // 내림차순 정렬
            return Double.compare(rate2, rate1);
        });

        return dataList.subList(0, 3);
    }
}
