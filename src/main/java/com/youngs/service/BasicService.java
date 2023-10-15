package com.youngs.service;

import com.youngs.dto.BasicArticleDTO;
import com.youngs.security.PrincipalUserDetails;

import java.util.List;

public interface BasicService {
    List<BasicArticleDTO> searchBasicArticleList(PrincipalUserDetails currentUserDetails, Long categorySeq, boolean isChecked);
}
