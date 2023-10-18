package com.loveyourdog.brokingservice.service;

import com.loveyourdog.brokingservice.model.dto.requestDto.CommentRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.CommentResponseDto;
import com.loveyourdog.brokingservice.model.entity.*;
import com.loveyourdog.brokingservice.repository.alarm.AlarmRepository;
import com.loveyourdog.brokingservice.repository.basicRequire.BasicRequireRepository;
import com.loveyourdog.brokingservice.repository.comment.CommentRepository;
import com.loveyourdog.brokingservice.repository.comment.CommentRepositoryImpl;
import com.loveyourdog.brokingservice.repository.cusRequire.CusRequireRepository;
import com.loveyourdog.brokingservice.repository.customer.CustomerRespository;
import com.loveyourdog.brokingservice.repository.dogwalker.DogwalkerRepository;
import com.loveyourdog.brokingservice.repository.inquiry.InquiryRepository;
import com.loveyourdog.brokingservice.repository.offer.OfferRepository;
import com.loveyourdog.brokingservice.repository.reservation.ReservationRepository;
import com.loveyourdog.brokingservice.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {


    private final ReservationRepository reservationRepository;
    private final OfferRepository offerRepository;
    private final InquiryRepository inquiryRepository;
    private final DogwalkerRepository dogwalkerRepository;
    private final CustomerRespository customerRespository;
    private final CusRequireRepository cusRequireRepository;
    private final BasicRequireRepository basicRequireRepository;
    private final AlarmRepository alarmRepository;
    private final CommentRepository commentRepository;
    private final CommentRepositoryImpl commentRepositoryImpl;
    private final ReviewRepository reviewRepository;

    private Review review;

    private Comment parentComment;



    // info -> comment 생성
    public boolean createComment(CommentRequestDto requestDto) throws Exception {
        Comment comment = null;
        Comment parent = null;
        Comment temp = null;

        System.out.println("dw "+requestDto.getDogwalkerId());
        System.out.println(requestDto.getParentId());
        review = reviewRepository.findById(requestDto.getReviewId()).orElseThrow(()-> {
            throw new NoSuchElementException("해당 id를 가진 리뷰는 없습니다");
        });

        if(requestDto.getParentId()!=null && requestDto.getParentId() > 0) {
            parent = commentRepository.findById(requestDto.getParentId()).orElseThrow(() -> {
                throw new NoSuchElementException("해당 id를 가진 parent comment는 없습니다");
            });
            temp = parent;
        }
        Comment ancestor;
        if(requestDto.getDogwalkerId()==null){
            if(requestDto.getCustomerId()==null){
                // 둘 다 null인 경우
                    throw new Exception("dwId, cusId 중 하나라도 입력되어야 합니다");
            } else {
                // 고객만 넘어온 경우
                Customer customer = customerRespository.findById(requestDto.getCustomerId()).orElseThrow(()-> {
                    throw new NoSuchElementException("해당 id를 가진 고객은 없습니다");
                });
                if(temp!=null){
                    while (true){
                        if(temp.getDepth() > 1){
                            temp = temp.getParent();
                            System.out.println("parent.getId() : "+temp.getId());
                        } else {
                            ancestor = temp;
                            System.out.println("parent.getId() stop! : "+temp.getId());
                            break;
                        }
                    }
                    System.out.println("parent!=null");
                    comment = Comment.builder()
                            .status(1) // 작성완료
                            .context(requestDto.getContext())
                            .depth(parent.getDepth()+1)
                            .userType("customer")
                            .customer(customer)
                            .review(review)
                            .parent(parent)
                            .ancestor(ancestor)
                            .build();
                } else {
                    System.out.println("parentComment==null");
                    comment = Comment.builder()
                            .status(1) // 작성완료
                            .context(requestDto.getContext())
                            .depth(1)
                            .userType("customer")
                            .customer(customer)
                            .review(review)
                            .build();
                }
                commentRepository.save(comment);

                // 댓글 알림(부모댓글 작성자, 리뷰 작성자)
                createAlarmForCommetCreation(review, comment, parent);
            }
        } else {
            if(requestDto.getCustomerId()==null){
                // 도그워커만 넘어온 경우
                Dogwalker dogwalker = dogwalkerRepository.findById(requestDto.getDogwalkerId()).orElseThrow(()-> {
                    throw new NoSuchElementException("해당 id를 가진 도그워커는 없습니다");
                });

                if(temp!=null) {
                    while (true){
                        if(temp.getDepth() > 1){
                            temp = temp.getParent();
                            System.out.println("parent.getId() : "+temp.getId());
                        } else {
                            ancestor = temp;
                            System.out.println("parent.getId() stop! : "+temp.getId());
                            break;
                        }
                    }
                    System.out.println("parentComment!=null");

                    comment = Comment.builder()
                            .status(1) // 작성완료
                            .context(requestDto.getContext())
                            .depth(parent.getDepth()+1)
                            .userType("dogwalker")
                            .dogwalker(dogwalker)
                            .review(review)
                            .parent(parent)
                            .ancestor(ancestor)
                            .build();
                } else {
                    System.out.println("parentComment==null");

                    comment = Comment.builder()
                            .status(1) // 작성완료
                            .context(requestDto.getContext())
                            .depth(1)
                            .userType("dogwalker")
                            .dogwalker(dogwalker)
                            .review(review)
                            .build();
                }

                commentRepository.save(comment);

                // 댓글 알림(부모댓글 작성자, 리뷰 작성자)
                createAlarmForCommetCreation(review, comment, parent);
            } else {
                // 둘 다 넘어온 경우
                    throw new Exception("dwId, cusId 중 하나만 입력되어야 합니다");
            }
        }



        return true;
    }


    // 부모댓글작성자, 리뷰작성자에게 갈 알람 Alarm테이블에 등록
    public boolean createAlarmForCommetCreation(Review review, Comment comment, Comment parentComment) throws Exception {

        // 부모댓글 작성자에게 알림
        if(parentComment!=null){
            if (parentComment.getCustomer() == null) {
                if (parentComment.getDogwalker() == null) {
                    throw new Exception("dwId, cusId 중 하나라도 입력되어야 합니다");
                } else {
                    Alarm alarmForParent = Alarm.builder()
                            .msgType(4)
                            .dogwalker(parentComment.getDogwalker())
                            .review(review)
                            .comment(comment)
                            .checked(1)
                            .parentCommentId(parentComment.getId())
                            .build();
                    alarmRepository.save(alarmForParent);
                }
            } else {
                if (parentComment.getDogwalker() == null) {
                    Alarm alarmForParent = Alarm.builder()
                            .msgType(4)
                            .customer(parentComment.getCustomer())
                            .review(review)
                            .comment(comment)
                            .checked(1)
                            .parentCommentId(parentComment.getId())
                            .build();
                    alarmRepository.save(alarmForParent);
                } else {
                    throw new Exception("dwId, cusId 중 하나만 입력되어야 합니다");
                }
            }
        }



        // 리뷰 작성자에게 알림
        Alarm alarmForReviewer = Alarm.builder()
                .msgType(5)
                .customer(getReviewerByReview(review))
                .review(review)
                .comment(comment)
                .checked(1)
                .build();
        alarmRepository.save(alarmForReviewer);

        return true;
    }

    public Customer getReviewerByReview(Review review) throws Exception {
        // 리뷰 작성자에게 알림
        Customer reviwer = null;
        if(review.getReservation().getOffer()==null){
            if(review.getReservation().getInquiry()==null){
                throw new Exception("offer inquriry 둘 중 하나라도 있어야 합니다");
            } else {
                reviwer = review.getReservation().getInquiry().getCustomer();
            }
        } else {
            if(review.getReservation().getInquiry()==null){
                reviwer = review.getReservation().getOffer().getCustomer();
            } else {
                throw new Exception("offer inquriry 둘 중 하나만 있어야 합니다");
            }
        }
        return reviwer;
    }

//    public List<CommentResponseDto> getCommentsByReviewId(Long reviewId) throws Exception {
//        List<CommentResponseDto> responseDtos = new ArrayList<>();
//
//        Review review = reviewRepository.findById(reviewId).orElseThrow(()->{
//            throw new NoSuchElementException("해당하는 리뷰가 업습니다");
//        });
//        List<Comment> comments = review.getComments();
//        if(comments!=null && comments.size()>0){
//            for (Comment comment : comments) {
//                String writerType = null;
//                Long writerId = 0L;
//                String writerNick = null;
//                String dirName = null;
//                String fileName= null;
//                String extension= null;
//                if(comment.getDogwalker()==null && comment.getCustomer()!=null){
//                    writerType = "customer";
//                    writerId = comment.getCustomer().getId();
//                    writerNick = comment.getCustomer().getNick();
//                    dirName = comment.getCustomer().getDirName();
//                    fileName = comment.getCustomer().getFileName();
//                    extension = comment.getCustomer().getExtension();
//                } else if(comment.getDogwalker()!=null && comment.getCustomer()==null) {
//                    writerType = "dogwalker";
//                    writerId = comment.getDogwalker().getId();
//                    writerNick = comment.getDogwalker().getNick();
//                    dirName = comment.getDogwalker().getDirName();
//                    fileName = comment.getDogwalker().getFileName();
//                    extension = comment.getDogwalker().getExtension();
//                } else {
//                    throw new Exception("comment에 dogwalker or customer 중 하나만 존재해야 합니다");
//                }
//                CommentResponseDto dto = CommentResponseDto.builder()
//                        .commentId(comment.getId())
//                        .reviewId(reviewId)
//                        .writerType(writerType)
//                        .writerId(writerId)
//                        .writerNick(writerNick)
//                        .dirName(dirName)
//                        .fileName(fileName)
//                        .extension(extension)
//                        .context(comment.getContext())
//                        .depth(comment.getDepth())
//                        .parentId(comment.getParent().getId())
//                        .status(comment.getStatus())
//                        .build();
//                responseDtos.add(dto);
//            }
//        }
//        return responseDtos;
//    }
//
//    public List<CommentResponseDto> getCommentsByReviewIdAndParentId(Long reviewId, Long parentId) throws Exception {
//        List<CommentResponseDto> responseDtos = new ArrayList<>();
//
//        Review review = reviewRepository.findById(reviewId).orElseThrow(()->{
//            throw new NoSuchElementException("해당하는 리뷰가 업습니다");
//        });
//        List<Comment> comments = review.getComments();
//        Comment parentComment = null;
//        if(comments!=null && comments.size()>0){
//            for (Comment comment:comments) {
//                if(comment.getParent().getId()==parentId) {
//                    String writerType = null;
//                    Long writerId = 0L;
//                    String writerNick = null;
//                    String dirName = null;
//                    String fileName = null;
//                    String extension = null;
//                    String parentWriterNick = null;
//                    parentComment = commentRepository.findById(parentId).get(); // 람디식쓰면 빨간밑줄(람다 캡처링) -> 당연히 존재하니까 orelsethrow생략
//                    if(parentComment.getDogwalker() == null && parentComment.getCustomer() != null){
//                        parentWriterNick = parentComment.getCustomer().getNick();
//                    } else if(parentComment.getDogwalker() != null && parentComment.getCustomer() == null){
//                        parentWriterNick = parentComment.getDogwalker().getNick();
//                    }
//                    if (comment.getDogwalker() == null && comment.getCustomer() != null) {
//                        writerType = "customer";
//                        writerId = comment.getCustomer().getId();
//                        writerNick = comment.getCustomer().getNick();
//                        dirName = comment.getCustomer().getDirName();
//                        fileName = comment.getCustomer().getFileName();
//                        extension = comment.getCustomer().getExtension();
//                    } else if (comment.getDogwalker() != null && comment.getCustomer() == null) {
//                        writerType = "dogwalker";
//                        writerId = comment.getDogwalker().getId();
//                        writerNick = comment.getDogwalker().getNick();
//                        dirName = comment.getDogwalker().getDirName();
//                        fileName = comment.getDogwalker().getFileName();
//                        extension = comment.getDogwalker().getExtension();
//                    } else {
//                        throw new Exception("comment에 dogwalker or customer 중 하나만 존재해야 합니다");
//                    }
//                    CommentResponseDto dto = CommentResponseDto.builder()
//                            .id(comment.getId())
//                            .reviewId(reviewId)
//                            .writerType(writerType)
//                            .writerId(writerId)
//                            .writerNick(writerNick)
//                            .dirName(dirName)
//                            .fileName(fileName)
//                            .extension(extension)
//                            .context(comment.getContext())
//                            .depth(comment.getDepth())
//                            .parentId(comment.getParent().getId())
//                            .parentWriterNick(parentWriterNick)
//                            .status(comment.getStatus())
//                            .build();
//                    responseDtos.add(dto);
//                }
//            }
//        }
//        return responseDtos;
//    }

    public List<CommentResponseDto> getCommentsByReview(Long reviewId){
        List<Comment> comments = commentRepositoryImpl.findCommentByReviewId(reviewId);
        System.out.println("comments size : "+comments.size()); // 3
        List<CommentResponseDto> responseDtos = convertNestedStructure(comments); // 계층형 구조로 변경
        System.out.println("responseDtos size : "+responseDtos.size()); // 2
//        for (CommentResponseDto responseDto : responseDtos) {
//            System.out.println(responseDto.getId() +":"+ responseDto.getChildren().size());
//        }

        return responseDtos;
    }

    public List<CommentResponseDto> getCommentsByParentIdAndReviewId(Long ancestorId,Long reviewId) throws NoSuchFieldException {
        List<Comment> comments = commentRepositoryImpl.findCommentByAnscestorIdAndReviewId(ancestorId,reviewId);
        System.out.println("comments size : "+comments.size()); // 3
        List<CommentResponseDto> responseDtos = new ArrayList<>();
        for (Comment comment : comments) {
            responseDtos.add(comment.toDto());
        }
        return responseDtos;
    }

    private List<CommentResponseDto> convertNestedStructure(List<Comment> comments) {
        List<CommentResponseDto> result = new ArrayList<>();
        Map<Long, CommentResponseDto> map = new HashMap<>();
        comments.stream().forEach(c -> {
            CommentResponseDto dto = null;
            try {
                dto = c.convertToCommentDto(); // dto 생성해서 변환
                // children...
//                List<CommentResponseDto> childrenDtos = new ArrayList<>();
//                dto.setChildren(childrenDtos);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
            // 현재 댓글 추가
            map.put(dto.getId(), dto); // map(Long commentId : Comment comment)

            // 현재 댓글의 부모가 있으면 부모댓글의 자식들에 현재 댓글을 추가... 반복
            // 현재 댓글의 부모가 없으면(=최상위댓글이면) 결과배열에 추가
            if(c.getParent() != null) {
                if(map.get(c.getParent().getId())!=null){
                    map.get(c.getParent().getId())
                            .getChildren()
                            .add(dto);
                }

            } else {
                result.add(dto);
            }
//            System.out.println("dto.getId()"+dto.getId());
//            dto.getChildren().forEach(child->{
//                System.out.println("child - "+child.getId());
//            });

        });
        return result;


    }
}