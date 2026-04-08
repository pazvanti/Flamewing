package tech.petrepopescu.flamewing.format;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultTest {

    @Test
    void testHeaderPreservation() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("https://example.com"));
        headers.add("X-Custom-Header", "value1");
        headers.add("X-Custom-Header", "value2");

        Result result = new Result(headers, HttpStatus.FOUND);

        assertEquals("https://example.com", result.getHeaders().getLocation().toString());
        List<String> customHeaders = result.getHeaders().get("X-Custom-Header");
        assertEquals(2, customHeaders.size());
        assertTrue(customHeaders.contains("value1"));
        assertTrue(customHeaders.contains("value2"));
    }

    @Test
    void testHeaderPreservationWithBody() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Test", "test-value");
        Format body = new JsonFormat("body");

        Result result = new Result(body, headers, HttpStatus.OK);

        assertEquals("test-value", result.getHeaders().getFirst("X-Test"));
        assertEquals(body, result.getBody());
    }
}
