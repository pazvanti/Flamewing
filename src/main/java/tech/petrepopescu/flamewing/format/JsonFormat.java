package tech.petrepopescu.flamewing.format;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import tech.petrepopescu.flamewing.special.FlamewingSpecialElementsUtil;

public class JsonFormat extends Format {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JsonFormat.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Object object;

    public JsonFormat(Object object) {
        this.object = object;
    }

    public JsonFormat() {
        this.object = null;
    }

    @Override
    public String getContent(FlamewingSpecialElementsUtil specialElementsUtil) {
        if (this.object == null) {
            return "";
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error parsing object to json", e);
            return "{}";
        }
    }

    @Override
    public MediaType getMediaType() {
        return MediaType.APPLICATION_JSON;
    }
}
