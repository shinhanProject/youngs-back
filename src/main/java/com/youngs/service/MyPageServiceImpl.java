package com.youngs.service;

import com.youngs.dto.UserSandDTO;
import com.youngs.entity.UserSand;
import com.youngs.repository.UserSandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageServiceImpl implements  MyPageService{
    private final UserSandRepository userSandRep;

    /**
     * 모래사장 조회
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * @exception RuntimeException 가져온 모래사장 정보가 없다면 throw
     * @return 모래사장 데이터
     * */
    @Override
    public List<UserSandDTO> searchuserSandList(Long userSeq) {
        List<UserSand> userSandList = userSandRep.findAllByUserUserSeq(userSeq);
        if(userSandList.isEmpty()){ //가져온 모래사장이 비어있다면
            throw new RuntimeException("조회할 모래사장 정보가 없습니다.");
        }

        List<UserSandDTO> userSandDTOList = new ArrayList<>();
        for(UserSand clam: userSandList){
            String createdAt = clam.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            userSandDTOList.add(new UserSandDTO(clam.getCount(), createdAt));
        }
        return userSandDTOList;
    }
}
