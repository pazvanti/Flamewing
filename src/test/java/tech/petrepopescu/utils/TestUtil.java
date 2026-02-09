package tech.petrepopescu.utils;

import tech.petrepopescu.flamewing.utils.StringUtils;

public class TestUtil {
    public static String sanitizeResult(String result) {
        StringBuilder builder = new StringBuilder();
        String[] lines = StringUtils.split(result, "\n");
        for (String line:lines) {
            if (line.contains("STATIC_HTML_")) {
                builder.append(removeUUID(line));
            } else {
                builder.append(line);
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private static String removeUUID(String line) {
        String uuid = StringUtils.substringBetween(line, "STATIC_HTML_", ")");
        return StringUtils.replace(line, uuid, "THISISUUID");
    }
}
