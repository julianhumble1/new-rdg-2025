package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.rdg.rdg_2025.rdg_2025_spring.exception.DatabaseException;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Getter
public class CloudinaryService {

    @Value("${rdg.app.cloudinaryName}")
    private String apiName;

    @Value("${rdg.app.cloudinaryApiKey}")
    private String apiKey;

    @Value("${rdg.app.cloudinaryApiSecret}")
    private String apiSecret;

    private Long timestamp;

    // helper to build configured Cloudinary instance
    private Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", apiName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    public String generateSecureSignature(String publicId, String uploadPreset, String folder) {
        Long timestamp = System.currentTimeMillis() / 1000L;
        this.timestamp = timestamp;
        Map<String, Object> paramsToSign = ObjectUtils.asMap(
                "timestamp", timestamp,
                "upload_preset", uploadPreset,
                "public_id", publicId,
                "asset_folder", folder
        );
        // use cloudinary instance (but apiSignRequest only needs the secret)
        return cloudinary().apiSignRequest(paramsToSign, apiSecret);
    }

    public String generateSecureUrl(String publicId) {
        try {
            Map<String, Object> resource = cloudinary().api().resource(publicId, ObjectUtils.emptyMap());
            Object secureUrl = resource.get("secure_url");
            return secureUrl != null ? secureUrl.toString() : null;
        } catch (Exception ex) {
            String msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
            if (msg.contains("404") || msg.contains("not found")) {
                // resource does not exist
                throw new EntityNotFoundException("No image with id: " + publicId, ex);
            }
            // handle not found / other errors as you prefer
            throw new DatabaseException("Failed to fetch Cloudinary resource: " + ex.getMessage(), ex);
        }
    }
}