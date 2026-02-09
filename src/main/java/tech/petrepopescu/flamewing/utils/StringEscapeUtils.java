package tech.petrepopescu.flamewing.utils;

import java.util.HashMap;
import java.util.Map;

public class StringEscapeUtils {
    private StringEscapeUtils() {}

    private static final Map<Character, String> HTML_ESCAPE_MAP;

    static {
        final Map<Character, String> initialMap = new HashMap<>();
        initialMap.put('&', "&amp;");
        initialMap.put('<', "&lt;");
        initialMap.put('>', "&gt;");
        initialMap.put('"', "&quot;");
        initialMap.put('\'', "&#39;");
        initialMap.put('`', "&#x60;");
        initialMap.put('=', "&#x3D;");
        HTML_ESCAPE_MAP = initialMap;
    }
    /**
     * Escapes HTML special characters in a string to prevent XSS attacks and HTML injection.
     * Similar to StringEscapeUtils.escapeHtml4 from Apache Commons Text.
     *
     * @param input the string to escape, may be null
     * @return a new escaped string, null if null string input
     */
    public static String escapeHtml(String input) {
        if (input == null) {
            return null;
        }

        StringBuilder escaped = new StringBuilder(input.length() * 2);

        for (int i = 0; i < input.length(); i++) {
            if (HTML_ESCAPE_MAP.containsKey(input.charAt(i))) {
                escaped.append(HTML_ESCAPE_MAP.get(input.charAt(i)));
            } else {
                escaped.append(input.charAt(i));
            }
        }

        return escaped.toString();
    }

    private static final Map<Character, String> JAVA_ESCAPE_MAP;

    static {
        final Map<Character, String> initialMap = new HashMap<>();
        initialMap.put('\\', "\\\\");
        initialMap.put('"', "\\\"");
        initialMap.put('\'', "\\'");
        initialMap.put('\b', "\\b");
        initialMap.put('\n', "\\n");
        initialMap.put('\t', "\\t");
        initialMap.put('\f', "\\f");
        initialMap.put('\r', "\\r");

        JAVA_ESCAPE_MAP = initialMap;
    }

    public static String escapeJava(String input) {
        if (input == null) {
            return null;
        }

        StringBuilder escaped = new StringBuilder(input.length() * 2);

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (JAVA_ESCAPE_MAP.containsKey(ch)) {
                escaped.append(JAVA_ESCAPE_MAP.get(ch));
            } else {
                escaped.append(ch);
            }
        }

        return escaped.toString();
    }
}
