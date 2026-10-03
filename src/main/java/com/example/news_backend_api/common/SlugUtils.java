package com.example.news_backend_api.common;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class SlugUtils {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern MULTIDASH = Pattern.compile("-+");

    private SlugUtils() {
    }

    public static String toSlug(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String str = input.trim().toLowerCase(Locale.ROOT);
        // Replace special Vietnamese characters
        str = str.replace('đ', 'd').replace('Đ', 'd');
        // Decompose unicode characters into base letter and accent
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        // Remove diacritical marks
        String stripped = Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(normalized).replaceAll("");
        // Replace whitespace with dash
        String noWhiteSpace = WHITESPACE.matcher(stripped).replaceAll("-");
        // Remove non-word characters
        String slug = NONLATIN.matcher(noWhiteSpace).replaceAll("");
        // Replace multiple dashes with single dash
        slug = MULTIDASH.matcher(slug).replaceAll("-");
        // Remove leading or trailing dashes
        return slug.replaceAll("^-|-$", "");
    }
}
