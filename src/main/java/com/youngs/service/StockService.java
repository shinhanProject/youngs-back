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

    @Transactional
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

    @Transactional
    public Map<String, Object> getStockStat(String corp_code) {
        // corp_code로 재무제표 데이터 받아오기\
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int lastYear = today.minusYears(1).getYear();
        int month = today.getMonthValue();
        Map<String, Object> stockData = new HashMap<>();

        String key = corp_code + "/" + lastYear + "-" + month + ".json"; // s3 키 파일 값
        String t_key = corp_code + "/totaldata/" + year + "-" + month + ".json";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream objectContent = s3Service.downloadObject(key);
            InputStream t_objectContent = s3Service.downloadObject(t_key);

            JsonNode jsonData = objectMapper.readTree(objectContent);
            JsonNode t_jsonData = objectMapper.readTree(t_objectContent);

            // ------- 기본 데이터 -------
            // 당기순이익 - netProfit
            if (jsonData.has("당기순이익(손실)")) {
                stockData.put("netProfit", "-" + jsonData.get("당기순이익(손실)").get("thstrm_amount"));
            } else {
                stockData.put("netProfit", jsonData.get("당기순이익").get("thstrm_amount"));
            }
            // 자본총계: totalEquity
            stockData.put("totalEquity", jsonData.get("자본총계").get("thstrm_amount"));
            //  자산총계: totalAssets
            stockData.put("totalAssets", jsonData.get("자산총계").get("thstrm_amount"));
            //  부채총계: totalLiabilities
            stockData.put("totalLiabilities", jsonData.get("부채총계").get("thstrm_amount"));
            // 유동자산: currentAssets
            stockData.put("currentAssets", jsonData.get("유동자산").get("thstrm_amount"));
            // 유동부채: currentLiabilities
            stockData.put("currentLiabilities", jsonData.get("유동부채").get("thstrm_amount"));
            // 영업이익: operatingProfit
            if (jsonData.has("영업이익(손실)")) {
                stockData.put("operatingProfit", "-" + jsonData.get("영업이익(손실)").get("thstrm_amount"));
            } else {
                stockData.put("operatingProfit", "" + jsonData.get("영업이익").get("thstrm_amount"));
            }
            // 매출액: revenue
            if (jsonData.has("매출액")) {
                stockData.put("revenue", jsonData.get("매출액").get("thstrm_amount"));
            } else {
                stockData.put("revenue", jsonData.get("수익(매출액)").get("thstrm_amount"));
            }
            // 배당금지급: dividendsPaid
            if (jsonData.has("배당금지급")) {
                stockData.put("dividendsPaid", jsonData.get("배당금지급").get("thstrm_amount"));
            }else {
                stockData.put("dividendsPaid", jsonData.get("배당금의 지급").get("thstrm_amount"));
            }
            // 시가총액: marketCap
            stockData.put("marketCap", t_jsonData.get("시가총액"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return stockData;
    }
}
