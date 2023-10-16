package com.youngs.repository;

import com.youngs.entity.UserSand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSandRepository extends JpaRepository<UserSand, Integer> {
    List<UserSand> findAllByUserUserSeq(Long userSeq);

    @Query(value = "select IFNULL(total, 0) from (select sum(count) as total from user_sand where user_seq=?1) sand", nativeQuery = true)
    int getByCountAndUserUserSeq(Long userSeq);
}
