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
     * 요약 조회
     * @author : 박상희
     * @param userSeq : 요약을 조회할 사용자의 고유 번호
     * @param category : 조회할 요약의 카테고리
     * @param articleId : 조회할 요약이 작성되어 있는 글의 고유 번호
     * @return - 200 : 요약 조회 성공
     * @return - 403 : 로그인하지 않은 사용자의 요청이므로 요약 조회 실패 (Spring Security의 설정으로 로그인하지 않은 사용자의 접근 제한)
     * @return - 500 : 요약 조회 실패
     **/
    @Override
    public ResponseEntity<?> searchSummary(Long userSeq, String category, Long articleId) {
        try {
            SummaryDTO summaryDTO;

            if (category.equals("basic")) { // 조회할 요약의 카테고리가 '기초 지식'일 경우
                BasicSummary basicSummary = basicSummaryRepository.findByUserUserSeqAndBasicArticleBasicSeq(userSeq, articleId);
                if (basicSummary == null) { // 요약이 작성되어 있지 않을 경우
                    throw new RuntimeException("기초 지식 요약이 작성되어 있지 않습니다.");
                }

                summaryDTO = SummaryDTO.builder()
                                .summarySeq(basicSummary.getSummarySeq())
                                .context(basicSummary.getContext())
                                .wasWritten(true)
                                .build();
            }
            else if (category.equals("news")) { // 조회할 요약의 카테고리가 '보도자료'일 경우
                NewsSummary newsSummary = newsSummaryRepository.findByUserUserSeqAndNewsArticleNewsSeq(userSeq, articleId);
                if (newsSummary == null) {
                    throw new RuntimeException("보도자료 요약이 작성되어 있지 않습니다.");
                }

                summaryDTO = SummaryDTO.builder()
                        .summarySeq(newsSummary.getSummarySeq())
                        .context(newsSummary.getContext())
                        .wasWritten(true)
                        .build();
            }
            else { // 요약을 조회할 수 있는 카테고리의 글이 아닐 경우
                throw new RuntimeException("요약을 조회할 수 있는 카테고리가 아닙니다.");
            }

            return ResponseEntity.ok().body(summaryDTO);
        }
        catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // Error 500
                    .body(responseDTO);
        }
    }

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

    /**
     * 요약 수정
     * @author : 박상희
     * @param userSeq : 요약을 수정할 사용자의 고유 번호
     * @param summaryDTO : 수정할 요약 정보
     * @return - 200 : 요약 수정 성공
     * @return - 403 : 로그인하지 않은 사용자의 요청이므로 요약 수정 실패 (Spring Security의 설정으로 로그인하지 않은 사용자의 접근 제한)
     * @return - 500 : 요약 수정 실패
     **/
    @Override
    public ResponseEntity<?> editSummary(Long userSeq, SummaryDTO summaryDTO) {
        try {
            Long summarySeq = summaryDTO.getSummarySeq(); // 수정할 요약의 고유 번호
            String category = summaryDTO.getCategory(); // 수정할 요약의 카테고리
            String context = summaryDTO.getContext(); // 수정한 요약의 내용

            if (category.equals("basic")) { // 수정할 요약의 카테고리가 '기초 지식'일 경우
                BasicSummary basicSummary = basicSummaryRepository.findByUserUserSeqAndSummarySeq(userSeq, summarySeq); // 해당 사용자의 수정할 기초 지식 요약

                if (basicSummary == null) { // 요약 작성이 되어 있지 않을 경우
                    throw new RuntimeException("수정할 기초 지식 요약이 없습니다.");
                }

                basicSummary.setContext(context); // 기초 지식 요약 수정
            }
            else if (category.equals("news")) { // 수정할 요약의 카테고리가 '보도자료'일 경우
                NewsSummary newsSummary = newsSummaryRepository.findByUserUserSeqAndSummarySeq(userSeq, summarySeq); // 해당 사용자의 수정할 보도자료 요약

                if (newsSummary == null) { // 요약 작성이 되어 있지 않을 경우
                    throw new RuntimeException("수정할 보도자료 요약이 없습니다.");
                }

                newsSummary.setContext(context); // 보도자료 요약 수정
            }
            else { // 요약을 수정할 수 있는 카테고리의 글이 아닐 경우
                throw new RuntimeException("요약을 수정할 카테고리가 잘못되었습니다.");
            }

            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .message(category + " 요약을 수정했습니다. 수정한 요약 내용 : " + context)
                    .build();

            return ResponseEntity.ok().body(responseDTO);
        }
        catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // Error 500
                    .body(responseDTO);
        }
    }
}
