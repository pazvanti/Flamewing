package tech.petrepopescu.flamewing.format;

import org.springframework.http.MediaType;
import tech.petrepopescu.flamewing.special.ContentGroup;
import tech.petrepopescu.flamewing.special.FlamewingSpecialElementsUtil;

public abstract class HtmlFormat extends Format {
    protected ContentGroup contentGroup = new ContentGroup();

    public abstract String getContentForSection(String sectionName, FlamewingSpecialElementsUtil specialElementsUtil);
    @Override
    public MediaType getMediaType() {
        return MediaType.TEXT_HTML;
    }

    public void setContentGroup(ContentGroup contentGroup) {
        this.contentGroup = contentGroup;
    }
}
