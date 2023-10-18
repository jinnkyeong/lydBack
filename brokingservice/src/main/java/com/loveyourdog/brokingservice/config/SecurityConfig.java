package com.loveyourdog.brokingservice.config;


import com.loveyourdog.brokingservice.security.JwtAuthenticationFilter;
import com.loveyourdog.brokingservice.security.JwtProvider;
import com.loveyourdog.brokingservice.security.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final JwtExceptionFilter jwtExceptionFilter;


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        WebSecurityCustomizer web = web1 -> {
            web1.ignoring().antMatchers(
            "/", "/assets/**",
            "/swagger/**", "/swagger-ui/**", "/swagger-resources/**", "/v2/api-docs");
            web1.httpFirewall(defaultHttpFirewall()); // 요청에 '//' 허용
        };
        return web;
    }
    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers()
                .frameOptions().sameOrigin(); // deny -> sameorigin
        http
            // ID, Password 문자열을 Base64로 인코딩하여 전달하는 구조
            .httpBasic().disable()
            // 쿠키 기반이 아닌 JWT 기반이므로 사용하지 않음
            .csrf().disable()
            // CORS 설정(Authentication Filter 인증 보다)
            .cors().configurationSource(corsConfigurationSource())
            .and()
            // Spring Security 세션 정책 : 세션을 생성 및 사용하지 않음
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // authorizeRequests : 조건별로 요청 허용/제한 설정
            .authorizeRequests()
            // permitall할 api
            .antMatchers("/swagger/**", "/swagger-ui/**", "/swagger-resources/**", "/v2/api-docs").permitAll()
            .antMatchers("/api/swagger/**", "/api/swagger-ui/**", "/api/swagger-resources/**", "/api/v2/api-docs").permitAll()
            .antMatchers( "/register", "/login", "/auth/**","/admin-login","/oauth2/**").permitAll()
            .antMatchers( "/api/register", "/api/login","/api/login/**", "/api/auth/**","/api/admin-login","/api/oauth2/**").permitAll()
            .antMatchers("/assets/**","/js/**","/css/**","/fonts/**","/img/**","/favicon.ico").permitAll()
            .antMatchers("/api/assets/**","/api/js/**","/api/css/**","/api/fonts/**","/api/img/**","/api/favicon.ico").permitAll()
            .antMatchers("/open/**","/ws/**","/ws").permitAll() // 시큐리비 배제할 api(search 등), 웹소켓
            .antMatchers("/api/open/**","/api/ws/**","/api/ws").permitAll() // 시큐리비 배제할 api(search 등), 웹소켓
//            .antMatchers("/v","/v/**").permitAll() // router path
            // /admin으로 시작하는 요청은 ADMIN 권한이 있는 유저에게만 허용
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/api/admin/**").hasRole("ADMIN")
            // 나머지는 인증 필요
            .anyRequest().authenticated();

//            http
//            .oauth2Login()
//            .authorizationEndpoint().baseUri("/oauth2/authorize")  // 소셜 로그인 url
//            .authorizationRequestRepository(cookieAuthorizationRequestRepository)  // 인증 요청을 cookie 에 저장
//            .and()
//            .redirectionEndpoint().baseUri("/oauth2/callback/*")  // 소셜 인증 후 redirect url
//            .and()
//            //userService()는 OAuth2 인증 과정에서 Authentication 생성에 필요한 OAuth2User 를 반환하는 클래스를 지정한다.
//            .userInfoEndpoint().userService(customOAuth2UserService)  // 회원 정보 처리
//            .and()
//            .successHandler(oAuth2AuthenticationSuccessHandler)
//            .failureHandler(oAuth2AuthenticationFailureHandler);

        http
            .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class) // jwt 만료 에러잡는 필터
            .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class) // jwt 인증 담당 필터
            .exceptionHandling()
            // 인가 예외(권한X) 처리(403)
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            })
            // 인증 예외 처리(401)
            .authenticationEntryPoint((request, response, authException) -> {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            });

        return http.build();
    }


    // CORS 허용 적용
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedOrigin("*");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("http://13.125.144.0:8080");
        configuration.addAllowedOrigin("http://13.125.144.0:80");
        configuration.addAllowedOrigin("http://www.loveyourdog.co.kr:80");
        configuration.addAllowedOrigin("http://www.loveyourdog.co.kr");
        configuration.addAllowedOrigin("https://loveyourdog.co.kr");
        configuration.addAllowedOrigin("https://www.loveyourdog.co.kr");

        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }



}