package com.loveyourdog.brokingservice.repository.comment;



import com.loveyourdog.brokingservice.model.entity.Application;
import com.loveyourdog.brokingservice.model.entity.Comment;
import com.loveyourdog.brokingservice.repository.application.ApplicationSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

//@Repository
public interface CommentRepositoryCustom {
//    public Page<Application> search(ApplicationSearchCondition condition, Pageable pageable);}
//    public Page<Application> search(ApplicationSearchCondition condition, PageRequest page);}

    public List<Comment> findCommentByReviewId(Long reviewId) ;



    public List<Comment> findCommentByAnscestorIdAndReviewId(Long ancestorId, Long reviewId);
}