package com.youngs.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngs.entity.Word;
import com.youngs.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Transactional
public class WordServiceImpl implements WordService {
    private final WordRepository wordRep;

    @Value("${openai.api-key}")
    private String OPENAI_API_KEY;
    private final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    /**
     * 용어에 해당하는 설명이 있는 지 조회
     * @author 이지은
     * @param name 검색할 단어
     * @return 단어의 정보(명칭, 설명)
     * */
    @Override
    public Word getByName(String name) {
        return wordRep.findByName(name);
    }

    /**
     * DB에 없는 용어와 설명 저장
     * @author 이지은
     * @param word Word엔티티. wordSeq와 createAt는 자동삽입
     * */
    @Override
    public void insertWord(Word word){
        wordRep.save(word);
    }

    /**
     * chatGPT로부터 단어에 해당하는 내용을 받아오기
     * @author 이지은
     * @param name 검색할 용어
     * @return 용어의 의미
     * */
    @Override
    public String getWordDefinition(String name){
        //100자 이내로 제한
        String[] messages = { "{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"}",
                "{\"role\": \"user\", \"content\": \"" + name + "에 대한 설명을 100자 이내로 답변해줘. \"}" };

        String model = "gpt-3.5-turbo"; //gpt 3.5 turbo 모델 사용
        double temperature = 0.5;
        int n = 1;
        try {
            URL url = new URL(OPENAI_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + OPENAI_API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String data = "{\"model\": \"" + model + "\", \"temperature\": " + temperature + ", \"n\": " + n
                    + ", \"messages\": [" + String.join(",", messages) + "]}";

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = data.getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }

            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = bufferedReader.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                String content = null;
                //chatGPT로부터 가져온 값이 비어있지 않을 때
                if(response.toString() != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(response.toString());
                    content = rootNode.get("choices").get(0).get("message").get("content").asText(); //content만 가져오기
                }

                return content; //content 값을 반환
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; //조회에 실패했을 때 null을 반환
    } //getWordDefinition end
}
