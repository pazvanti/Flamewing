package test.parsing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.petrepopescu.flamewing.controllers.FragmentController;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.FlamewingParser;
import tech.petrepopescu.flamewing.parser.compiler.Compiler;
import tech.petrepopescu.flamewing.parser.compiler.DynamicClassLoader;
import tech.petrepopescu.flamewing.parser.route.RouteGenerator;
import tech.petrepopescu.flamewing.special.FlamewingSpecialElementsUtil;
import tech.petrepopescu.flamewing.spring.FlamewingErrorHandler;
import tech.petrepopescu.flamewing.spring.FlamewingMessageConverter;
import tech.petrepopescu.flamewing.spring.SecurityConfig;
import tech.petrepopescu.flamewing.spring.config.FlamewingConfiguration;
import tech.petrepopescu.flamewing.spring.config.ViewsConfiguration;
import tech.petrepopescu.flamewing.views.View;
import tech.petrepopescu.utils.ReadFileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {SecurityConfig.class, FlamewingMessageConverter.class, RouteGenerator.class, Compiler.class, ElementFactory.class,
        DynamicClassLoader.class, FlamewingConfiguration.class, FlamewingSpecialElementsUtil.class, FragmentController.class,
        FlamewingErrorHandler.class})
class ParsingTests {
    @Autowired
    private Compiler compiler;

    @Autowired
    private RouteGenerator routeGenerator;

    @Autowired
    private FlamewingSpecialElementsUtil flamewingSpecialElementsUtil;

    @Test
    void test() throws IOException {
        FlamewingConfiguration flamewingConfiguration = new FlamewingConfiguration();
        flamewingConfiguration.setViews(new ViewsConfiguration());
        flamewingConfiguration.getViews().setPath("src/test/resources/views");

        FlamewingParser parser = new FlamewingParser(new ElementFactory(null), routeGenerator, compiler, flamewingConfiguration);
        parser.parse();

        Map<String, List<Object>> tests = new HashMap<>();
        tests.put("simpleTest", List.of(0, 0));
        tests.put("forAndIfTest", List.of(4, List.of("one", "two", "three"), 3));
        tests.put("advanceForAndIfTest", List.of(0, 3, List.of("one", "two", "three", "for", "five")));
        tests.put("variableTest", List.of(3, 4, List.of("10", "90", "50")));
        tests.put("elseInScript", List.of(3));
        tests.put("autowiredTest", List.of(3));

        List<Object> rawTestParams = new ArrayList<>();
        rawTestParams.add("<span>test</span>");
        rawTestParams.add(null);
        tests.put("rawTest", rawTestParams);

        for (Map.Entry<String, List<Object>> entry : tests.entrySet()) {
            String htmlContent = View.of(entry.getKey(), entry.getValue().toArray(new Object[0])).getContent(flamewingSpecialElementsUtil);
            String expected = ReadFileUtil.readFileToString(new File("src/test/resources/expected/" + entry.getKey() + ".html"), Charset.defaultCharset());
            assertEquals(expected, htmlContent, entry.getKey());
        }
    }
}
