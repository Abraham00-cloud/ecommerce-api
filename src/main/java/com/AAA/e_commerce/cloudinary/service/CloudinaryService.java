package com.AAA.e_commerce.cloudinary.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String folderName) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload an empty file.");
        }

        try {
            Map<?,?> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folderName,
                            "resource_type", "auto"
                    ));
            String secureUrl = uploadResult.get("secure_url").toString();
            log.info("Successfully uploaded image to Cloudinary. URL: {}", secureUrl);

            return secureUrl;
        } catch (IOException e) {
            log.error("Failed to upload image to Cloudinary: {}", e.getMessage(), e);
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }

    public void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Successfully deleted image from Cloudinary. Public ID: {}", publicId);
        } catch (Exception e) {
            log.error("Failed to delete image from Cloudinary: {}", e.getMessage(), e);
            throw new RuntimeException("Image deletion failed: " + e.getMessage());
        }
    }
}
