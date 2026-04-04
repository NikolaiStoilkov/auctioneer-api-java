package com.auctioneer.controller.comment;

import com.auctioneer.dtos.comment.CommentDto;
import com.auctioneer.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{adId}")
    public List<CommentDto> getComments(@PathVariable Long adId) {
        return commentService.getAll(adId);
    }

    @PostMapping("/create/{adId}")
    public void create(@RequestBody CommentDto commentDto) {
        commentService.create(commentDto);
    }

    @PutMapping("/edit")
    public void edit(@RequestBody CommentDto commentDto) {
        commentService.edit(commentDto);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }
}
