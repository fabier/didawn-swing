package com.didawn.utils;

import java.util.Arrays;

public class FileNameCleaner {

    private static final int[] ILLEGAL_CHARS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 34, 42, 47, 58, 60, 62, 63, 92, 124 };

    public static String removeIllegalChars(String badFileName) {
        StringBuilder cleanName = new StringBuilder();

        for (int i = 0; i < badFileName.length(); ++i) {
            char c = badFileName.charAt(i);
            if (Arrays.binarySearch(ILLEGAL_CHARS, c) < 0) {
                cleanName.append(c);
            }
        }

        return cleanName.toString();
    }
}
