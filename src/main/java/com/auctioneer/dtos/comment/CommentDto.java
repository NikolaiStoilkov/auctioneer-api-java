package com.auctioneer.dtos.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private Long id;

    @NotBlank(message = "Content is required")
    @Size(min = 1, max = 100, message = "Content must be between 1 and 100 characters")
    private String content;

    @NotNull(message = "Author ID is required")
    private Long authorId;

    @NotNull(message = "Ad ID is required")
    private Long adId;
}
