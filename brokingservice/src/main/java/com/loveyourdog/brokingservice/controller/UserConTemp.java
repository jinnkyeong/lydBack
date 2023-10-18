package com.loveyourdog.brokingservice.controller;//package com.loveyourdog.brokingservice.controller;
//
//import com.loveyourdog.brokingservice.model.enums.Auth;
//import com.loveyourdog.brokingservice.model.entity.Customer;
//import com.loveyourdog.brokingservice.model.serverside.UserDto;
//import com.loveyourdog.brokingservice.repository.UserRepository;
//import com.loveyourdog.brokingservice.service.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Slf4j
//@RestController
//public class UserController {
//
//    private UserService userService;
//    private PasswordEncoder encoder;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    public UserController(UserService userService, PasswordEncoder encoder){
//        this.encoder = encoder;
//        this.userService = userService;
//    }
//
//
//
//    @GetMapping("/user/page")
//    public String wdfg(){
//        return "user 페이지로 들어옴";
//
//    }
//
//    @GetMapping("/admin/page")
//    public String sdfg(){
//        return "admin 페이지로 들어옴";
//
//    }
//
//    @PostMapping("/signUp")
//    public String signUp(@RequestParam("email") String email,
//                         @RequestParam("pwd") String pwd){
//        System.out.println("id : "+email);
//        System.out.println("raw pwd : "+pwd);
//        Customer customer = new Customer();
//
//        String encPwd = encoder.encode(pwd);
//
//        customer.setEmail(email);
//        customer.setAuth(Auth.USER);
//        customer.setPwd(encPwd);
//
//        userRepository.save(customer);
//
//        return "유저 회원가입 완료  "+ customer.getEmail()+" : "+ customer.getPwd();
//    }
//
//
//    // 이곳의 코드는 SecurityConfig 때문에 작동 안하지만, 요청url을 매핑한 메소드가 존재해야 함.
//    // 아니면 404,405 에러
////    @GetMapping("/login")
////    public void getLogin(){}
////    @PostMapping("/login")
////    public String postLogin(UserDto userDto){
////        //return ResponseEntity.ok().body("login success");
////        return "login success";
////    }
//    @PostMapping("/logout")
//    public String getLogout(HttpServletRequest request, HttpServletResponse response,Authentication authentication){
//        //System.out.println(authentication.getPrincipal()); // loadbyusername-> 객체화해서 유저정보?
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        System.out.println("로그아웃 완료");
//        return "로그아웃 완료";
//    }
//
//
//
//
//}
