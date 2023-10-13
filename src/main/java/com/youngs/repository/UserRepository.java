package com.youngs.repository;

import com.youngs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    Boolean existsByEmail(String email);

    User findByEmailAndUserPw(String email, String userPw);

    /**
     * point를 기준으로 내림차순 조회
     * @author 이지은
     **/
    @Query(value = "select u from User u order by u.point desc")
    List<User> findAllOrderByPointDesc();
}
