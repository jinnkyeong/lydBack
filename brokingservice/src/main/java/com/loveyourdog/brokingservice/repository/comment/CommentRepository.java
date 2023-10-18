package com.loveyourdog.brokingservice.repository.comment;

import com.loveyourdog.brokingservice.model.entity.Application;
import com.loveyourdog.brokingservice.model.entity.Comment;
import com.loveyourdog.brokingservice.repository.application.ApplicationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long>, CommentRepositoryCustom {
}
