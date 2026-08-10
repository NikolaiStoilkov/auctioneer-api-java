package com.auctioneer.controller.image;

import com.auctioneer.domain.entities.Image;
import com.auctioneer.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Uploads images to the database and serves their raw bytes back.
 */
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    /**
     * Stores an uploaded image.
     *
     * @param file the multipart image file (form field {@code file})
     * @return the generated image code
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) {
        String code = imageService.store(file);
        return Map.of("code", code);
    }

    /**
     * Returns the raw bytes of a stored image with its original content type.
     *
     * @param code the image code
     * @return the image bytes
     */
    @GetMapping("/{code}")
    public ResponseEntity<byte[]> download(@PathVariable String code) {
        Image image = imageService.get(code);

        MediaType mediaType = image.getContentType() != null
                ? MediaType.parseMediaType(image.getContentType())
                : MediaType.APPLICATION_OCTET_STREAM;

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok().contentType(mediaType);
        if (image.getFileName() != null) {
            builder.header(HttpHeaders.CONTENT_DISPOSITION,
                    "inline; filename=\"" + image.getFileName() + "\"");
        }

        return builder.body(image.getBytes());
    }
}
