package com.yunoi.todo.security;

import com.yunoi.todo.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

/**
 *  유저 정보를 받아 JWT 생성
 * */
@Slf4j
@Service
public class TokenProvider {

    private static final String SECRET_KEY = "testKeytestKeytestKeytestKeytestKeytestKeytestKeytestKeytestKeytestKey";

    private Key key;
    // 키 초기화, 설정에 대해서 좀 더 찾아보고 보강할 것
    public TokenProvider() {
        String encodeStr = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        this.key = Keys.hmacShaKeyFor(encodeStr.getBytes());
    }

    public String create(UserEntity userEntity) {
        // 기한은 생성 시점으로부터 1일로 설정
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        // JWT Token 생성
        return Jwts.builder().signWith(key)
                // payload에 들어갈 내용
                .setSubject(userEntity.getId()) // sub
                .setIssuer("todo app")  // iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate)  //exp
                .compact();
    }

    // 인증 컴포넌트에서 호출할 create 오버로딩
    public String create(final Authentication authentication) {
        ApplicationOAuth2User userPrincipal = (ApplicationOAuth2User) authentication.getPrincipal();
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        return Jwts.builder().signWith(key)
                .setSubject(userPrincipal.getName())    // 리턴값: id
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    public String validateAndGetUserId(String token) {
        // parserClaimsJws 메서드가 Base 64로 디코딩 및 파싱
        // 즉, 헤더와 페이로드를 setSigningKey로 넘어온 시크릿 키를 이용해 서명 후, token의 서명과 비교.
        // 위조되지 않았다면 페이로드(Claims)  리턴, 위조라면 예외 던지기
        // 그 중 우리는 userId가 필요하므로 getBody를 부른다.

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer("todo app")
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
