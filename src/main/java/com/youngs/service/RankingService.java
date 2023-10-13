package com.youngs.service;

import com.youngs.dto.UserRankDTO;

import java.util.List;

public interface RankingService {
    List<UserRankDTO> getTop30UsersByPoint (Long userSeq);
}
