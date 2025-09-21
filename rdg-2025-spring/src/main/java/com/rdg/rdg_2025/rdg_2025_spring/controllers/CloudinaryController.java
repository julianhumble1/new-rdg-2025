package com.rdg.rdg_2025.rdg_2025_spring.controllers;

import com.rdg.rdg_2025.rdg_2025_spring.payload.response.cloudinary.CloudinarySignatureResponse;
import com.rdg.rdg_2025.rdg_2025_spring.payload.response.cloudinary.CloudinaryUrlResponse;
import com.rdg.rdg_2025.rdg_2025_spring.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/cloudinary")
public class CloudinaryController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/generate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> generateSignature(@RequestParam String publicId, @RequestParam String uploadPreset, @RequestParam String folder) {
        try {
            String signature = cloudinaryService.generateSecureSignature(publicId, uploadPreset, folder);
            String apiKey = cloudinaryService.getApiKey();
            Long timestamp = cloudinaryService.getTimestamp();
            return ResponseEntity.ok().body(new CloudinarySignatureResponse(signature, apiKey, timestamp));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/url")
    public ResponseEntity<?> generateUrl(@RequestParam String publicId) {
        try {
            String url = cloudinaryService.generateSecureUrl(publicId);
            return ResponseEntity.ok().body(new CloudinaryUrlResponse(url));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

}
