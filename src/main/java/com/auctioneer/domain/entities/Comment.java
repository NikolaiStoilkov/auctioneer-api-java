package com.auctioneer.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * A user comment on an ad. Created/updated timestamps are managed
 * automatically by JPA auditing.
 */
@Setter
@Getter
@Entity
@Table(
        name = "COMMENTS"
)
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(name = "ad_id", nullable = false)
    private Long adId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
