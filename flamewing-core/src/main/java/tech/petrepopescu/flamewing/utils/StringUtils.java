package tech.petrepopescu.flamewing.utils;

import java.util.Arrays;
import java.util.Objects;

public class StringUtils {
    private StringUtils() {}

    public static boolean equals(String str1, String str2) {
        return Objects.equals(str1, str2);
    }

    public static boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isBlank(StringBuilder builder) {
        return builder == null || builder.toString().isBlank();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isEmpty(StringBuilder builder) {
        return builder == null || builder.isEmpty();
    }

    public static boolean contains(String str, String substring) {
        if (str == null || substring == null) return str == substring;
        return str.contains(substring);
    }

    public static boolean contains(String str, char c) {
        if (str == null) return false;
        return str.indexOf(c) != -1;
    }

    public static boolean startsWith(String str, String prefix) {
        if (str == null || prefix == null) return str == prefix;
        return str.startsWith(prefix);
    }

    public static boolean startsWithAny(String str, String... prefixes) {
        for (String prefix : prefixes) {
            if (startsWith(str, prefix)) return true;
        }
        return false;
    }

    public static boolean endsWith(String str, String suffix) {
        if (str == null || suffix == null) return str == suffix;
        return str.endsWith(suffix);
    }

    public static String substring(String str, int start) {
        if (str == null) return null;
        return substring(str, start, str.length());
    }

    public static String substring(String str, int start, int end) {
        if (str == null) return null;

        if (end < 0) {
            end = str.length() + end;
        }

        if (start < 0) {
            start = str.length() + start;
        }

        if (end > str.length()) {
            end = str.length();
        }

        if (start > end) {
            return "";
        }

        return str.substring(start, end);
    }

    public static String substringBetween(final String str, final String open, final String close) {
        if (str == null) return null;
        final int start = str.indexOf(open);
        if (start != -1) {
            final int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    public static String[] split(String str, String separator) {
        if (str == null) return new String[] {};
        return Arrays.stream(str.split(separator)).filter(StringUtils::isNotBlank).toList().toArray(new String[0]);
    }

    public static String[] split(String str, char separator) {
        if (str == null) return new String[] {};
        return str.split(String.valueOf(separator));
    }

    public static int indexOf(String str, char c) {
        return indexOf(str, c, 0);
    }

    public static int indexOf(String str, String substr) {
        return indexOf(str, substr, 0);
    }

    public static int indexOf(String str, String substr, int start) {
        if (str == null) return -1;
        return str.indexOf(substr, start);
    }

    public static int indexOf(String str, char c, int start) {
        if (str == null) return -1;
        return str.indexOf(c, start);
    }

    public static int lastIndexOf(String str, char c) {
        if (str == null) return -1;
        return str.lastIndexOf(c);
    }

    public static int lastIndexOf(String str, String substr) {
        if (str == null) return -1;
        return str.lastIndexOf(substr);
    }

    public static String repeat(char c, int count) {
        return new String(new char[count]).replace("\0", String.valueOf(c));
    }

    public static String repeat(CharSequence str, int count) {
        return new String(new char[count]).replace("\0", str);
    }

    public static int countMatches(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) count++;
        }
        return count;
    }

    public static int countMatches(String str, String substr) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.startsWith(substr, i)) count++;
        }
        return count;
    }

    public static String trim(String str) {
        if (str == null) return null;
        return str.trim();
    }

    public static String replace(String str, String oldValue, String newValue) {
        if (str == null) return null;
        return str.replace(oldValue, newValue);
    }

    public static String remove(String str, String toRemove) {
        if (str == null) return null;
        return str.replace(toRemove, "");
    }

    public static String remove(String str, char toRemove) {
        if (str == null) return null;
        return str.replace(String.valueOf(toRemove), "");
    }
}
