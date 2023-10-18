package com.loveyourdog.brokingservice.repository.comment;//package com.loveyourdog.brokingservice.repository.customImpl;

import com.loveyourdog.brokingservice.model.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private JPAQueryFactory queryFactory;
    private QComment comment;

    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory =  new JPAQueryFactory(em);
        this.comment = QComment.comment;

    }

    @Override
    public List<Comment> findCommentByReviewId(Long reviewId) {
        return queryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .distinct()
                .where(comment.review.id.eq(reviewId))
                .orderBy(
                        comment.parent.id.asc().nullsFirst(), // 부모가 null이면 더 먼저 나오기
                        comment.createdDate.asc() // 생성일자가 빠르면 나중에 나오기
                ).fetch();

    }

    @Override
    public List<Comment> findCommentByAnscestorIdAndReviewId(Long ancestorId, Long reviewId) {
        return queryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .distinct()
                .where(comment.review.id.eq(reviewId),
                        comment.ancestor.id.eq(ancestorId))
                .orderBy(
                        comment.createdDate.asc() // 생성일자가 빠르면 나중에 나오기
                ).fetch();

    }




}
