package com.loveyourdog.brokingservice.repository.review;//package com.loveyourdog.brokingservice.repository.customImpl;

import com.loveyourdog.brokingservice.model.entity.Comment;
import com.loveyourdog.brokingservice.model.entity.QComment;
import com.loveyourdog.brokingservice.model.entity.QReview;
import com.loveyourdog.brokingservice.model.entity.Review;
import com.loveyourdog.brokingservice.repository.comment.CommentRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private JPAQueryFactory queryFactory;
    private QReview review;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory =  new JPAQueryFactory(em);
        this.review = QReview.review;
    }

    @Override
    public List<Review> findGoodReviews() {
        return queryFactory.selectFrom(review)
                .where(review.star.gt(4.4))
                .orderBy(
                        review.createdDate.asc() // 생성일자가 빠르면 나중에 나오기
                ).fetch();
    }
}
