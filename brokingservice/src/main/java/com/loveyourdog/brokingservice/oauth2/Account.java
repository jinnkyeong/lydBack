package com.loveyourdog.brokingservice.oauth2;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Long socialId;
    private String email;
    private String nickname;
    private String role;
}
