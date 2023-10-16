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

import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {
    private final UserRepository userRepository;

    private final UserSandRepository userSandRepository;

    private final BasicRepository basicRepository;

    private final BasicSummaryRepository basicSummaryRepository;

    private final NewsRepository newsRepository;

    private final NewsSummaryRepository newsSummaryRepository;

    /**
     * 기초 지식 요약 작성 및 바다 기록
     * @author : 박상희
     * @param userSeq : 요약을 작성할 사용자의 고유 번호
     * @param summaryDTO : 작성할 요약 정보
     * @return - 200 : 기초 지식 요약 작성 성공
     * @return - 500 : 기초 지식 요약 작성 실패, 이미 기초 지식 요약이 작성되어 있을 경우
     **/
    @Override
    public ResponseEntity<?> writeBasicSummary(Long userSeq, SummaryDTO summaryDTO) {
        try {
            Long basicSeq = summaryDTO.getArticleId(); // 요약을 작성할 기초 지식 글의 고유 번호

            if (basicSummaryRepository.findByUserUserSeqAndBasicArticleBasicSeq(userSeq, basicSeq) != null) { // 이미 요약 작성이 되어 있을 경우
                throw new RuntimeException("이미 기초 지식 요약이 작성되어 있습니다.");
            }

            User user = userRepository.findByUserSeq(userSeq); // 요약을 작성할 사용자
            BasicArticle basicArticle = basicRepository.findByBasicSeq(basicSeq); // 요약을 작성할 기초 지식 글

            BasicSummary basicSummary = BasicSummary.builder()
                    .context(summaryDTO.getContext())
                    .user(user)
                    .basicArticle(basicArticle)
                    .build();

            basicSummaryRepository.save(basicSummary); // 기초 지식 요약 작성

            addSea(user, userSeq, basicSummary.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))); // 바다 기록

            return ResponseEntity.ok().body(basicSummary);
        }
        catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // Error 500
                    .body(responseDTO);
        }
    }

    /**
     * 보도자료 요약 작성 및 바다 기록
     * @author : 박상희
     * @param userSeq : 요약을 작성할 사용자의 고유 번호
     * @param summaryDTO : 작성할 요약 정보
     * @return - 200 : 보도자료 요약 작성 성공
     * @return - 500 : 보도자료 요약 작성 실패, 이미 보도자료 요약이 작성되어 있을 경우
     **/
    @Override
    public ResponseEntity<?> writeNewsSummary(Long userSeq, SummaryDTO summaryDTO) {
        try {
            Long newsSeq = summaryDTO.getArticleId(); // 요약을 작성할 보도자료 고유 번호

            if (newsSummaryRepository.findByUserUserSeqAndNewsArticleNewsSeq(userSeq, newsSeq) != null) { // 이미 요약 작성이 되어 있을 경우
                throw new RuntimeException("이미 보도자료 요약이 작성되어 있습니다.");
            }

            User user = userRepository.findByUserSeq(userSeq); // 요약을 작성할 사용자
            NewsArticle newsArticle = newsRepository.findByNewsSeq(newsSeq); // 요약을 작성할 보도자료 글

            NewsSummary newsSummary = NewsSummary.builder()
                    .context(summaryDTO.getContext())
                    .user(user)
                    .newsArticle(newsArticle)
                    .build();

            newsSummaryRepository.save(newsSummary); // 보도자료 요약 작성

            addSea(user, userSeq, newsSummary.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))); // 바다 기록

            return ResponseEntity.ok().body(newsSummary);
        }
        catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // Error 500
                    .body(responseDTO);
        }
    }

    /**
     * 바다 기록
     * @author : 박상희
     * @param user : 바다를 기록할 사용자
     * @param userSeq : 바다를 기록할 사용자의 고유 번호
     * @param createdAt : 바다를 기록할 날짜
     **/
        public void addSea(User user, Long userSeq, String createdAt) {
        if (userSandRepository.findByUserUserSeqAndCreatedAtDate(userSeq, createdAt) == null) { // 해당 사용자의 해당 날짜의 바다가 없을 경우
            UserSand userSand = UserSand.builder()
                            .user(user)
                            .count(1)
                            .build();

            userSandRepository.save(userSand); // 바다 생성
        }
        else { // 해당 사용자의 해당 날짜의 바다가 있을 경우
            UserSand userSand = userSandRepository.findByUserUserSeqAndCreatedAtDate(userSeq, createdAt); // 해당 사용자의 해당 날짜의 바다
            userSand.setCount(userSand.getCount() + 1); // 바다 기록 개수 추가
        }
    }
}
