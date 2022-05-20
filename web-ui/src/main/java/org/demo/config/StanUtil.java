package org.demo.config;

public class StanUtil {
    private static int stan = 0;
    public static String getStan(){
        stan ++;
        return String.format("%07d",stan);
    }
}
