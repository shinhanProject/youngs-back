package com.youngs.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.youngs.repository.StockCorpCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class StockChartService {
    private final StockCorpCodeRepository stockCodeRepository;
    private final S3Service s3Service;

    @Transactional
    public String getStockChart(String corpName) throws IOException {
        int currentYear = LocalDate.now().getYear();

        String corpCode = stockCodeRepository.findByName(corpName); // 이름으로 회사 코드 조회
        String key = corpCode + "/chart/" + currentYear + ".json"; // s3 키 파일 값

        InputStream objectContent = s3Service.downloadObject(key);
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(objectContent))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content.toString();
    }
}
