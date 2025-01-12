package com.rdg.rdg_2025.rdg_2025_spring.helpers;

public class SlugUtils {

    public static String generateSlug(String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9]", "-").replaceAll("-{2,}", "-");

    }

}
