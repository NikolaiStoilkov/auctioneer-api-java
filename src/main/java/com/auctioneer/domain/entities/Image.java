package com.auctioneer.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * A binary image stored directly in the database. The raw bytes live in a
 * {@code @Lob} column (mapped to BLOB/VARBINARY depending on the dialect),
 * alongside the original content type and file name.
 */
@Getter
@Setter
@Entity
@Table(name = "images")
@EntityListeners(AuditingEntityListener.class)
public class Image {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private String code;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "bytes")
    private byte[] bytes;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
