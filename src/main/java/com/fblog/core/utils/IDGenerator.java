package com.fblog.core.utils;

import java.util.UUID;

/**
 * id生成器
 */
public class IDGenerator {
    private IDGenerator() {
    }

    /**
     * 以62进制（字母加数字）生成19位UUID，最短的UUID
     */
    public static String uuid19() {
        UUID uuid = UUID.randomUUID();
        StringBuilder sb = new StringBuilder();
        sb.append(digits(uuid.getMostSignificantBits() >> 32, 8));
        sb.append(digits(uuid.getMostSignificantBits() >> 16, 4));
        sb.append(digits(uuid.getMostSignificantBits(), 4));
        sb.append(digits(uuid.getMostSignificantBits(), 12));
        return sb.toString();
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return NumberUtils.toString(hi | (val & (hi - 1)), NumberUtils.MAX_RADIX).substring(1);
    }

    /**
     * 32位的uuid
     */
    public static String uuid32() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }

}
