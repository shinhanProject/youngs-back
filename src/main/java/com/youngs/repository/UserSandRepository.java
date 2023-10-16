package com.youngs.repository;

import com.youngs.entity.UserSand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSandRepository extends JpaRepository<UserSand, Integer> {
    List<UserSand> findAllByUserUserSeq(Long userSeq);

    @Query(value = "select IFNULL(total, 0) from (select sum(count) as total from user_sand where user_seq=?1) sand", nativeQuery = true)
    int getByCountAndUserUserSeq(Long userSeq);

    /**
     * 사용자 고유 번호와 날짜를 기준으로 해당 날짜의 바다 조회
     * @author : 박상희
     * @param userSeq : 사용자 고유 번호
     * @param createdAt : 날짜
     * @return 해당 사용자의 해당 날짜의 바다가 있을 경우 바다 반환, 없을 경우 null 반환
     **/
    @Query(value = "select us from UserSand us where us.user.userSeq = ?1 and DATE_FORMAT(us.createdAt, '%Y-%m-%d') = ?2")
    UserSand findByUserUserSeqAndCreatedAtDate(Long userSeq, String createdAt);
}
