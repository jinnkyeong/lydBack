package com.loveyourdog.brokingservice.websocket;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SocketController {

    private final SocketService socketService;

    // 클라이언트로부터 어떤 메세지를 받은 경우 활용!!!!
//    // /app/receive로 메세지를 받음
//    @MessageMapping("/receive")
//    // 가공한 메세지를 /send에게 반환 -> 브로커가 /send 구독자들에게 메세지 전달
//    @SendTo("/send")
//    public SocketVO SocketHandler(SocketVO socketVO) {
//        String userName = socketVO.getUserName();
//        String content = socketVO.getContent();
//        System.out.println("\n SocketHandler로 들어옴");
//        System.out.println(" \n username, content : "+userName+" " +content);
//
//        // 생성자로 반환값을 생성합니다.
//        SocketVO result = new SocketVO(userName, content);
//        // 반환
//        System.out.println("SocketHandler를 떠남\n");
//        return result;
//    }


    @MessageMapping("/receive")
    @SendTo("/api/send")
    public List<AlarmMessage> SocketHandler(AlarmDto alarmDto){
        return socketService.getAlarmMessage(alarmDto);
    }



    @GetMapping("/api/open/alarms/chk/{alarmId}")
    public ResponseEntity<Boolean> setAlarmChecked(@PathVariable("alarmId")Long alarmId) throws Exception {
        return new ResponseEntity<>(socketService.setAlarmsChecked(alarmId), HttpStatus.OK);
    }

}