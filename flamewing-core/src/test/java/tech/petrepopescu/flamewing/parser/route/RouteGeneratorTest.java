package tech.petrepopescu.flamewing.parser.route;

import tech.petrepopescu.flamewing.spring.config.FlamewingConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class RouteGeneratorTest {

    @Test
    void testGetMethodSignature() {
        RouteGenerator generator = new RouteGenerator(mock(FlamewingConfiguration.class));
        
        RouteVariable mandatoryParam = RouteVariable.builder()
                .name("query")
                .varName("query")
                .varType(String.class)
                .required(true)
                .build();
        
        RouteVariable optionalParam = RouteVariable.builder()
                .name("page")
                .varName("page")
                .varType(Integer.class)
                .required(false)
                .build();

        RoutePath path = RoutePath.builder()
                .name("search")
                .url("/search")
                .method(HttpMethod.GET)
                .pathVariables(Collections.emptyList())
                .requestVariables(List.of(mandatoryParam, optionalParam))
                .build();

        assertEquals("search(java.lang.String query, java.lang.Integer page)", 
                generator.getMethodSignature(path, false));
        assertEquals("search(java.lang.String query)", 
                generator.getMethodSignature(path, true));
    }

    @Test
    void testGetUrl() {
        RouteGenerator generator = new RouteGenerator(mock(FlamewingConfiguration.class));
        
        RouteVariable mandatoryParam = RouteVariable.builder()
                .name("query")
                .varName("query")
                .varType(String.class)
                .required(true)
                .build();
        
        RouteVariable optionalParam = RouteVariable.builder()
                .name("page")
                .varName("page")
                .varType(Integer.class)
                .required(false)
                .build();

        RoutePath path = RoutePath.builder()
                .name("search")
                .url("/search")
                .method(HttpMethod.GET)
                .pathVariables(Collections.emptyList())
                .requestVariables(List.of(mandatoryParam, optionalParam))
                .build();

        assertEquals("/search?query=\" + query + \"&page=\" + page + \"", 
                generator.getUrl(path, false));
        assertEquals("/search?query=\" + query + \"", 
                generator.getUrl(path, true));
    }

    @Test
    void testGetFileContentWithOptionalParams() {
        RouteGenerator generator = new RouteGenerator(mock(FlamewingConfiguration.class));

        RouteVariable mandatoryParam = RouteVariable.builder()
                .name("query")
                .varName("query")
                .varType(String.class)
                .required(true)
                .build();

        RouteVariable optionalParam = RouteVariable.builder()
                .name("page")
                .varName("page")
                .varType(Integer.class)
                .required(false)
                .build();

        RoutePath path = RoutePath.builder()
                .name("search")
                .url("/search")
                .method(HttpMethod.GET)
                .pathVariables(Collections.emptyList())
                .requestVariables(List.of(mandatoryParam, optionalParam))
                .build();

        String content = generator.getFileContent("SearchController", "routes", List.of(path));

        // Check for both methods
        assert(content.contains("public static Route search(java.lang.String query)"));
        assert(content.contains("public static Route search(java.lang.String query, java.lang.Integer page)"));
    }

    @Test
    void testGetFileContentWithoutOptionalParams() {
        RouteGenerator generator = new RouteGenerator(mock(FlamewingConfiguration.class));

        RouteVariable mandatoryParam = RouteVariable.builder()
                .name("query")
                .varName("query")
                .varType(String.class)
                .required(true)
                .build();

        RoutePath path = RoutePath.builder()
                .name("search")
                .url("/search")
                .method(HttpMethod.GET)
                .pathVariables(Collections.emptyList())
                .requestVariables(List.of(mandatoryParam))
                .build();

        String content = generator.getFileContent("SearchController", "routes", List.of(path));

        // Check for only one method
        assert(content.contains("public static Route search(java.lang.String query)"));
        // Count occurrences of "public static Route"
        int count = 0;
        int lastIndex = 0;
        while ((lastIndex = content.indexOf("public static Route", lastIndex)) != -1) {
            count++;
            lastIndex += "public static Route".length();
        }
        assertEquals(1, count);
    }

    @Test
    void testCombinePaths() {
        RouteGenerator generator = new RouteGenerator(mock(FlamewingConfiguration.class));

        assertEquals("/api/users", generator.combinePaths("/api", "/users"));
        assertEquals("/api/users", generator.combinePaths("/api", "users"));
        assertEquals("/api/users", generator.combinePaths("api", "/users"));
        assertEquals("/api/users", generator.combinePaths("api", "users"));
        assertEquals("/api/users", generator.combinePaths("/api/", "/users"));
        assertEquals("/users", generator.combinePaths("", "/users"));
        assertEquals("/api", generator.combinePaths("/api", ""));
    }
}


