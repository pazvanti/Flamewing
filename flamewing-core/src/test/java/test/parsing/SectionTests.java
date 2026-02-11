package test.parsing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.petrepopescu.flamewing.controllers.FragmentController;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.FlamewingParser;
import tech.petrepopescu.flamewing.parser.compiler.FlamewingCompiler;
import tech.petrepopescu.flamewing.parser.compiler.DynamicClassLoader;
import tech.petrepopescu.flamewing.parser.compiler.FlamewingCompilerImpl;
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
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {SecurityConfig.class, FlamewingMessageConverter.class, RouteGenerator.class, FlamewingCompilerImpl.class, ElementFactory.class,
        DynamicClassLoader.class, FlamewingConfiguration.class, FlamewingSpecialElementsUtil.class, FragmentController.class,
        FlamewingErrorHandler.class})
class SectionTests {
    @Autowired
    private FlamewingCompilerImpl compiler;

    @Autowired
    private RouteGenerator routeGenerator;

    @Autowired
    private FlamewingSpecialElementsUtil flamewingSpecialElementsUtil;

    @Test
    void test() throws Exception {
        FlamewingConfiguration flamewingConfiguration = new FlamewingConfiguration();
        flamewingConfiguration.setViews(new ViewsConfiguration());
        flamewingConfiguration.getViews().setPath("src/test/resources/section");

        FlamewingParser parser = new FlamewingParser(new ElementFactory(null), routeGenerator, compiler, flamewingConfiguration);
        parser.parse();

        Map<String, List<Object>> tests = new HashMap<>();
        tests.put("simpleSectionTest", List.of(3));
        tests.put("insertOnceTest", List.of(3));

        // We need to repeat the insertOnce test to make sure that second call is correct as well
        tests.put("insertOnceTest", List.of(3));

        tests.put("sectionTest", List.of(10));
        tests.put("complexSectionTest", List.of(1));
        tests.put("sectionTestWithLoop", List.of(1));

        for (Map.Entry<String, List<Object>> entry : tests.entrySet()) {
            String htmlContent = View.of(entry.getKey(), entry.getValue().toArray(new Object[0])).getContent(flamewingSpecialElementsUtil);
            String expected = ReadFileUtil.readFileToString(new File("src/test/resources/section/expected/" + entry.getKey() + ".html"), Charset.defaultCharset());
            assertEquals(expected, htmlContent, "Failed for test: " + entry.getKey());
        }
    }
}
