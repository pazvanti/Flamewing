package tech.petrepopescu.flamewing.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.petrepopescu.flamewing.controllers.FragmentController;
import tech.petrepopescu.flamewing.parser.ElementFactory;
import tech.petrepopescu.flamewing.parser.FlamewingParser;
import tech.petrepopescu.flamewing.parser.compiler.FlamewingCompilerImpl;
import tech.petrepopescu.flamewing.parser.compiler.DynamicClassLoader;
import tech.petrepopescu.flamewing.parser.route.RouteGenerator;
import tech.petrepopescu.flamewing.special.FlamewingSpecialElementsUtil;
import tech.petrepopescu.flamewing.spring.config.FlamewingConfiguration;

import java.util.List;

@Configuration
@EnableConfigurationProperties
@Import({SecurityConfig.class, FlamewingMessageConverter.class, RouteGenerator.class, FlamewingCompilerImpl.class, ElementFactory.class,
        DynamicClassLoader.class, FlamewingConfiguration.class, FlamewingSpecialElementsUtil.class, FragmentController.class,
        FlamewingErrorHandler.class})
public class FlamewingConfig implements WebMvcConfigurer {
    @Autowired
    private FlamewingMessageConverter flamewingMessageConverter;

    @Autowired
    private FlamewingConfiguration config;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(flamewingMessageConverter);
    }

    @Bean
    public FlamewingParser templateParser(RouteGenerator routeGenerator, ElementFactory elementFactory, FlamewingCompilerImpl compiler, FlamewingConfiguration configuration) {
        FlamewingParser flamewingParser = new FlamewingParser(elementFactory, routeGenerator, compiler, configuration);
        flamewingParser.parse();
        return flamewingParser;
    }
}
