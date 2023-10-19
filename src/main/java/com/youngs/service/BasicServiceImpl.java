package com.youngs.service;

import com.youngs.dto.BasicArticleDTO;
import com.youngs.dto.ResponseDTO;
import com.youngs.entity.BasicArticle;
import com.youngs.entity.BasicSummary;
import com.youngs.repository.BasicRepository;
import com.youngs.repository.BasicSummaryRepository;
import com.youngs.security.PrincipalUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicServiceImpl implements BasicService{
    private final BasicRepository basicRep;
    private final BasicSummaryRepository basicSummaryRep;

    /**
     * 카테고리에 해당하는 기초지식 조회
     * @author 이지은
     * @param categorySeq 기초지식 카테고리 인덱스.  1: 주식 기초, 2: 주식 투자 기법, 3: 위험성, 4: 경제 기초 지식
     * @exception RuntimeException 가져온 기초지식 없다면 throw
     * @return  사용자가 공부한 자료를 제외하고 리턴 - isChecked==true 이며, 로그인한 사용자일 때
     * @return  모든 자료 리턴 - isChecked==false 및 로그인하지 않은 사용자일 때
     * */
    @Override
    public List<BasicArticleDTO> searchBasicArticleList(PrincipalUserDetails currentUserDetails, Long categorySeq, boolean isChecked) throws RuntimeException  {
        List<BasicArticle> basicArticle;
        if(isChecked && currentUserDetails != null){ //'공부한 거 안보기 체크'하고 로그인한 사용자일 때
            Long currentUserSeq = currentUserDetails.getUserSeq(); //로그인한 사용자 고유 번호
            basicArticle = basicRep.findAllByUserNotStudy(categorySeq, currentUserSeq);
        } else {
            //모든 자료
            basicArticle = basicRep.findAllByBasicCategoryCategorySeq(categorySeq);
        }
        if(basicArticle.isEmpty()){ //가져온 기초지식 정보가 없다면
            throw new RuntimeException("카테고리에 해당하는 기초지식 자료가 없습니다");
        }
        List<BasicArticleDTO> basicArticleList = new ArrayList<>();
        for(BasicArticle basic : basicArticle){
            basicArticleList.add(new BasicArticleDTO(basic.getBasicSeq(), basic.getSubject(), basic.getDescription(), false));
        }
        return basicArticleList;
    }

    /**
     * 카테고리에 해당하는 세부 기초 지식 조회
     * @author 이지은
     * @param currentUserDetails : 현재 로그인한 사용자 정보
     * @param categorySeq 기초 지식 카테고리 인덱스.  1: 주식 기초, 2: 주식 투자 기법, 3: 위험성, 4: 경제 기초 지식
     * @param basicSeq 기초 지식 인덱스
     * @exception RuntimeException 가져온 세부 기초지식 없다면 throw
     * @return 세부 기초 지식
     * */
    @Override
    public ResponseEntity<?> searchBasicArticle(PrincipalUserDetails currentUserDetails, Long categorySeq, Long basicSeq) throws RuntimeException {
        try {
            BasicArticle basicArticle = basicRep.findByBasicCategoryCategorySeqAndBasicSeq(categorySeq, basicSeq);
            if(basicArticle == null){ //가져온 기초지식 정보가 없다면
                throw new RuntimeException("조회할 기초지식 자료가 없습니다");
            }
            
            boolean wasWritten = false; //초기 false
            if (currentUserDetails != null) { //로그인한 사용자일 때
                Long currentUserSeq = currentUserDetails.getUserSeq(); //로그인한 사용자 고유 번호
                //해당 자료에 사용자가 요약 작성을 했는 지 확인
                BasicSummary basicSummary = basicSummaryRep.findByUserUserSeqAndBasicArticleBasicSeq(currentUserSeq, basicSeq);
                wasWritten = (basicSummary != null); // true : 이미 요약된 기록 존재, false: 요약된 적이 없음
            }
            
            BasicArticleDTO basicArticleDTO = new BasicArticleDTO(basicArticle.getBasicSeq(), basicArticle.getSubject(), basicArticle.getContext(), wasWritten);
            return ResponseEntity.ok().body(basicArticleDTO);
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }
}
