package com.loveyourdog.brokingservice.repository.review;



import com.loveyourdog.brokingservice.model.entity.Comment;
import com.loveyourdog.brokingservice.model.entity.Review;

import java.util.List;

//@Repository
public interface ReviewRepositoryCustom {


    public List<Review> findGoodReviews() ;

}