//package com.loveyourdog.brokingservice.oauth2;

import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import lombok.RequiredArgsConstructor;

//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import java.security.AuthProvider;
//
//@RequiredArgsConstructor
//@Service
//// oauth 인증이 정상적으로 완료되었을 때 회원 정보를 처리하기 위한 custom 클래스
//public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//
//    private final DogwalkerRepository dogwalkerRepository;
//    private final CustomerRespository customerRespository;
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
//        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
//        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);
//        return processOAuth2User(oAuth2UserRequest, oAuth2User);
//    }
//
//    protected OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
//        //OAuth2 로그인 플랫폼 구분
//        AuthProvider authProvider = AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().
//                getRegistrationId().toUpperCase());
//        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, oAuth2User.getAttributes());
//
//        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
//            throw new RuntimeException("Email not found from OAuth2 provider");
//        }
//
//        Dogwalker dogwalker = dogwalkerRepository.findWithRolesByEmail(oAuth2UserInfo.getEmail()).orElse(null);
//        //이미 가입된 경우
//        if (dogwalker != null) {
//            if (!dogwalker.getAuthProvider().equals(authProvider)) {
//                throw new RuntimeException("Email already signed up.");
//            }
//            dogwalker = updateUser(dogwalker, oAuth2UserInfo);
//        }
//        //가입되지 않은 경우
//        else {
//            dogwalker = registerUser(authProvider, oAuth2UserInfo);
//        }
//        return UserPrincipal.create(dogwalker, oAuth2UserInfo.getAttributes());
//    }
//
//    private Dogwalker registerUser(AuthProvider authProvider, OAuth2UserInfo oAuth2UserInfo) {
//        User user = User.builder()
//                .email(oAuth2UserInfo.getEmail())
//                .name(oAuth2UserInfo.getName())
//                .oauth2Id(oAuth2UserInfo.getOAuth2Id())
//                .authProvider(authProvider)
//                .role(Role.ROLE_USER)
//                .build();
//
//        return userRepository.save(user);
//    }
//
//    private Dogwalker updateUser(Dogwalker dogwalker, OAuth2UserInfo oAuth2UserInfo) {
//        return dogwalkerRepository.save(user.update(oAuth2UserInfo));
//    }
//}