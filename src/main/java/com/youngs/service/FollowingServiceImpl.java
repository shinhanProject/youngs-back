package com.youngs.service;

import com.youngs.dto.ResponseDTO;
import com.youngs.entity.Following;
import com.youngs.entity.User;
import com.youngs.repository.FollowingRepository;
import com.youngs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowingServiceImpl implements FollowingService{
    private final UserRepository userRep;
    private final FollowingRepository followingRep;

    /**
     * 팔로우하기
     * @author : 박상희, 이지은
     * @param currentUserSeq : 로그인한 사용자의 고유 번호
     * @param targetUserSeq : 팔로우할 사용자의 고유 번호
     * @return - 200 : 팔로우 성공
     * @return - 403 : 로그인하지 않은 사용자의 요청이므로 팔로우 실패 (Spring Security의 설정으로 로그인하지 않은 사용자의 접근 제한)
     * @return - 500 : 팔로우 실패
     * @throws RuntimeException - 팔로우 신청한 사용자 또는 팔로우할 사용자를 찾지 못할 경우, 이미 팔로우한 사용자일 경우, 로그인한 사용자가 본인을 팔로우할 경우
     **/
    @Override
    public ResponseEntity<?> saveFollowing(Long currentUserSeq, Long targetUserSeq) throws RuntimeException {
        try {
            if (!currentUserSeq.equals(targetUserSeq)) { // 로그인한 사용자와 팔로우 대상자가 다를 경우
                User user = userRep.findByUserSeq(currentUserSeq);
                User targetUser = userRep.findByUserSeq(targetUserSeq);

                if (user != null && targetUser != null) { // 사용자와 팔루우할 대상이 모두 null이 아닐 때
                    // 팔로우가 되어 있는지 체크
                    Following following = followingRep.findByFollowerAndAndFollowing(currentUserSeq, targetUserSeq);
                    if (following != null) { // 팔로우 중일 경우
                        throw new RuntimeException("이미 팔로우한 대상자입니다.");
                    }

                    Following newFollowing = Following.builder()
                            .follower(user)
                            .following(targetUser)
                            .build();
                    followingRep.save(newFollowing);
                }
                else {
                    throw new RuntimeException("대상자를 찾을 수 없습니다.");
                }
            }
            else { // 로그인한 사용자와 팔로우 대상자가 같을 경우
                throw new RuntimeException("팔로우 대상자가 아닙니다.");
            }

            return ResponseEntity.ok().body("팔로우에 성공했습니다.");
        }
        catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message("팔로우에 실패했습니다. " + e.getMessage()).build();

            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }

    /**
     * 언팔로우하기
     * @author : 박상희, 이지은
     * @param currentUserSeq : 로그인한 사용자 고유 번호
     * @param targetUserSeq : 언팔로우할 사용자의 고유 번호
     * @return - 200 : 언팔로우 성공
     * @return - 403 : 로그인하지 않은 사용자의 요청이므로 언팔로우 실패 (Spring Security의 설정으로 로그인하지 않은 사용자의 접근 제한)
     * @return - 500 : 언팔로우 실패
     * @throws RuntimeException - 팔로우되어 있는 사용자가 아닐 경우, 언팔로우 신청한 사용자 또는 언팔로우할 사용자를 찾지 못할 경우, 로그인한 사용자가 본인을 언팔로우할 경우
     **/
    @Override
    public ResponseEntity<?> deleteFollowing(Long currentUserSeq, Long targetUserSeq) {
        try {
            if (!currentUserSeq.equals(targetUserSeq)) { // 로그인한 사용자와 언팔로우 대상자가 다를 경우
                User user = userRep.findByUserSeq(currentUserSeq);
                User targetUser = userRep.findByUserSeq(targetUserSeq);

                if (user != null && targetUser != null) { // 사용자와 언팔로우할 대상이 모두 null이 아닐 때
                    // 팔로우가 되어 있는지 체크
                    Following following = followingRep.findByFollowerAndAndFollowing(currentUserSeq, targetUserSeq);
                    if (following == null) { // 팔로우되어 있지 않을 경우
                        throw new RuntimeException("팔로우되어 있지 않아서 언팔로우할 대상자가 아닙니다.");
                    }

                    followingRep.deleteByFollowerAndFollowing(user, targetUser);
                }
                else {
                    throw new RuntimeException("대상자를 찾을 수 없습니다.");
                }
            }
            else { // 로그인한 사용자와 언팔로우 대상자가 같을 경우
                throw new RuntimeException("언팔로우 대상자가 아닌 본인입니다.");
            }

            return ResponseEntity.ok().body("언팔로우에 성공했습니다.");
        }
        catch (Exception e) {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().message(e.getMessage()).build();

            return ResponseEntity
                    .internalServerError() // 500
                    .body(responseDTO);
        }
    }
}
