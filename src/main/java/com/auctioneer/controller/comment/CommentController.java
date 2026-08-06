package com.auctioneer.controller.comment;

import com.auctioneer.dtos.comment.CommentDto;
import com.auctioneer.service.comment.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /**
     * Returns all comments on an ad.
     *
     * @param adId the id of the ad
     * @return the ad's comments
     */
    @GetMapping("/{adId}")
    public List<CommentDto> getComments(@PathVariable Long adId) {
        return commentService.getAll(adId);
    }

    /**
     * Creates a new comment.
     *
     * @param commentDto the comment to create
     */
    @PostMapping("/create/{adId}")
    public void create(@Valid @RequestBody CommentDto commentDto) {
        commentService.create(commentDto);
    }

    /**
     * Updates an existing comment.
     *
     * @param commentDto the new comment data, including the comment id
     */
    @PutMapping("/edit")
    public void edit(@Valid @RequestBody CommentDto commentDto) {
        commentService.edit(commentDto);
    }

    /**
     * Deletes a comment.
     *
     * @param id the id of the comment to delete
     */
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }
}
