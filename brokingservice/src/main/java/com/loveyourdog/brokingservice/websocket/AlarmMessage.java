package com.loveyourdog.brokingservice.websocket;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmMessage {

    private Long id; // alarm id
    private int msgType;
    private Long userId;
    private String userType; // dogwalker / customer
    private String text; // 알림창에 나올 문장
    private int checked;
    private String dirName;
    private String fileName;
    private String extension;
    
    // 1 : 청약자가 지원서or의뢰서 변경 -> 미수락자
    private Long paperId; // 청약자의 변경된 서류 id
    
    // 2 : 예약 완료 -> 청약자
    private Long reservId; // 예약 id
    
    // 3 : 합격 결정 완료 -> 합격자
    
    // 4 : 대댓글 작성 -> 부모댓글러  2->1   3->2   4->3 ...
    // 5 : 댓글,대댓글 작성 -> 원글러 1,2,3,4 -> 0
    private Long reviewAppId;
    private Long reviewId; // 원글인 리뷰 id
    private String reviewContext; // 원글인 리뷰내용
    private Long commentId; // 작성된 댓글 id
    private String commentContext; // 새로운 댓글 내용
    private Long parentCommentId; // 작성된 댓글의 부모 댓글
    private int depth; // 깊이


    public AlarmMessage(Long id, Long userId, String userType, int msgType,String dirName,String fileName,String extension, int checked) {
        this.id = id;
        this.userId = userId;
        this.userType = userType;
        this.msgType = msgType;
        this.dirName = dirName;
        this.fileName = fileName;
        this.extension = extension;
        this.checked = checked;
    }
}
