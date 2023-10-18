package com.loveyourdog.brokingservice.service;//package com.loveyourdog.brokingservice.service;
//
//import com.loveyourdog.brokingservice.model.entity.Customer;
//import com.loveyourdog.brokingservice.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserService {
//
//    private UserRepository userRepository;
//    @Autowired
//    public UserService(UserRepository userRepository){
//        this.userRepository = userRepository;
//    }
//
//
//    // 임시 비밀번호 저장
//    public void SetTempPassword(String email, String tempPwd){
//        Customer customer = userRepository.findByEmail(email);
//        customer.setPwd(tempPwd);
//        userRepository.save(customer);
//    }
//
//
//
//
//}
