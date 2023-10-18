package com.loveyourdog.brokingservice.websocket;

import com.loveyourdog.brokingservice.model.entity.Alarm;
import com.loveyourdog.brokingservice.model.entity.Application;
import com.loveyourdog.brokingservice.model.entity.Customer;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import com.loveyourdog.brokingservice.repository.alarm.AlarmRepository;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class SocketService {

    private final DogwalkerRepository dogwalkerRepository;
    private final CustomerRespository customerRespository;
    private final  AlarmRepository alarmRepository;


    public List<AlarmMessage> getAlarmMessage(AlarmDto alarmDto){
        List<Alarm> alarms = null;
        List<AlarmMessage> messages = new ArrayList<>();
        System.out.println("넘어온 alarmDto 정보 : "+alarmDto.getUserId() +" "+alarmDto.getUserType());

        Long id = Long.valueOf(alarmDto.getUserId());
        if(alarmDto.getUserType().equalsIgnoreCase("dogwalker")){
            Dogwalker dogwalker =  dogwalkerRepository.findById(id).orElseThrow(
                    ()-> new NullPointerException("해당 id와 일치하는 dogwalker 없습니다"));
            alarms =  dogwalker.getAlarms();
            for (Alarm alarm : alarms) {
                if(isDateValid(alarm)){ // 알람 생성 후 7일 이내라면
                    messages.add(converToMessage("dogwalker", alarm));
                }
            }
        }
        else if(alarmDto.getUserType().equalsIgnoreCase("customer")) {
            Customer customer = customerRespository.findById(Long.valueOf(alarmDto.getUserId())).get();
            alarms = customer.getAlarms();
            for (Alarm alarm : alarms) {
                if(isDateValid(alarm)) { // 알람 생성 후 7일 이내라면
                    messages.add(converToMessage("customer", alarm));
                }
            }
        } else {
            throw new NoSuchElementException("userType이 dw or cus 가 아닙니다");
        }
        return messages;
    }

    // 알람 생성 후 일주일 이내라면 알람 메시지 받기
    public boolean isDateValid(Alarm alarm){
        LocalDateTime now  = LocalDateTime.now();
        if(now.isAfter(alarm.getCreatedDate()) && now.isBefore(alarm.getCreatedDate().plusWeeks(1))){
            return true;
        } else {
            return false;
        }
    }

    // 각 케이스마다 다른 메세지 발행
    // Alarm -> AlarmMessage
    public AlarmMessage converToMessage(String userType, Alarm alarm){

        String text = null;
        Long userId = 0L;
        String dirName = null;
        String fileName = null;
        String extension = null;
        if(userType.equalsIgnoreCase("dogwalker")){
            userId = alarm.getDogwalker().getId();
            dirName = alarm.getDogwalker().getDirName();
            fileName = alarm.getDogwalker().getFileName();
            extension = alarm.getDogwalker().getExtension();
        } else if (userType.equalsIgnoreCase("customer")){
            userId = alarm.getCustomer().getId();
            dirName = alarm.getCustomer().getDirName();
            fileName = alarm.getCustomer().getFileName();
            extension = alarm.getCustomer().getExtension();
        } else {
            throw new NoSuchElementException("dw or cus이어야 합니다.");
        }
        AlarmMessage message = new AlarmMessage(
                alarm.getId(),
                userId,
                userType,
                alarm.getMsgType(),
                dirName,
                fileName,
                extension,
                alarm.getChecked()
        );

        switch (alarm.getMsgType()){
            case 1: // 청약자가 지원서or의뢰서 변경 -> 미수락자
                // text
                if(userType.equalsIgnoreCase("dogwalker")){
                    text = "회원님께 문의를 했던 고객님의 의뢰서가 수정되었습니다.";
                } else if (userType.equalsIgnoreCase("customer")) {
                    text = "회원님께 의뢰를 제안했던 도그워커의 지원서가 수정되었습니다.";
                } else {
                    System.out.println("dw or cus이어야 합니다");
                    throw new NoSuchElementException("dw or cus이어야 합니다.");
                }
                message.setText(text);
                // paper id
                message.setPaperId(alarm.getPaperId());
                break;

            case 2: // 예약 완료 -> 청약자
                // text
                text = "회원님의 예약이 완료되었습니다."; message.setText(text);
                // reservation id
                message.setReservId(alarm.getReservation().getId());

                break;
            case 3: // 합격 결정 완료 -> 합격자
                text = "도그워커 합격여부가 결정되었습니다.";
                message.setText(text);

                break;
            case 4: // 댓글 작성 -> 부모댓글러
                // text
                text = "회원님의 댓글에 대댓글이 작성되었습니다."; message.setText(text);

                Application application = null;
                if(alarm.getReview().getReservation().getOffer()!=null){
                    application = alarm.getReview().getReservation().getOffer().getApplication();
                } else if(alarm.getReview().getReservation().getInquiry()!=null){
                    application = alarm.getReview().getReservation().getInquiry().getApplication();
                }
                message.setReviewAppId(application.getId());
                message.setReviewId(alarm.getReview().getId());
                message.setReviewContext(alarm.getReview().getContext());
                message.setCommentId(alarm.getComment().getId());
                message.setCommentContext(alarm.getComment().getContext());
                message.setParentCommentId(alarm.getComment().getParent().getId());
                message.setDepth(alarm.getComment().getDepth());

                break;
            case 5: // 댓글작성 -> 원글러
                // text
                text = "회원님의 리뷰에 댓글이 작성되었습니다."; message.setText(text);
                message.setReviewId(alarm.getReview().getId());
                message.setReviewContext(alarm.getReview().getContext());
                message.setCommentId(alarm.getComment().getId());
                message.setCommentContext(alarm.getComment().getContext());
                if(alarm.getComment().getParent()!=null){
                    message.setParentCommentId(alarm.getComment().getParent().getId());
                }
                message.setDepth(alarm.getComment().getDepth());

                break;

            default:
                break;
        }
        return message;
    }


    public boolean setAlarmsChecked(Long alarmId){
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(()->{
            throw new NoSuchElementException("id에 해당하는 alarm이 없습니다");
        });
        alarm.setChecked(2);
        alarmRepository.save(alarm);
        return true;
    }

}
