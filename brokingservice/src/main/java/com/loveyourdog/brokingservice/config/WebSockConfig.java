//package com.loveyourdog.brokingservice.config;
//
//import com.loveyourdog.brokingservice.websocket.WebSocketHandler;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.*;
//
//@RequiredArgsConstructor
//@Configuration
////@EnableWebSocket
//@EnableWebSocketMessageBroker
////public class WebSockConfig implements WebSocketConfigurer {
//public class WebSockConfig implements WebSocketMessageBrokerConfigurer {
//    private final WebSocketHandler webSocketHandler;
//
////    @Override
////    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
////        /*
////         * 스프링에서 웹소켓을 사용하기 위해서 클라이언트가 보내는 통신을 처리할 핸들러가 필요하다
////         * 직접 구현한 웹소켓 핸들러 (webSocketHandler)를 웹소켓이 연결될 때, Handshake 할 주소 (/ws/chat)와 함께 addHandler 메소드의 인자로 넣어준다.
////         */
////        registry.addHandler(webSocketHandler, "ws/alarm")
////                .setAllowedOriginPatterns("*");//CORS 설정
////    }
//
//    @Override
//    // 클라이언트가 메시지를 구독할 endpoint를 정의합니다.
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableSimpleBroker("/alarm");
//    }
//
//    @Override
//    // connection을 맺을때 CORS 허용합니다.
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/")
//                // 스프링 5.3, 스프링부트 2.4 버전 부터 allowCredentials이 true인 경우 setAllowedOrigins 메서드에서
//                // 와일드 카드 `'*'`을 사용하실 수 없습니다.
//                //  	.setAllowedOrigins("*")
//                .setAllowedOriginPatterns("*")
//                .withSockJS();
//    }
//
//
//}