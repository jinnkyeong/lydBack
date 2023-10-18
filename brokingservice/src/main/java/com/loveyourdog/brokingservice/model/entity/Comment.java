package com.loveyourdog.brokingservice.model.entity;


import com.loveyourdog.brokingservice.model.dto.responseDto.CommentResponseDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
@Entity
public class Comment extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Lob
    private String context;

    @ColumnDefault("1")
    private int status;
    // 1 alive
    // 2 deleted

    private int depth;
    // 댓글 1
    // 대댓글 2 ...

    private String userType; // dogwalker customer

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dogwalker")
    private Dogwalker dogwalker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer")
    private Customer customer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private Comment parent; // 부모댓글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ancestor")
    private Comment ancestor;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();  // 자식 댓글 list

    @Builder.Default
    @OneToMany(mappedBy = "ancestor", orphanRemoval = true)
    private List<Comment> descendants = new ArrayList<>();  // 자식 댓글 list



    public CommentResponseDto convertToCommentDto() throws NoSuchFieldException {
        List<CommentResponseDto> children = new ArrayList<>();
        CommentResponseDto responseDto = CommentResponseDto.builder()
                .id(id)
                .context(context)
                .status(status)
                .depth(depth)
                .reviewId(review.getId())
                .children(children)
                .build();
        // 작성자 정보 setting
        if(getUserType().equals("dogwalker")){
            responseDto.setWriterType("dogwalker");
            responseDto.setWriterId(getDogwalker().getId());
            responseDto.setWriterNick(getDogwalker().getNick());
            responseDto.setDirName(getDogwalker().getDirName());
            responseDto.setFileName(getDogwalker().getFileName());
            responseDto.setExtension(getDogwalker().getExtension());

        } else if (getUserType().equals("customer")) {
            responseDto.setWriterType("customer");
            responseDto.setWriterId(getCustomer().getId());
            responseDto.setWriterNick(getCustomer().getNick());
            responseDto.setDirName(getCustomer().getDirName());
            responseDto.setFileName(getCustomer().getFileName());
            responseDto.setExtension(getCustomer().getExtension());
        } else {
            throw new NoSuchFieldException("도그워커나 고객이 아닙니다");
        }
        // 부모 댓글 정보
        if(getParent()!=null){
            responseDto.setParentId(getParent().getId()); // 부모댓글 id
            if(getParent().getUserType().equals("dogwalker")){ // 부모댓글 작성자 닉네임
                responseDto.setParentWriterNick(getParent().getDogwalker().getNick());
            } else if(getParent().getUserType().equals("customer")){
                responseDto.setParentWriterNick(getParent().getCustomer().getNick());
            }
        }
        return responseDto;
    }
    public CommentResponseDto toDto() throws NoSuchFieldException {
        CommentResponseDto dto = CommentResponseDto.builder()
                .id(this.id)
                .context(this.context)
                .reviewId(this.review.getId())
//                .createdDate(this.createdDate)
//                .updatedDate(this.updatedDate)
                .depth(this.depth)
                .build();
        if(getUserType().equals("dogwalker")){
            dto.setWriterType("dogwalker");
            dto.setWriterId(getDogwalker().getId());
            dto.setWriterNick(getDogwalker().getNick());
            dto.setDirName(getDogwalker().getDirName());
            dto.setFileName(getDogwalker().getFileName());
            dto.setExtension(getDogwalker().getExtension());

        } else if (getUserType().equals("customer")) {
            dto.setWriterType("customer");
            dto.setWriterId(getCustomer().getId());
            dto.setWriterNick(getCustomer().getNick());
            dto.setDirName(getCustomer().getDirName());
            dto.setFileName(getCustomer().getFileName());
            dto.setExtension(getCustomer().getExtension());
        } else {
            throw new NoSuchFieldException("도그워커나 고객이 아닙니다");
        }
        dto.setParentId(getParent().getId()); // 부모댓글 id
        if(getParent().getUserType().equals("dogwalker")){ // 부모댓글 작성자 닉네임
            dto.setParentWriterNick(getParent().getDogwalker().getNick());
            dto.setParentWriterId(getParent().getDogwalker().getId());
            dto.setParentWriterType("dogwalker");
        } else if(getParent().getUserType().equals("customer")){
            dto.setParentWriterNick(getParent().getCustomer().getNick());
            dto.setParentWriterId(getParent().getCustomer().getId());
            dto.setParentWriterType("customer");
        }


//        // 자식 노드 설정
//        List<CommentResponseDto> dto = this.getChildren() == null ?
//                null :
//                this.getChildren().stream().map(Comment::toDto).collect(Collectors.toList());
//        build.setChildren(dto);

        return dto;
    }


}
