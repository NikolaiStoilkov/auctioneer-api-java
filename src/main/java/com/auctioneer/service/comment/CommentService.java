package com.auctioneer.service.comment;

import com.auctioneer.domain.entities.Comment;
import com.auctioneer.dtos.comment.CommentDto;
import com.auctioneer.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<CommentDto> getAll(Long adId) {
        List<Comment> comments = commentRepository.findAllByAdId(adId);

        List<CommentDto> commentsDto = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDto dto = new CommentDto();
            BeanUtils.copyProperties(comment, dto);
            commentsDto.add(dto);
        }

        return commentsDto;
    }

    public void create(CommentDto commentDto) {
        Comment comment = new Comment();

        BeanUtils.copyProperties(commentDto, comment);

        commentRepository.save(comment);
    }

    public void edit(CommentDto commentDto) {
        Long id = commentDto.getId();

        Comment comment = commentRepository.getCommentById(id);

        BeanUtils.copyProperties(commentDto, comment);

        commentRepository.save(comment);
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}
