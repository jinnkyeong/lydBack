//package com.loveyourdog.brokingservice.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.resource.PathResourceResolver;
//
//import java.io.IOException;
//
//
//// Cors 관련 요청을 허용할 출처 지정(전역적)
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:8080") // 허용할 출처
//                .allowedMethods("GET", "POST"); // 허용할 HTTP method
//                //.allowCredentials(true) // 쿠키 인증 요청 허용
//                //.maxAge(3000); // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱(50분)
//    }
//
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        System.out.println("<<<<<<<<<addResourceHandlers>>>>>>>>>");
////        registry.addResourceHandler("/v/**/*") // /v로 시작하는 모든 url에 대해서 index.html 반환
//        registry.addResourceHandler("/**/*") // 컨트롤러에 매핑 안된 모든 url에 대하여 "
//                .addResourceLocations("classpath:/static/")
//                .resourceChain(true)
//                .addResolver(new PathResourceResolver() {
//                    @Override
//                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
//                        System.out.println("resourcePath : "+resourcePath);
//                        System.out.println("location : "+location);
//                        Resource requestedResource = location.createRelative(resourcePath);
//                        return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
//                                : new ClassPathResource("/static/index.html");
//                    }
//
//                });
//    }
//}