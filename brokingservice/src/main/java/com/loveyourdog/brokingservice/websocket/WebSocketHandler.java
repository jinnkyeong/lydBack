//package com.loveyourdog.brokingservice.websocket;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//@RequestMapping("/alarm")
//public class WebSocketHandler extends TextWebSocketHandler {
//    private final ObjectMapper objectMapper;
////    private final ChatService chatService;
//    private final AlarmService alarmService;
//
//
//    // 로그인 한 인원 전체
//    private List<WebSocketSession> sessions = new ArrayList<>();
//
//
//    // WebSocket 연결 되었을때 (= WebSocket 생성)
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        log.info("웹소켓이 연결됨");
//        sessions.add(session);
//
//    }
//    //양방향데이터 통신
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
////        // WebSocket Message의 payload
////        String payload = message.getPayload();
////        log.info("{}", payload);
////
////        // payload(string) -> AlarmMessage(java object)
////        AlarmMessge alarmMessge = objectMapper.readValue(payload, AlarmMessge.class);
////
////
////        //메세지 타입에 따라 로직을 결정
////        chatRoom.handlerActions(session, chatMessage, chatService);
//        for(WebSocketSession s : sessions){
//            String userId = (String) s.getAttributes().get("userId");
//            String userType = (String) s.getAttributes().get("userType");
//            // 로그인 한 사람 중 해당 session을 가진 유저인 경우
//            if (s.getAttributes().get("userId").equals(session.getAttributes().get("userId"))
//            && s.getAttributes().get("userType").equals(session.getAttributes().get("userType"))){
//                List<AlarmMessage> messages = new ArrayList<>();
//                if(userType.equalsIgnoreCase("dogwalker")){
//                     messages = alarmService.findAlarmMessagesOfDw(userId);
//                }
////                else {
////                     messages = alarmService.findAlarmMessagesOfCus(userId);
////                }
//
//                for(AlarmMessage msg : messages){
////                    if(msg.getMsgType()==1){ // 청약자가 지원서or의뢰서 변경 -> 미수락자
////
////                    } else if(msg.getMsgType()==2){ // 예약 완료 -> 청약자
////
////                    } else if(msg.getMsgType()==3){ // 합격 결정 완료 -> 합격자
////
////                    } else if(msg.getMsgType()==4){ // 대댓글 작성 -> 부모댓글러
////
////                    }else if(msg.getMsgType()==5){ // 댓글,대댓글 작성 -> 원글러
////
////                    }
//                    TextMessage sendMsg = new TextMessage("!"+msg.getUserType());
//                    s.sendMessage(sendMsg);
//                }
//            }
//        }
//    }
//    //웹소켓 닫혔을때
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        log.info("웹소켓이 닫힘");
//        sessions.remove(session);
//    }
//}
