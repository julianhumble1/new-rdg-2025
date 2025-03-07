package com.rdg.rdg_2025.rdg_2025_spring.helpers;

import com.github.slugify.Slugify;

public class SlugUtils {

    public static String generateSlug(String name) {
        Slugify slg = Slugify.builder().build();
        return slg.slugify(name);

    }

    public static String generateSlug(String firstName, String secondName) {
        String fullName = firstName + " " +  secondName;
        Slugify slg = Slugify.builder().build();
        return slg.slugify(fullName);
    }

}
