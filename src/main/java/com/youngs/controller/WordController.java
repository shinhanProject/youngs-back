package com.youngs.controller;

import com.youngs.dto.ResponseDTO;
import com.youngs.entity.Word;
import com.youngs.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/word")
public class WordController {
    private final WordService wordService;

    /**
     * 용어에 대한 설명 조회 API
     * @author 이지은
     * @param name 의미를 검색할 용어
     * @return 용어의 설명
     * */
    @GetMapping("/{name}")
    public ResponseEntity<?> searchName(@PathVariable("name") String name){
        try{
            Map<String, String> response = new HashMap<>();
            // 단어 DB에 해당하는 단어가 있는지 확인
            Word word = wordService.getByName(name);

            // 만약 없다면 ChatGPT를 통해 값을 가져온다.
            if (word == null) {
                String description = wordService.getWordDefinition(name);
                if (description == null) { //chatGPT에서 가져온 값이 없다면
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("단어 조회 실패");
                } else {
                    Word newWord = Word.builder()
                            .name(name) // 단어 명칭
                            .description(description) // 단어 설명
                            .build();
                    // 단어 DB에 저장
                    wordService.insertWord(newWord);
                    // 전달할 값(설명) map에 담기
                    response.put("description", description);
                    return ResponseEntity.ok().body(response);
                }
            } else { //이미 있다면 단어 DB에서 설명을 조회
                response.put("description", word.getDescription());
                return ResponseEntity.ok().body(response);
            }
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }
}
