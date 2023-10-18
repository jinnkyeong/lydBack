package com.loveyourdog.brokingservice.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loveyourdog.brokingservice.model.dto.requestDto.ReissueRequestDto;
import com.loveyourdog.brokingservice.model.entity.Customer;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import com.loveyourdog.brokingservice.repository.admin.AdminRepository;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import com.loveyourdog.brokingservice.service.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Principal;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;

@RequiredArgsConstructor
@Component
// JWT를 생성하고 검증
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret.key}") // 서버에서 지정한 값은 고정이지만 토큰이 발생될때마다의 문자는 변한다(salting에 의해))
    private String salt;
    @Value("${jwt.live.atk}") // access token 유효시간 : 30분
    private int atkLive;
    @Value("${jwt.live.rtk}") // refresh token 유효시간 : 2주
    private int rtkLive;
    private Key secretKey;
    private ObjectMapper objectMapper = new ObjectMapper();

    private final DogwalkerDetailsService dogwalkerDetailsService;
    private final CustomerDetailsService customerDetailsService;
    private final AdminDetailsService adminDetailsService;
    private final RedisService redisService;
    private final DogwalkerRepository dogwalkerRepository;
    private final CustomerRespository customerRespository;



    // init() : 필터를 웹 콘테이너에 생성 후 초기화할 때 호출
    // @PostConstruct : 의존성 주입이 이루어진 후 초기화를 수행. 클래스가 service를 수행하기 전에 발생. lifecycle에서 오직 한 번만 수행
    @PostConstruct
    protected void init() {
        System.out.println("init 실행 한다");
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8)); // String -> byte -> Key
        System.out.println("secretKey : "+secretKey);
    }

    public String resolveToken(HttpServletRequest request) {
        System.out.println("request.getHeader(\"Authorization\") : "+ request.getHeader("Authorization"));
        return request.getHeader("Authorization");
    }

    // 토큰 검증
    // 1. 토큰이 제대로 넘어왔는지 확인
    // 2. 로그아웃 된 atk인지 확인 (=블랙리스트에 있는 atk인가?)
    // 3. 토큰 만료 여부 확인
    // 이상의 검증절차를 모두 통과한 경우에만 true. 하나라도 아니면 false

    public boolean validateToken(String token, String requestURI) {
        if(!requestURI.contains("/auth/reissue")){
            try {
                // 1. 토큰이 제대로 넘어왔는지 확인
                System.out.println("token.substring(0, \"BEARER \".length() : " + token.substring(0, "BEARER ".length()));
                // 토큰이 Bearer xxxxxxxx.xxxxxx.xxxxx 형태로 넘어오지 않았다면
                if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                    return false;
                } else {
                    token = token.split(" ")[1].trim(); // Bearer만 떼기
                    System.out.println("token : " + token);
                }

                // 2. 로그아웃 된 atk인지 확인(=블랙리스트에 있는 atk인가?)
                if (redisService.hasKeyBlackList(token)) {
                    System.out.println("로그아웃하여 다시 사용할 수 없는 액세스 토큰입니다");
                    // TODO 에러 발생시키는 부분 수정
                    throw new RuntimeException("로그아웃하여 다시 사용할 수 없는 액세스 토큰입니다");
                }

                // 3. 토큰 만료 여부 확인
                Jws<Claims> claims = Jwts.parserBuilder() // JwtParserBuilder 인스턴스 생성
                        .setSigningKey(secretKey) // JWS 서명을 확인하는 데 사용할 SecretKey 또는 비대칭 공개키를 지정
                        .build() // thread-safe JwtParser가 반환
                        .parseClaimsJws(token); //  jwtString -> 오리지널 signed JWT
                boolean result = !claims.getBody().getExpiration().before(new Date()); // 해당 JWT가 생존했을 경우 true / 만료되었을 경우 false
                System.out.println("토큰 생존 true, 만료 false : " + result);
                return result;

            }catch (ExpiredJwtException e){
                System.out.println("검증단계 예외 발생 : "+e.getMessage());
                log.warn("the token is expired and not valid anymore", e);
                throw new JwtException("토큰 기한 만료");
            }catch (Exception e) {
                System.out.println("검증단계 예외 발생 : "+e.getMessage());
                System.out.println("예외 발생으로 false");
                return false;
            }
        } else {
            System.out.println("Authentication filter에서 /auth/reissue가 지나감");
            return false;
        }
    }


    public Authentication getAuthentication(String token) throws JsonProcessingException, IllegalAccessException {
        // access token(subject(email,userType)) -> userDetails(auth)
        // -> Authentication(userDetails, auth) -> SecurityContextHolder에 등록
        UserDetails userDetails = null;

        String userType = this.getSubject(token).getUserType();
        String email = this.getSubject(token).getEmail();
        System.out.println("subject에서 꺼낸 "+userType+" "+email);

        if(userType.equalsIgnoreCase("customer")){
            userDetails = customerDetailsService.loadUserByUsername(email);
        } else if (userType.equalsIgnoreCase("dogwalker")) {
            userDetails = dogwalkerDetailsService.loadUserByUsername(email);
        } else  if(userType.equalsIgnoreCase("admin")) {
            userDetails = adminDetailsService.loadUserByUsername(email);
        }else {
            throw new IllegalAccessException("user Type 에러");
        }

//        if(userType.equalsIgnoreCase("customer")){
//            userDetails = customerDetailsService.loadUserByUsername(email);
//        } else if (userType.equalsIgnoreCase("dogwalker")) {
//            userDetails = dogwalkerDetailsService.loadUserByUsername(email);
//        } else  if(userType.equalsIgnoreCase("admin")) {
//            userDetails = adminDetailsService.loadUserByUsername(email);
//        }else {
//            throw new IllegalAccessException("user Type 에러");
//        }
        System.out.println("<<<<<<<<<<  userDetails.getAuthorities() :   >>>>>>>>>>>>>>>>>>>");
        userDetails.getAuthorities().forEach(auth-> System.out.println("auth : "+auth));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }
    public String getSubjectStr(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }
    public Subject getSubject(String token) throws JsonProcessingException {
        String subjectStr = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
        return objectMapper.readValue(subjectStr, Subject.class); // subjectStr(json) -> Subject(java object)
    }

    // 토큰 생성(atk or rtk)
    public String createToken(Subject subject, int tokenLive) throws JsonProcessingException {
        Date now = new Date();
        Claims claims = Jwts.claims(); // claim : payload에 담길 정보의 한 조각

        System.out.println("create token 시 atkrtk: "+atkLive+","+rtkLive+" - parameter live "+tokenLive);
        System.out.println("now  : "+now);
        System.out.println("expire.. : "+new Date(now.getTime() + tokenLive));

        String subjectStr = objectMapper.writeValueAsString(subject); // (subject -> string) => claim => jwt
        claims.setSubject(subjectStr);
        return Jwts.builder()
                .setClaims(claims) // 유저 식별가능한 정보, 다만 노출될 것을 감안해서 중요한 정보는X(=>이메일, id 등)
                .setIssuedAt(now) // 토큰 생성 시점 : 지금
                .setExpiration(new Date(now.getTime() + tokenLive)) // 토큰 만료 시점
                .signWith(secretKey) // 서명
                .compact();
    }

