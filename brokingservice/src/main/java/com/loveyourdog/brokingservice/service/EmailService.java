package com.loveyourdog.brokingservice.service;

import com.loveyourdog.brokingservice.model.dto.obj.EmailMsg;
import com.loveyourdog.brokingservice.model.dto.requestDto.EmailResponseDto;
import com.loveyourdog.brokingservice.security.UserTypeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final String FROM_ADDRESS = "kimsmsm33@gmail.com";

    // parameter : userTypeDto(email 필수)
    // 비밀번호 찾기) 임시 비밀번호 생성 -> DB 업데이트, 메일 발송
    // return : boolean 성공여부
    public boolean sendPasswordMail(UserTypeDto userTypeDto) throws Exception {
            String randomNum = createCode(); // 새로 랜덤하게 생성한 번호
            EmailMsg emailMessage = EmailMsg.builder()
                    .to(userTypeDto.getEmail())
                    .subject("[럽유얼도그] 임시 비밀번호 발송")
                    .message("새로 생성된 임시 비밀번호는 " + randomNum + "입니다. \n\n '내 정보-프로필/보안'에서 비밀번호를 변경 해 주세요.")
                    .build();
            sendMail(
                    emailMessage,
                    randomNum,
                    "PWD", // db에 임시번호 저장도 함
                    userTypeDto.getUserType()
            );

        return true;
    }
    // parameter : userTypeDto(email,userType 필수)
    // 회원가입) 인증코드 생성 -> 메일 발송
    // return : EmailResponseDto(인증코드)
    public EmailResponseDto sendJoinMail(UserTypeDto userTypeDto){
        String randomNum = createCode(); // 새로 랜덤하게 생성한 번호
        EmailMsg emailMessage = EmailMsg.builder()
                .to(userTypeDto.getEmail())
                .subject("[럽유얼도그]회원가입 이메일 인증")
                .message("회원가입을 위한 인증코드는 "+randomNum+"입니다. \n\n인증번호를 입력 해 주세요.")
                .build();

        sendMail(
                emailMessage,
                randomNum,
                "EMAIL",
                null
        );

        return EmailResponseDto.builder().
                code(randomNum).
                build();
    }
    public String sendMail(EmailMsg emailMsg, String randomNum, String type, String userType) {


        String encodedNum = passwordEncoder.encode(randomNum);
        if(type.equalsIgnoreCase("PWD")) {
            userService.SetTempPassword(emailMsg.getTo(), encodedNum, userType); // 임시 비밀번호 해싱 후 저장
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailMsg.getTo()); // 메일 수신자
        message.setFrom(FROM_ADDRESS); // 발신자(필수;;)
        message.setSubject(emailMsg.getSubject()); // 메일 제목
        message.setText(emailMsg.getMessage()); // 메일 내용
        javaMailSender.send(message);

        log.info("Successed to send email");
        return randomNum;

    }

    // 인증번호 및 임시 비밀번호 생성 메서드
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0:
                    key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1:
                    key.append((char) ((int) random.nextInt(26) + 65)); break;
                default:
                    key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }

}