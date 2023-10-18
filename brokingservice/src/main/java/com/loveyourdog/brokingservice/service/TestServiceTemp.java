package com.loveyourdog.brokingservice.service;

import com.loveyourdog.brokingservice.model.dto.obj.Dto;
import com.loveyourdog.brokingservice.model.entity.A;
import com.loveyourdog.brokingservice.model.entity.B;
import com.loveyourdog.brokingservice.repository.ARepository;
import com.loveyourdog.brokingservice.repository.BRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TestServiceTemp {
    private final ARepository aRepository;
    private final BRepository bRepository;


//    // 1. A(다) - B(일) 단방향
//    public boolean updateB(Long id1, Long id2, String name){
//        A a = aRepository.findById(id1).get();
//        a.getB().setBName(name); // A의 B의 속성을 바로 변경 가능
//        aRepository.save(a);
//        return true;
//    }
//    public Dto findBbyAid(Long id){
//        A a = aRepository.findById(id).get();
//        B b = a.getB();
//        System.out.println("b name : "+b.getBName());
//        Dto dto = Dto.builder()
//                .id(b.getId())
//                .name(b.getBName()).build();
//        return dto;
//    }

//    // 2. A(다) - B(일) 양방향
//    // 완전 자유롭게 조회 가능하네
//
//    public List<Dto> findBByAbyBid(Long id){
//        List<Dto> dtos = new ArrayList<>();
//        B b = bRepository.findById(id).get();
//        List<A> as =  b.getAs();
//        for (int i = 0; i < as.size(); i++) {
//            Dto dto = Dto.builder()
//                    .id(as.get(i).getId())
//                    .name(as.get(i).getAName()).build();
//            dtos.add(dto);
//            System.out.println(as.get(i).getB().getId());
//            System.out.println(as.get(i).getB().getBName());
//            System.out.println(as.get(i).getB().getAs());
//
//        }
//        return dtos;
//    }
    public String change(Long id){
        List<Dto> dtos = new ArrayList<>();
        B b = bRepository.findById(id).get();
        List<A> as =  b.getAs();
        for (int i = 0; i < as.size(); i++) {
            as.get(i).setAName("메롱");
//            Dto dto = Dto.builder()
//                    .id(as.get(i).getId())
//                    .name(as.get(i).getAName())
//
//            .build();
//            dtos.add(dto);
            System.out.println(as.get(i).getId());
            System.out.println(as.get(i).getAName());
            System.out.println(as.get(i).getB());
            System.out.println("-----------");
            System.out.println(as.get(i).getB().getId());
            System.out.println(as.get(i).getB().getBName());
            System.out.println(as.get(i).getB().getAs());
        }
        b.setAs(as);

        return "dtos";
    }





}
