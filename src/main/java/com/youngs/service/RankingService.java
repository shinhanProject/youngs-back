package com.youngs.service;

import com.youngs.dto.UserRankDTO;
import com.youngs.security.PrincipalUserDetails;

import java.util.List;

public interface RankingService {
    List<UserRankDTO> getTop30UsersByPoint (PrincipalUserDetails currentUserDetails);
}
