package com.rdg.rdg_2025.rdg_2025_spring.payload.response.cloudinary;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CloudinaryUrlResponse {
    public String url;

    public CloudinaryUrlResponse(String url) {
        this.url = url;
    }
}
