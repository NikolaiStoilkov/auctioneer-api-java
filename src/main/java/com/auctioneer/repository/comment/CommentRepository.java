package com.auctioneer.repository.comment;

import com.auctioneer.domain.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access for {@link Comment}.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Returns all comments on the given ad.
     *
     * @param adId the ad id
     * @return the ad's comments
     */
    List<Comment> findAllByAdId(Long adId);

    /**
     * Returns a comment by id, or {@code null} if none exists.
     *
     * @param id the comment id
     * @return the comment or {@code null}
     */
    Comment getCommentById(Long id);
}
