package com.youngs.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 사용자 토큰 생성 및 유효성 검증 클래스
 * 사용자 정보를 기반으로 JWT를 생성하고, JWT 유효성을 검사하여 사용자의 인증을 처리한다.
 * @author : 박상희
 **/
@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "SECRET_KEY";

    /**
     * 사용자 정보를 기반으로 JWT를 생성하는 메서드
     * @author : 박상희
     * @param userDetails : 사용자 정보
     * @return 생성된 JWT 문자열
     **/
    public String create(UserDetails userDetails) {
        // 기한은 지금부터 1 일로 설정
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS));

        // JWT Token 생성
        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                // payload에 들어갈 내용
                .setSubject(userDetails.getUsername()) // sub  // email
                .setIssuer("Young's") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate) // exp
                .compact();
    }

    /**
     * JWT를 검증하고, 포함된 이메일을 반환하는 메서드
     * @author : 박상희
     * @param token : 체크할 JWT
     * @return JWT에 포함된 사용자 이메일
     **/
    public String validateAndGetEmail(String token) {
        // parseClaimsJws 메서드가 Base64로 디코딩 및 파싱
        // 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용해 서명한 후 token의 서명과 비교
        // 위조되지 않았다면 페이로드(Claims) 리턴, 위조라면 예외를 날린다.
        // 그 중 우리는 email이 필요하므로 getBody를 부른다.
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
