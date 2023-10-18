package com.loveyourdog.brokingservice.scheduler;

import com.loveyourdog.brokingservice.model.dto.obj.DwRankVo;
import com.loveyourdog.brokingservice.model.entity.Application;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class Scheduler { // 스케쥴러는 파라미터가 있으면 안된다고 한다.. 포기
//    @Scheduled(cron =  "0 0 0 * * *") // 매일 정각
    // parameter : 같은 state, town의 도그워커 list
    public List<Application> rankPolpular(List<Application> applList){
        List<DwRankVo> vos = new ArrayList<>();
        List<Application> applications = new ArrayList<>();

        // 각 지원서의 점수를 정함(star+view)
        for (Application application:applList) {
            int viewPoint = 0;
            if (application.getView()==0) {
                viewPoint = 0;
            }else if (application.getView() < 10) {
                viewPoint = 1;
            }else if (application.getView() < 50) {
                viewPoint = 2;
            }else if (application.getView() < 100) {
                viewPoint = 3;
            }else if (application.getView() < 200) {
                viewPoint = 4;
            }else if (application.getView() < 350) {
                viewPoint = 5;
            }else if (application.getView() < 500) {
                viewPoint = 6;
            }else if (application.getView() < 650) {
                viewPoint = 7;
            }else if (application.getView() < 800) {
                viewPoint = 8;
            }else if (application.getView() < 950) {
                viewPoint = 9;
            }else {
                viewPoint = 10;
            }
            System.out.println("tqtqttqqttq : "+application.getDogwalker().getStar() + viewPoint);
            DwRankVo dwRankVo = DwRankVo.builder()
                    .application(application)
                    .totPoint(application.getDogwalker().getStar() + viewPoint)
                    .build();
            vos.add(dwRankVo);
        }

        Collections.sort(vos, Collections.reverseOrder()); // 내림차순 정렬

        if(vos.size()>4) {

            for (int i = 0; i < 4; i++) { // 상위 4개 지원서 추출
                applications.add(vos.get(i).getApplication());
            }
        } else {
            for (int i = 0; i < vos.size(); i++) { // 전부
                applications.add(vos.get(i).getApplication());
            }
        }
        return applications;
    }



}
