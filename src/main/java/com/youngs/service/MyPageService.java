package com.youngs.service;

import com.youngs.dto.UserSandDTO;

import java.util.List;

public interface MyPageService {
    /**
     *  모래사장 조회
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * */
    List<UserSandDTO> searchuserSandList(Long userSeq);
}
