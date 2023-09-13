package com.yunoi.todo.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunoi.todo.model.UserEntity;
import com.yunoi.todo.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OAuthUserServiceImpl extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    public OAuthUserServiceImpl() {
        super();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // DefaultOAuth2UserService의 기존 loadUser를 호출한다. 이 메서드가 user-info-uri를 이용해 사용자 정보를 가져오는 부분이다.
        final OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            // 사용자 정보 확인 (debugging) 테스팅 시에만 사용해야 함
            log.info("OAuth2User attributes {} ", new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // login 필드 가져오기
        final String username = (String) oAuth2User.getAttributes().get("login");   // TODO: attributes에 들어있는 내용은 제공자마다 다르다. 확장 가능한 코드를 작성하기 위해 고민 필요
        final String authProvider = userRequest.getClientRegistration().getClientName();

        UserEntity userEntity = null;

        if(!userRepository.existsByUsername(username)) {    // TODO: 동작 확인용 구현. 동명이인을 대비할 로직 필요할 것임.
            // 유저가 존재하지 않으면 새로 생성
            userEntity = UserEntity.builder()
                    .username(username)
                    .authProvider(authProvider)
                    .build();
            userEntity = userRepository.save(userEntity);
        }

        log .info("Successfully pulled user info username {} authProvider {}", username, authProvider);

        return new ApplicationOAuth2User(userEntity.getId(), oAuth2User.getAttributes());
    }
}
