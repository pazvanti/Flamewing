package tech.petrepopescu.flamewing.format;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class Result extends ResponseEntity<Format> {
    public Result(Format format, HttpStatus status) {
        super(format, HttpStatusCode.valueOf(status.value()));
    }

    public Result(HttpHeaders headers, HttpStatus status) {
        super(toMultiValueMap(headers), HttpStatusCode.valueOf(status.value()));
    }

    public Result(Format format, HttpHeaders headers, HttpStatus status) {
        super(format, toMultiValueMap(headers), HttpStatusCode.valueOf(status.value()));
    }

    private static MultiValueMap<String, String> toMultiValueMap(HttpHeaders headers) {
        if (headers == null) {
            return null;
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        headers.forEach(map::addAll);
        return map;
    }
}
