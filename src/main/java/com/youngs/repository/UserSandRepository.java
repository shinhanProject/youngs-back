package com.youngs.repository;

import com.youngs.entity.UserSand;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserSandRepository extends JpaRepository<UserSand, Integer> {
    List<UserSand> findAllByUserUserSeq(Long userSeq);
}
