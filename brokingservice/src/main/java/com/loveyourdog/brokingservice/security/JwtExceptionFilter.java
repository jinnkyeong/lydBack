package com.loveyourdog.brokingservice.security;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String requestURI = req.getRequestURI(); // 요청 uri
        try {
            chain.doFilter(req, res); // go to 'JwtAuthenticationFilter'

        } catch (JwtException ex) {
            System.out.println("JwtExceptionFilter에서 JwtException를 만남");
            if(!requestURI.contains("/auth/reissue")) {
                setErrorResponse(HttpStatus.UNAUTHORIZED, res, ex);
            } else {
                System.out.println("excepton filter에서 /auth/reissue가 지나감. dofilter함");
                chain.doFilter(req, res); // go to 'JwtAuthenticationFilter'
            }
        }


    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse res, Throwable ex) throws IOException {
        res.setStatus(status.value());
        res.setContentType("application/json; charset=UTF-8");

        JwtExceptionResponse jwtExceptionResponse = new JwtExceptionResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        res.getWriter().write(jwtExceptionResponse.convertToJson());
    }
}