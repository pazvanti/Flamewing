package tech.petrepopescu.flamewing.views.predefined;

import tech.petrepopescu.flamewing.format.HtmlFormat;
import tech.petrepopescu.flamewing.special.FlamewingSpecialElementsUtil;

import java.io.InputStream;
import java.util.Base64;

public abstract class PredefinedView extends HtmlFormat {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PredefinedView.class);

    @Override
    public String getContentForSection(String sectionName, FlamewingSpecialElementsUtil specialElementsUtil) {
        // Predefined views only have the HTML section.
        return getContent(specialElementsUtil);
    }

    @Override
    public String getContent(FlamewingSpecialElementsUtil specialElementsUtil) {

        StringBuilder builder = new StringBuilder();
        builder.append("<head>\n").append("        <title>").append(getTitle()).append("</title>\n").append("    </head>");
        builder.append("<body style=\"background-color: #1d1b1b; margin: 0px; padding: 0px;\">");
        builder.append("<div style=\"text-align: center;\">");
        String logo = logoBase64();
        if (logo != null) {
            builder.append("<img src=\"data:image/png;base64,").append(logo).append("\" />");
        }
        builder.append("</div>");
        builder.append("<div style=\"text-align: center; border-radius: 15px; border-style:solid; border-color: ").append(getBorderColor()).append("; width: 70%; margin: auto; margin-top: 10px; background-color: ").append(getBackgroundColor()).append(";\">");
        builder.append("<h2 style=\"color: ").append(getTextColor()).append("; font-family:Arial, Helvetica, sans-serif\">");
        builder.append(getMessage());
        if (getSubMessage() != null) {
            builder.append("<p style=\"color: ").append(getTextColor()).append("; font-family:Arial, Helvetica, sans-serif\">");
            builder.append(getSubMessage());
            builder.append("</p>");
        }
        builder.append("</div>\n" + "    </body>");
        builder.append("</html>");
        return builder.toString();
    }

    private String logoBase64() {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("logo_small.png")) {
            if (is == null) {
                return null;
            }
            byte[] bytes = is.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            log.warn("Error reading Flamewing logo");
        }
        return null;
    }

    abstract String getTitle();

    abstract String getMessage();

    String getSubMessage() {
        return null;
    }

    String getBorderColor() {
        return "#087990";
    }

    String getBackgroundColor() {
        return "#032830;";
    }

    String getTextColor() {
        return "#6edff6";
    }
}
