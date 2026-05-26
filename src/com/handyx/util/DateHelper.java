package com.handyx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    private static final String DATE_PATTERN     = "dd-MM-yyyy";
    private static final String DATETIME_PATTERN = "dd-MM-yyyy HH:mm";
    private static final String EMPTY            = "-";

    private DateHelper() {
    }

    public static Long parseDate(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN, Locale.ROOT);
        sdf.setLenient(false);
        try { return sdf.parse(input.trim()).getTime(); }
        catch (ParseException e) { return null; }
    }

    public static String formatDate(long millis) {
        if (millis == 0) return EMPTY;
        return new SimpleDateFormat(DATE_PATTERN, Locale.ROOT).format(new Date(millis));
    }

    public static String formatDateTime(long millis) {
        if (millis == 0) return EMPTY;
        return new SimpleDateFormat(DATETIME_PATTERN, Locale.ROOT).format(new Date(millis));
    }

    public static boolean isSameDay(long a, long b) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN, Locale.ROOT);
        return sdf.format(new Date(a)).equals(sdf.format(new Date(b)));
    }

    public static String stars(int rating) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 5; i++) sb.append(i <= rating ? "" : "");
        return sb.toString();
    }

    public static String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}
