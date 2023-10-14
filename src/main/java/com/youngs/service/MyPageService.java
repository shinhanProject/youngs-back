package com.youngs.service;

import com.youngs.dto.FollowingDTO;
import com.youngs.dto.SummaryDTO;
import com.youngs.dto.UserSandDTO;
import com.youngs.entity.Following;

import java.util.List;

public interface MyPageService {
    /**
     *  모래사장 조회
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * */
    List<UserSandDTO> searchUserSandList(Long userSeq);

    /**
     * 팔로우 목록 조회
     * @author 이지은
     * @param userSeq 사용자 인덱스
     * */
    List<FollowingDTO> sarchFollowingList(Long userSeq);
}
