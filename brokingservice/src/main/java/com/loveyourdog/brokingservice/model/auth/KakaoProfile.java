package com.loveyourdog.brokingservice.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoProfile {
//        response.getBody() :
//    {
//        "id":3044067818,
//        "connected_at":"2023-09-30T13:43:35Z",
//        "properties": {
//            "nickname":"김진경",
//            "profile_image":"http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg",
//            "thumbnail_image":"http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img110x110.jpg"
//        },
//        "kakao_account": {
//            "profile_nickname_needs_agreement":false,
//            "profile_image_needs_agreement":false,
//            "profile": {
//                "nickname":"김진경",
//                "thumbnail_image_url":"http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_110x110.jpg",
//                "profile_image_url":"http://k.kakaocdn.net/dn/dpk9l1/btqmGhA2lKL/Oz0wDuJn1YV2DIn92f6DVK/img_640x640.jpg",
//                "is_default_image":true
//            },
//            "has_email":true,
//            "email_needs_agreement":false,
//            "is_email_valid":true,
//            "is_email_verified":true,
//            "email":"wlsrud0303@naver.com",
//            "has_age_range":true,
//            "age_range_needs_agreement":false,
//            "age_range":"20~29",
//            "has_gender":true,
//            "gender_needs_agreement":false,
//            "gender":"female"
//        }
//    }

    private Long id;
    private String nickname;
    private String profile_image;
    private String email;
    private String age_range;
    private String gender;


}
