package tech.petrepopescu.flamewing.spring;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import tech.petrepopescu.flamewing.format.Format;
import tech.petrepopescu.flamewing.special.FlamewingSpecialElementsUtil;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class FlamewingMessageConverter extends AbstractHttpMessageConverter<Format> {
    private final FlamewingSpecialElementsUtil flamewingSpecialElementsUtil;

    public FlamewingMessageConverter(FlamewingSpecialElementsUtil flamewingSpecialElementsUtil) {
        super(MediaType.TEXT_HTML, MediaType.APPLICATION_JSON);
        this.flamewingSpecialElementsUtil = flamewingSpecialElementsUtil;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Format.class.isAssignableFrom(clazz);
    }

    @Override
    protected Format readInternal(Class<? extends Format> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(Format flamewingFormat, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        String data = flamewingFormat.getContent(flamewingSpecialElementsUtil);

        outputMessage.getHeaders().setContentType(flamewingFormat.getMediaType());

        // write the response body
        OutputStream outputStream = outputMessage.getBody();
        outputStream.write(data.getBytes());
        outputStream.flush();
    }
}
