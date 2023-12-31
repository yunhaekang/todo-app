package com.yunoi.todo.config;

import com.yunoi.todo.security.JwtAuthenticationFilter;
import com.yunoi.todo.security.OAuthSuccessHandler;
import com.yunoi.todo.security.OAuthUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.web.filter.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private OAuthUserServiceImpl oAuthUserService;

    @Autowired
    private OAuthSuccessHandler oAuthSuccessHandler;

    @Bean
    public SecurityFilterChain customFilterChain(HttpSecurity http) throws Exception {
        // http 시큐리티 빌더 변경된 방식으로 적용 (filterChain)
        http.cors() // WebMvcConfig 에서 이미 설정했으므로 기본 cors 설정
                .and().csrf().disable() // csrf 는 현재 사용하지 않으므로 disable
                .httpBasic().disable()  // token 을 사용하므로 베이직 인증 disable
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 기반이 아님을 선언
                .and().authorizeRequests()  // /와 /user/** 경로는 인증 안해도 됨
                    .antMatchers("/", "/user/**", "/oauth2/**").permitAll() //oauth2 엔드포인트 추가
                .anyRequest()   // 이외의 모든 경로는 인증해야 됨
                .authenticated()
                .and()
                .oauth2Login() // oauth2Login 설정 추가
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*") // callback uri 설정
                .and()
                .userInfoEndpoint()
                .userService(oAuthUserService) // OAuthUserServiceImpl을 유저 서비스로 등록
                .and()
                .successHandler(oAuthSuccessHandler);   // success handler 등록

        // filter 등록
        // 매 요청마다 CorsFilter 실행한 후에 jwtAuthenticationFilter 를 실행한다.
        http.addFilterAfter(
                jwtAuthenticationFilter
                , CorsFilter.class
        );

        return http.build();
    }
}
