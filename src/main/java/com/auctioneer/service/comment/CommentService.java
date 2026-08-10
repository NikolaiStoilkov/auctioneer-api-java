package com.auctioneer.service.comment;

import com.auctioneer.domain.entities.Comment;
import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.comment.CommentDto;
import com.auctioneer.exceptions.CommentNotFoundException;
import com.auctioneer.repository.comment.CommentRepository;
import com.auctioneer.repository.user.UserRepository;
import com.auctioneer.service.discordNotifications.DiscordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final DiscordService discordService;

    public List<CommentDto> getAll(Long adId) {
        List<Comment> comments = commentRepository.findAllByAdId(adId);
        List<CommentDto> commentsDto = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDto dto = new CommentDto();

            BeanUtils.copyProperties(comment, dto);

            userRepository.findById(comment.getAuthorId()).ifPresent(user -> dto.setAuthorUsername(user.getUsername()));

            commentsDto.add(dto);
        }

        return commentsDto;
    }

    /**
     * Creates a comment. Timestamps are populated by JPA auditing.
     *
     * @param commentDto the comment to create
     */
    public void create(CommentDto commentDto) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDto, comment, "id", "createdAt", "updatedAt");

        commentRepository.save(comment);
        discordService.sendCommentNotification("💬 New comment on ad " + commentDto.getAdId() + " by user " + commentDto.getAuthorId());
    }

    /**
     * Updates an existing comment. The {@code updatedAt} timestamp is
     * refreshed automatically by JPA auditing; {@code createdAt} is preserved.
     *
     * @param commentDto the new comment data, including the comment id
     * @throws CommentNotFoundException if no comment exists with the given id
     */
    public void edit(CommentDto commentDto) {
        Long id = commentDto.getId();
        Comment comment = commentRepository.getCommentById(id);

        if (comment == null) {
            throw new CommentNotFoundException(id);
        }

        // Preserve the auditing-managed timestamps
        BeanUtils.copyProperties(commentDto, comment, "id", "createdAt", "updatedAt");

        commentRepository.save(comment);
        discordService.sendCommentNotification("✏️ Comment " + commentDto.getId() + " edited on ad " + commentDto.getAdId());
    }

    /**
     * Deletes a comment by id.
     *
     * @param id the id of the comment to delete
     */
    public void delete(Long id) {
        commentRepository.deleteById(id);
        discordService.sendCommentNotification("🗑️ Comment " + id + " deleted");
    }
}