//     rtk로 atk 재발급
    public TokenResponse reissueAtk(ReissueRequestDto requestDto) throws IllegalAccessException, JsonProcessingException {
        String email = null;
        String userType = null;
        System.out.println("requestDto.getUserId() : "+requestDto.getUserId());

        // user 정보 -> email 찾기
        if(requestDto.getUserType().equalsIgnoreCase("dogwalker")){
            Dogwalker dogwalker = dogwalkerRepository.findById(requestDto.getUserId()).orElseThrow(()->{
               throw new NoSuchElementException("해당하는 도그워커가 없습니다");
            });
            email = dogwalker.getEmail();
            userType = "dogwalker";
        } else if(requestDto.getUserType().equalsIgnoreCase("customer")){
            Customer customer  = customerRespository.findById(requestDto.getUserId()).orElseThrow(()->{
                throw new NoSuchElementException("해당하는 고객이 없습니다");
            });
            email = customer.getEmail();
            userType = "customer";
        } else {
            new IllegalAccessException("유저타입을 다시 확인하세요");
        }

        System.out.println("유저 찾아서 email : "+email);
        System.out.println("유저 찾아서 userType : "+userType);

        // email -> redis에서 rtk 찾기
        String rtkInRedis = redisService.getValues(email);

            // rtk가 없는 경우 : 예외처리
        if (Objects.isNull(rtkInRedis)) {
            throw new IllegalAccessException("인증정보가 만료되어 refresh token을 찾을 수 없습니다");
            // 이거 다른 상태코드를 줘서 사용자가 다시 로그인할 수 있게 해야 할듯???

            // rtk가 있는 경우 : atk 재발급
        } else {
            Subject atkSubject = Subject.atk(email, userType);
            String atk = createToken(atkSubject, atkLive);
            return new TokenResponse(atk, null);
        }
    }

}