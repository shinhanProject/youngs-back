package com.youngs.repository;

import com.youngs.entity.UserSand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSandRepository extends JpaRepository<UserSand, Integer> {
    List<UserSand> findAllByUserUserSeq(Long userSeq);

    @Query(value = "select sand.* from (select count(*) from user_sand u where user_seq=?1) sand", nativeQuery = true)
    int getByCountAndUserUserSeq(Long userSeq);
}
