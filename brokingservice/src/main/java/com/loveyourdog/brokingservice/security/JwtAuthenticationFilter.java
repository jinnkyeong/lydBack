package com.loveyourdog.brokingservice.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;

// OncePerRequestFilter : 단 한번의 요청에 단 한번만 동작하도록 보장된 필터
// 이 필터를 통하여 servlet에 도달하기 전에 검증을 완료할 수 있다.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("시큐리티 내의 jwt 커스텀 필터 -----------------------");
        String requestURI = request.getRequestURI(); // 요청 uri
        System.out.println("requestURI : " + requestURI);

        if(!requestURI.contains("/auth/reissue")) {

            // 헤더의 Authorization에서 atk 가져오기
            String atk = jwtProvider.resolveToken(request);

            // 가져온 토큰을 검증
            boolean isValid = jwtProvider.validateToken(atk, requestURI);

            if (atk == null) {
                logger.warn("couldn't find bearer string, will ignore the header");

            } else {
                if (isValid) { // 토큰 검증 : SecurityContextHolder에 인증된 Athentication 등록
                    System.out.println("isValid : " + isValid);

                    atk = atk.split(" ")[1].trim(); // Bearer 떼기
                    try {
                        // access token(subject(email,userType)) -> userDetails(auth)
                        // -> Authentication(userDetails, auth) -> SecurityContextHolder에 등록
                        Authentication auth = jwtProvider.getAuthentication(atk);
                        SecurityContextHolder.getContext().setAuthentication(auth);


                    } catch (IllegalArgumentException e) {
                        logger.error("an error occured during getting username from token", e);
                        // JwtException (custom exception) 예외 발생시키기
                        throw new JwtException("유효하지 않은 토큰");
                    } catch (ExpiredJwtException e) {
                        logger.warn("the token is expired and not valid anymore", e);
                        throw new JwtException("토큰 기한 만료");
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
            filterChain.doFilter(request, response); // 필터체인으로 연결

        } else {
            // 토큰 재발급 요청 시 JWT토큰이 만료된 것이지만 Controller까지 갈 수 있어야 하므로 통과시킴
            filterChain.doFilter(request, response); // 필터체인으로 연결
        }
    }
}