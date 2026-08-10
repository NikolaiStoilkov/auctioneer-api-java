package com.auctioneer.service.image;

import com.auctioneer.domain.entities.Image;
import com.auctioneer.exceptions.ImageNotFoundException;
import com.auctioneer.repository.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Stores and retrieves binary images held in the database.
 */
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    /**
     * Persists an uploaded file and returns its generated code.
     *
     * @param file the multipart file to store
     * @return the generated image code
     * @throws IllegalArgumentException if the file is empty
     */
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file must not be empty");
        }

        Image image = new Image();
        try {
            image.setBytes(file.getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read uploaded image", e);
        }
        image.setContentType(file.getContentType());
        image.setFileName(file.getOriginalFilename());

        return imageRepository.save(image).getCode();
    }

    /**
     * Loads an image by its code.
     *
     * @param code the image code
     * @return the image
     * @throws ImageNotFoundException if no image exists with the given code
     */
    public Image get(String code) {
        return imageRepository.findById(code)
                .orElseThrow(() -> new ImageNotFoundException(code));
    }
}
