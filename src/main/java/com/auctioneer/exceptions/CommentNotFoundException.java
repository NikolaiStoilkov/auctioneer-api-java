package com.auctioneer.exceptions;

/** Thrown when no comment exists for a given id; mapped to HTTP 404. */
public class CommentNotFoundException extends ResourceNotFoundException {

    public CommentNotFoundException(Long commentId) {
        super("Comment not found: " + commentId);
    }
}
