package com.example.kpx.holeinmywallet;

public class Utility {
    public static String replaceDotsWithEquals(String email) {
        String result = email;
        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) == '.') {
                result = result.substring(0, i) + "=" + (i < result.length() - 1 ? email.substring(i + 1) : "");
            }
        }
        return result;
    }
}
