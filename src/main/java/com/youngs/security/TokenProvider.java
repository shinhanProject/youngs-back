package com.youngs.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 사용자 토큰 생성 및 유효성 검증 클래스
 * 사용자 정보를 기반으로 JWT를 생성하고, JWT 유효성을 검사하여 사용자의 인증을 처리한다.
 * @author : 박상희
 **/
@Slf4j
@Service
public class TokenProvider {
    private static final String ACCESS_SECRET_KEY = "ACCESS_SECRET_KEY"; // Access Token Secret Key
    private static final String REFRESH_SECRET_KEY = "REFRESH_SECRET_KEY"; // Refresh Token Secret Key

    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 3600; // Access Token 유효 시간 (1시간)
    private static final long REFRESH_TOKEN_VALIDITY_SECONDS = 2592000; // Refresh Token 유효 시간 (30일)

    /**
     * JwtBuilder 객체를 생성하과 반환하는 메서드
     * @author : 박상희
     * @param time : 토큰의 유효 시간 (밀리초)
     * @param secretKeyType : 토큰의 종류 (AccessToken 또는 RefreshToken)
     * @return JwtBuilder 객체
     **/
    public JwtBuilder create(long time, String secretKeyType) {
        String SECRET_KEY = "";
        if (secretKeyType.equals("AccessToken")) {
            SECRET_KEY = ACCESS_SECRET_KEY;
        }
        else if (secretKeyType.equals("RefreshToken")) {
            SECRET_KEY = REFRESH_SECRET_KEY;
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + time); // 기한은 지금부터 토큰 유효 시간만큼 설정

        // JWT 생성
        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                // payload에 들어갈 내용
                .setIssuer("Young's") // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate); // exp
    }

    /**
     * Access Token을 생성하는 메서드
     * @author : 박상희
     * @param userDetails : 사용자 정보
     * @return 생성된 Access Token
     **/
    public String createAccessToken(UserDetails userDetails) {
        long accessTokenExpiryTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;

        return create(accessTokenExpiryTime, "AccessToken")
                // payload에 들어갈 내용
                .setSubject(userDetails.getUsername()) // sub  // email
                .compact();
    }

    /**
     * Refresh Token을 생성하는 메서드
     * @author : 박상희
     * @return 생성된 Refresh Token
     **/
    public String createRefreshToken() {
        long refreshTokenExpiryTime = REFRESH_TOKEN_VALIDITY_SECONDS * 1000;

        // 보통 Refresh Token은 Access Token과 달리 Subject, Claim과 같은 회원 정보를 담고 있지 않는다.
        return create(refreshTokenExpiryTime, "RefreshToken").compact();
    }

    /**
     * 토큰의 유효성을 검사하는 메서드
     * @author : 박상희
     * @param token : JWT
     * @param secretKeyType : 토큰의 종류 (AccessToken 또는 RefreshToken)
     * @return 토큰의 유효성 여부
     **/
    public boolean validateToken(String token, String secretKeyType) { // 토큰을 받아 유효성 검사를 실행
        try {
            String SECRET_KEY = "";
            if (secretKeyType.equals("AccessToken")) {
                SECRET_KEY = ACCESS_SECRET_KEY;
            }
            else if (secretKeyType.equals("RefreshToken")) {
                SECRET_KEY = REFRESH_SECRET_KEY;
            }

            // SECRET_KEY를 이용하여 파싱을 수행
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);

            return true;
        }
        catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        }
        catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        }
        catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        }
        catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    /**
     * JWT를 검증하고, 포함된 이메일을 반환하는 메서드
     * @author : 박상희
     * @param token : 체크할 JWT
     * @param secretKeyType : 토큰의 종류 (AccessToken 또는 RefreshToken)
     * @return JWT에 포함된 사용자 이메일
     **/
    public String validateAndGetEmail(String token, String secretKeyType) {
        String SECRET_KEY = "";
        if (secretKeyType.equals("AccessToken")) {
            SECRET_KEY = ACCESS_SECRET_KEY;
        }
        else if (secretKeyType.equals("RefreshToken")) {
            SECRET_KEY = REFRESH_SECRET_KEY;
        }

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
