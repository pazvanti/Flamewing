package tech.petrepopescu.flamewing.special;

public class Stringifier {
    public static String toString(String string) {
        return escape(string);
    }

    private static String escape(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        } else {
            StringBuilder builder = null;
            int prevPosition = 0;
            for (int count = 0; count < str.length(); count++) {
                char c = str.charAt(count);
                String sanitized = switch (c) {
                    case '"' -> "&quot;";
                    case '&' -> "&amp;";
                    case '\'' -> "&#39;";
                    case '<' -> "&lt;";
                    case '>' -> "&gt;";
                    default -> null;
                };

                if (sanitized != null) {
                    if (builder == null) {
                         builder = new StringBuilder(str.length() + sanitized.length());
                    }
                    builder.append(str, prevPosition, count);
                    prevPosition = count + 1;
                    builder.append(sanitized);
                }
            }
            if (builder != null) {
                return builder.toString();
            } else {
                return str;
            }
        }
    }


    public static String toString(int value) {
        return Integer.toString(value);
    }

    public static String toString(double value) {
        return Double.toString(value);
    }

    public static String toString(float value) {
        return Float.toString(value);
    }

    public static String toString(long value) {
        return Long.toString(value);
    }

    public static String toString(char value) {
        return Character.toString(value);
    }

    public static String toString(short value) {
        return Short.toString(value);
    }

    public static String toString(byte value) {
        return Byte.toString(value);
    }

    public static String toString(boolean value) {
        return Boolean.toString(value);
    }

    public static String toString(Object object) {
        return object.toString();
    }
}
