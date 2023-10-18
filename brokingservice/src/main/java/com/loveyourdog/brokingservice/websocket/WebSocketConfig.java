package com.loveyourdog.brokingservice.websocket;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
// SecurityConfig 거친 후 여기로 옴
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler; // jwt 확인하는 interceptor

    // 1. interceptor - jwt 체크
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        System.out.println("\n[ WebSocketConfig -- configureClientInboundChannel ]");
        registration.interceptors(stompHandler);
    }

    @Override
    // 2. WebSocket 연결
    // EndPoint : /api/ws
    // Cors : * 허용
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        System.out.println("\n[ WebSocketConfig -- registerStompEndpoints ]");
        registry.addEndpoint("/api/ws")
                // 스프링 5.3, 스프링부트 2.4 버전 부터 allowCredentials이 true인 경우 setAllowedOrigins 메서드에서 와일드 카드 `'*'`을 사용하실 수 없습니다.
                .setAllowedOriginPatterns("*")
//                .setAllowedOrigins("http://localhost:8090")
                .withSockJS();
    }


    // 3. message broker
    // broker : 받은 메세지를 /send 구독자에게 뿌려줌
    // /app/receive
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        System.out.println("\n[ WebSocketConfig -- configureMessageBroker ]");
        config.enableSimpleBroker("/api/send"); // 내장 브로커 : prefix의 구독자에게 메세지 뿌림
        config.setApplicationDestinationPrefixes("/api/app"); // prefix로 온 것을 가공할 때 사용(@MessageMapping으로 받아서 @SendTo로 반환)
    }


}