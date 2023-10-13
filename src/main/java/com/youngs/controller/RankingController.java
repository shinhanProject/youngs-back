package com.youngs.controller;

import com.youngs.dto.ResponseDTO;
import com.youngs.dto.UserRankDTO;
import com.youngs.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rank")
public class RankingController {
    private final RankingService rankingService ;

    /**
     * 상위 30위 유저 랭킹 조회 API
     * @author 이지은
     * @param request userSeq 값
     * */
    @PostMapping()
    public ResponseEntity<?> searchRanking(@RequestBody Map<String, Long> request){
        try{
            // 상위 30위 조회
            List<UserRankDTO> userList = rankingService.getTop30UsersByPoint(request.get("userSeq"));
            return ResponseEntity.ok().body(userList);
        } catch (Exception e){
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();
            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }

}
