package com.auctioneer.exceptions;

public class CommentNotFoundException extends ResourceNotFoundException {

    public CommentNotFoundException(Long commentId) {
        super("Comment not found: " + commentId);
    }
}
