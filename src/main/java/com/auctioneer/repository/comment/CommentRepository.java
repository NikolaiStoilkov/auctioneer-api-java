package com.auctioneer.repository.comment;

import com.auctioneer.domain.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByAdId(Long adId);

    Comment getCommentById(Long id);
}
