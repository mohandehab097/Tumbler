package com.social.Tumblr.security.utils;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Constants {

    public static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 20;
    public static final Integer tokenExpiration = 86400000;

    public static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    public static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    public static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    public static final Pattern SPECIAL_CHARACTER_PATTERN = Pattern.compile("[^a-zA-Z0-9]");

    public static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9. ()-]{7,25}$");

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM");

}
