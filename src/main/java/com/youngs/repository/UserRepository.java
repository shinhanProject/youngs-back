package com.youngs.repository;

import com.youngs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 정보를 데이터베이스에서 조회 및 조작하는 데 사용되는 JpaRepository 인터페이스
 * User 엔티티와 상호 작용하여 사용자 정보를 관리한다.
 * @author : 박상희
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 해당 이메일을 기준으로 사용자 정보를 검색하는 메서드
     * @author : 박상희
     * @param email : 검색할 사용자 이메일
     * @return Optional<User> 객체로 래핑된 사용자 정보 (사용자 정보가 존재하지 않을 경우, 빈 Optional 반환)
     **/
    Optional<User> findByEmail(String email);

    /**
     * 해당 이메일이 데이터베이스에 존재하는지 확인하는 메서드
     * @author : 박상희
     * @param email : 확인할 이메일
     * @return - 이메일이 존재할 경우 : true
     * @return - 이메일이 존재하지 않을 경우 : false
     **/
    Boolean existsByEmail(String email);

    /**
     * 해당 이메일과 해당 비밀번호를 기준으로 사용자 정보를 검색하는 메서드
     * @author : 박상희
     * @param email : 사용자 이메일
     * @param userPw : 사용자 비밀번호
     * @return - 사용자 정보가 있을 경우 : 검색된 사용자 정보
     * @return - 사용자 정보가 없을 경우 : null
     **/
    User findByEmailAndUserPw(String email, String userPw);

    User findByUserSeq(Long userSeq);

    /**
     * 포인트를 기준으로 모든 사용자 정보를 내림차순으로 조회하는 메서드
     * @author : 이지은
     * @return 포인트를 기준으로 내림차순으로 정렬된 사용자 정보 목록
     **/
    @Query(value = "select u from User u order by u.point desc")
    List<User> findAllOrderByPointDesc();
}
