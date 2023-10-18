//package com.loveyourdog.brokingservice.websocket;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.loveyourdog.brokingservice.model.entity.Alarm;
//import com.loveyourdog.brokingservice.model.entity.Dogwalker;
//import com.loveyourdog.brokingservice.repository.alarm.AlarmRepository;
//import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
//import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class AlarmService {
//    private final ObjectMapper objectMapper;
//    private final DogwalkerRepository dogwalkerRepository;
//    private final CustomerRespository customerRespository;
//    private final AlarmRepository alarmRepository;
//
////    private Map<String, ChatRoom> chatRooms;
////
////    @PostConstruct
////    private void init() {
////        chatRooms = new LinkedHashMap<>();
////    }
////
////    //활성화된 모든 채팅방을 조회
////    public  List<ChatDto> findAllRoom() {
////        List<ChatDto> collect = chatRooms.values().stream().map(chatRoom -> new ChatDto(chatRoom.getRoomId(), chatRoom.getName(), (long) chatRoom.getSessions().size())).collect(Collectors.toList());
////        return collect;
////    }
////    //채팅방 하나를 조회
////    public ChatRoom findRoomById(String roomId) {
////        return chatRooms.get(roomId);
////    }
//
////    //새로운 방 생성
////    public ChatRoom createRoom(String name) {
////        String randomId = UUID.randomUUID().toString();
////        ChatRoom chatRoom = ChatRoom.builder()
////                .roomId(randomId)
////                .name(name)
////                .build();
////        chatRooms.put(randomId, chatRoom);
////        return chatRoom;
////    }
////    //방 삭제
////    public void deleteRoom(String roomId) {
////        ChatRoom chatRoom = findRoomById(roomId);
////        //해당방에 아무도 없다면 자동 삭제
////        if(chatRoom.getSessions().size() == 0) chatRooms.remove(roomId);
////    }
//
//
//
//
//    public <T> void sendMessage(WebSocketSession session, T message) {
//        try{
//            TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(message));
//            session.sendMessage(textMessage);
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//        }
//    }
//
//    public List<AlarmMessage> findAlarmMessagesOfDw(String dwId) {
//        List<AlarmMessage> messages = new ArrayList<>();
//        Dogwalker dogwalker = dogwalkerRepository.findById(Long.valueOf(dwId)).get();
//
//        AlarmMessage alarmMessage = null;
//        for (Alarm alarm : dogwalker.getAlarms()) {
//            System.out.println("!!!!" + alarm.getMsgType());
//            alarmMessage = AlarmMessage.builder()
//                    .msgType(alarm.getMsgType())
//                    .build();
//        }
//        messages.add(alarmMessage);
//        return messages;
//
//    }
//
//}
