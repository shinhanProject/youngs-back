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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StockService {
    private final StockCorpCodeRepository stockCodeRepository;
    private final S3Service s3Service;
    @Transactional
    public List<ArrayList<String>> getStocks() throws IOException {
        LocalDate today = LocalDate.now();
        String target_date = today.minusDays(1).format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
        int currentYear = today.getYear();

        // 종목 리스트 전체 - 이름, code
        List<ArrayList<String>> stockList = stockCodeRepository.findAllList();
        // 종목의 전일 종가 가져와서 추가
        for (int i = 0; i < stockList.size(); i++) {
            String key = stockList.get(i).get(1) + "/chart/" + currentYear + ".json"; // s3 키 파일 값

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                InputStream objectContent = s3Service.downloadObject(key);
                JsonNode jsonData = objectMapper.readTree(objectContent);
                String closeValue = jsonData.get(target_date).get("close").asText();
                stockList.get(i).add(closeValue);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        return stockList;
    }
}
