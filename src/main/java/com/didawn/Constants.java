package com.didawn;

public class Constants {

    public static String DIDAWN = "XYYnYf";
    public static int ROLL = 12;
    private static String DAWN;
    public static String KEY1 = "g" + "4" + "el" + "58" + "wc" + "0" + "zvf" + "9" + "na" + "1";
    public static String KEY2 = "jo" + "6" + "aey" + "6" + "haid" + "2" + "Teih";

    public static String dawn() {
        if (DAWN == null) {
            synchronized (DIDAWN) {
                StringBuilder sb = new StringBuilder();
                for (char c : DIDAWN.toCharArray()) {
                    sb.append((char) (c + ROLL));
                }
                DAWN = sb.toString();
            }
        }
        return DAWN;
    }

    public static String dawnDotCom() {
        return dawn() + ".com";
    }
}
