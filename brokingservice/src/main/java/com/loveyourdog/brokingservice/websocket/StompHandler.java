package com.loveyourdog.brokingservice.websocket;

import com.loveyourdog.brokingservice.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;


@Component
@RequiredArgsConstructor
//@Order(Ordered.HIGHEST_PRECEDENCE + 99) // 인터셉터 중 가장 우선순위를 높게
public class StompHandler implements ChannelInterceptor {

    private final JwtProvider tokenProvider;


    // publisher 가 send 하기 전 실행
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        System.out.println("\n [ StompHandler -- presend ]");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message); // STOMP의 헤더에 직접 접근
        System.out.println("*************************");
        System.out.println("message : " + message);
        System.out.println("헤더 : " + message.getHeaders());
        System.out.println("토큰 : " + accessor.getNativeHeader("Authorization"));
        System.out.println("*************************");
        if(accessor.getCommand() == StompCommand.CONNECT) { // websocket 연결시(최초 1회)
            if(!tokenProvider.validateToken(accessor.getFirstNativeHeader("Authorization"),"blabla"))
                try {
                    throw new AccessDeniedException("websocket 통신 중 token 검증 실패");
                } catch (AccessDeniedException e) {
                    throw new RuntimeException(e);
                }
        }
        System.out.println("presend를 나감\n");
        return message;
    }
}