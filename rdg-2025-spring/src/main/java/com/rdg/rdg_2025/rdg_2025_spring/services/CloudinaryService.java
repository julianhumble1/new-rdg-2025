package com.rdg.rdg_2025.rdg_2025_spring.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.Getter;
import org.aspectj.weaver.SignatureUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service @Getter
public class CloudinaryService {

    @Value("${rdg.app.cloudinaryName}")
    private String apiName;

    @Value("${rdg.app.cloudinaryApiKey}")
    private String apiKey;

    @Value("${rdg.app.cloudinaryApiSecret}")
    private String apiSecret;

    private Long timestamp;

    public String generateSecureSignature(String publicId, String uploadPreset) {
        Long timestamp = System.currentTimeMillis() / 1000L;
        this.timestamp = timestamp;
        Map<String, Object> paramsToSign = ObjectUtils.asMap(
                "timestamp", timestamp,
                "upload_preset", uploadPreset,
                "public_id", publicId
        );
        Cloudinary cloudinary = new Cloudinary();
        return cloudinary.apiSignRequest(paramsToSign, apiSecret);
    }
}
