package com.youngs.service;

import com.youngs.dto.BasicArticleDTO;
import com.youngs.entity.BasicArticle;
import com.youngs.repository.BasicRepository;
import com.youngs.security.PrincipalUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicServiceImpl implements BasicService{
    private final BasicRepository basicRep;

    /**
     * 카테고리에 해당하는 기초지식 조회
     * @author 이지은
     * @param categorySeq 기초지식 카테고리 인덱스.  1: 주식 기초, 2: 주식 투자 기법, 3: 위험성, 4: 경제 기초 지식
     * @exception RuntimeException 가져온 기초지식 없다면 throw
     * @return  사용자가 공부한 자료를 제외하고 리턴 - isChecked==true 이며, 로그인한 사용자일 때
     * @return  모든 자료 리턴 - isChecked==false 및 로그인하지 않은 사용자일 때
     * */
    @Override
    public List<BasicArticleDTO> searchBasicArticleList(PrincipalUserDetails currentUserDetails, Long categorySeq, boolean isChecked) {
        List<BasicArticle> basicArticle;
        System.out.println("isChecked : " + isChecked);
        if(isChecked && currentUserDetails != null){ //'공부한 거 안보기 체크'하고 로그인한 사용자일 때
            Long currentUserSeq = currentUserDetails.getUserSeq(); //로그인한 사용자 고유 번호
            basicArticle = basicRep.findAllByUserNotStudy(categorySeq, currentUserSeq);
        } else {
            //모든 자료
            basicArticle = basicRep.findAllByBasicCategoryCategorySeq(categorySeq);
        }
        List<BasicArticleDTO> basicArticleList = new ArrayList<>();
        for(BasicArticle basic : basicArticle){
            basicArticleList.add(new BasicArticleDTO(basic.getBasicSeq(), basic.getSubject(), basic.getDescription()));
        }
        return basicArticleList;
    }

}
