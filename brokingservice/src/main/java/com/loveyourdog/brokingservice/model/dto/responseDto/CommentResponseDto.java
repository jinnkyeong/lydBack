package com.loveyourdog.brokingservice.model.dto.responseDto;

import com.loveyourdog.brokingservice.model.entity.Comment;
import com.loveyourdog.brokingservice.model.entity.Customer;
import com.loveyourdog.brokingservice.model.entity.Dogwalker;
import com.loveyourdog.brokingservice.model.entity.Review;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {


    private Long id;

    private String context;

    private int status;
    // 1 alive
    // 2 deleted

    private int depth;
    // 원글 0
    // 댓글 1
    // 대댓글 2 ...

    // 작성자 정보
    private String writerType; // dogwalker customer
    private Long writerId;
    private String writerNick;
    private String dirName;
    private String fileName;
    private String extension;

    private Long reviewId; // 리뷰

    // 부모댓글 정보
    private Long parentId; // 부모댓글
    private String parentWriterNick; // 부모댓글 작성자 닉네임
    private Long parentWriterId;
    private String parentWriterType;
//    private CommentResponseDto parent;


    private List<CommentResponseDto> children;  // 자식 댓글 list


//    private List<CommentResponseDto> children;  // 자식 댓글 list
//    private List<Comment> children;  // 자식 댓글 list

//    public CommentResponseDto(Comment comment) throws NoSuchFieldException {
//        this.id = comment.getId();
//        this.context = comment.getContext();
//        this.status = comment.getStatus();
//        this.depth = comment.getDepth();
//        this.writerType = comment.getUserType();
////        this.writerId = comment.get
////        this.writerNick = writerNick;
////        this.dirName = dirName;
////        this.fileName = fileName;
////        this.extension = extension;
//        this.reviewId = comment.getReview().getId();
//        this.parentId = comment.getParent().getId();
////        this.parentWriterNick = parentWriterNick;
////        this.children = comment.convertToCommentDto().getChildren();
//    }
//

}
