//package com.loveyourdog.brokingservice.controller;
//
//import org.springframework.boot.web.server.ErrorPage;
//import org.springframework.boot.web.server.ErrorPageRegistrar;
//import org.springframework.boot.web.server.ErrorPageRegistry;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ErrorController implements ErrorPageRegistrar {
//    @Override
//    public void registerErrorPages(ErrorPageRegistry registry) {
//        ErrorPage e404 = new ErrorPage(HttpStatus.NOT_FOUND,"/static/index.html");
//        registry.addErrorPages(e404);
//    }
//}
