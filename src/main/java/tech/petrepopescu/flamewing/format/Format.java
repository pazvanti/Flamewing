package tech.petrepopescu.flamewing.format;

import org.springframework.http.MediaType;
import tech.petrepopescu.flamewing.special.FlamewingSpecialElementsUtil;

public abstract class Format {
    public abstract String getContent(FlamewingSpecialElementsUtil specialElementsUtil);

    public abstract MediaType getMediaType();
}
