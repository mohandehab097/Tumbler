package com.social.Tumblr.security.utils;

public abstract class Utility {

    public static String findFirstNameOfUser(String fullName) {
        if (fullName.equals("") && fullName.isEmpty() && fullName != null) {
            String[] firstName = fullName.split(" ");
            return firstName[0];
        }

        return "";
    }

}
