package com.loveyourdog.brokingservice.controller;

import com.loveyourdog.brokingservice.model.dto.requestDto.CommentRequestDto;
import com.loveyourdog.brokingservice.model.dto.requestDto.ReviewRequestDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.CommentResponseDto;
import com.loveyourdog.brokingservice.model.dto.responseDto.ReviewResponseDto;
import com.loveyourdog.brokingservice.service.CommentService;
import com.loveyourdog.brokingservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api")
public class CommentController {


    private final CommentService commentService;
//

    // info -> comment 생성
    @PostMapping(value = "/open/comments")
    public ResponseEntity<Boolean> createComment(@RequestBody CommentRequestDto requestDto) throws Exception {
        return new ResponseEntity<>(commentService.createComment(requestDto), HttpStatus.OK);
    }

//    @GetMapping("/open/comments/{reviewId}")
//    public ResponseEntity<List<CommentResponseDto>> getCommentsByReviewId(@PathVariable("reviewId")Long reviewId) throws Exception {
//        return new ResponseEntity<>(commentService.getCommentsByReviewId(reviewId), HttpStatus.OK);
//    }
//
//    @GetMapping("/open/comments/{reviewId}/{parentId}")
//    public ResponseEntity<List<CommentResponseDto>> getCommentsByReviewIdAndParentId(@PathVariable("reviewId")Long reviewId,
//                                                                                     @PathVariable("parentId")Long parentId ) throws Exception {
//        return new ResponseEntity<>(commentService.getCommentsByReviewIdAndParentId(reviewId, parentId), HttpStatus.OK);
//    }
    @GetMapping("/open/comments/{reviewId}")
        public ResponseEntity<List<CommentResponseDto>> getCommentsByReviewId(@PathVariable("reviewId")Long reviewId) throws Exception {
        return new ResponseEntity<>(commentService.getCommentsByReview(reviewId), HttpStatus.OK);
    }
    @GetMapping("/open/childComments/{ancestorId}/{reviewId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByParentIdAndReviewId(
            @PathVariable("ancestorId")Long ancestorId,
            @PathVariable("reviewId")Long reviewId) throws Exception {
        return new ResponseEntity<>(commentService.getCommentsByParentIdAndReviewId(ancestorId, reviewId), HttpStatus.OK);
    }

}
