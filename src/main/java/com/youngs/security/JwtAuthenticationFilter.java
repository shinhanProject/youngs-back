package com.youngs.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HTTP 요청을 필터링하고, JWT를 사용하여 사용자 인증을 처리하는 JWT 인증 필터
 * JWT를 사용하여 사용자를 인증하고, SecurityContext에 사용자 인증 정보를 저장하여 요청 처리 중 사용자를 식별한다.
 * @author : 박상희
 **/
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private PrincipalUserDetailsService principalUserDetailsService;

    /**
     * HTTP 요청 필터링 및 JWT 기반 사용자 인증 처리 메서드
     * @author : 박상희
     * @param request : HTTP 요청
     * @param response : HTTP 응답
     * @param filterChain : 다음 필터로 요청을 전달하는 데 사용되는 FilterChain
     * @throws ServletException : ServletException 예외 처리
     * @throws IOException : IOException 예외 처리
     **/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 요청에서 Access Token 가져오기
            String accessToken = parseBearerToken(request);
            log.info("Filter is running...");

            // Access Token 검사하기 // JWT이므로 인가 서버에 요청하지 않고도 검증 가능
            if (accessToken != null && !accessToken.equalsIgnoreCase("null") && tokenProvider.validateToken(accessToken, "AccessToken")) {
                // email 가져오기 // 위조된 경우 예외 처리된다.
                String email = tokenProvider.validateAndGetEmail(accessToken, "AccessToken");
                log.info("Authenticated email : " + email);

                // email을 통해 user 정보 가져오기
                UserDetails userDetails = principalUserDetailsService.loadUserByUsername(email);

                // 인증 완료 // SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, // 인증된 사용자의 정보
                        null,
                        userDetails.getAuthorities()
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청에서 Bearer 토큰을 파싱하는 메서드
     * @author : 박상희
     * @param request : HTTP 요청
     * @return Bearer 토큰 (토큰을 찾을 수 없을 경우, null 반환)
     **/
    private String parseBearerToken(HttpServletRequest request) {
        // Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴한다.
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
