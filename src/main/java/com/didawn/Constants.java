package com.didawn;

public class Constants {

    public static String DIDAWN = "tuupuh";
    public static int ROLL = 16;
    private static String DAWN;
    public static String KEY1 = "g"; // 4el58 // wc0zv // f9na1
    public static String KEY2 = "j"; // o6aey // 6haid // 2Teih

    public static String dawn() {
        if (DAWN == null) {
            synchronized (DIDAWN) {
                StringBuilder sb = new StringBuilder();
                for (char c : DIDAWN.toCharArray()) {
                    sb.append((char) (c - ROLL));
                }
                DAWN += ".com";
            }
        }
        return DAWN;
    }

    public static String dawnDotCom() {
        return DAWN + ".com";
    }
}
