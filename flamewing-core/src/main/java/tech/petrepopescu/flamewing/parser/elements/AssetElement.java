package tech.petrepopescu.flamewing.parser.elements;

import tech.petrepopescu.flamewing.utils.StringUtils;
import org.springframework.stereotype.Controller;
import tech.petrepopescu.flamewing.parser.ElementFactory;

import java.util.List;

@Controller
public class AssetElement extends Element {
    private String path;

    public AssetElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        final String line = lines.get(this.lineNumber);
        String lineNoRoute = StringUtils.substring(line, "@asset.path(\"".length());
        int elementEnd = StringUtils.indexOf(lineNoRoute, "\")");

        // TODO: We need to be able to parse variable inside asset element path
        path = StringUtils.substring(lineNoRoute, 0, elementEnd);
        discoverNextElement(StringUtils.substring(lineNoRoute, elementEnd + 2), fileName);
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        appendWithContentBuilder("ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()");
        appendAsStringWithContentBuilder(path);
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        }
        return this.contentBuilder;
    }
}
