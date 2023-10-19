package com.youngs.service;

import com.youngs.dto.BasicArticleDTO;
import com.youngs.security.PrincipalUserDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BasicService {
    List<BasicArticleDTO> searchBasicArticleList(PrincipalUserDetails currentUserDetails, Long categorySeq, boolean isChecked);

    ResponseEntity<?> searchBasicArticle(PrincipalUserDetails currentUserDetails, Long categorySeq, Long basicSeq);
}
