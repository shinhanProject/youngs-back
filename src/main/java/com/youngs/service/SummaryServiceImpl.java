package com.youngs.service;

import com.youngs.dto.ResponseDTO;
import com.youngs.dto.SummaryDTO;
import com.youngs.entity.*;
import com.youngs.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {
    private final UserRepository userRepository;

    private final BasicRepository basicRepository;

    private final BasicSummaryRepository basicSummaryRepository;

    /**
     * 기초 지식 요약 작성
     * @author : 박상희
     * @param userSeq : 요약을 작성할 사용자의 고유 번호
     * @param summaryDTO : 작성할 요약 정보
     * @return - 200 : 기초 지식 요약 작성 성공
     * @return - 500 : 기초 지식 요약 작성 실패
     **/
    @Override
    public ResponseEntity<?> writeBasicSummary(Long userSeq, SummaryDTO summaryDTO) {
        try {
            User user = userRepository.findByUserSeq(userSeq);
            BasicArticle basicArticle = basicRepository.findByBasicSeq(summaryDTO.getArticleId());

            BasicSummary basicSummary = BasicSummary.builder()
                    .context(summaryDTO.getContext())
                    .user(user)
                    .basicArticle(basicArticle)
                    .build();

            basicSummaryRepository.save(basicSummary);

            return ResponseEntity.ok().body(basicSummary);
        }
        catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // Error 500
                    .body(responseDTO);
        }
    }
}
