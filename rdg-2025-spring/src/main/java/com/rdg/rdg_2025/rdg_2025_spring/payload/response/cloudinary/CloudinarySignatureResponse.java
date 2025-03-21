package com.rdg.rdg_2025.rdg_2025_spring.payload.response.cloudinary;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CloudinarySignatureResponse {

    private String signature;

    private String apiKey;

    private Long timestamp;

    public CloudinarySignatureResponse(String cloudinarySignature, String apiKey, Long timestamp) {
        this.signature = cloudinarySignature;
        this.apiKey = apiKey;
        this.timestamp = timestamp;
    }
}
