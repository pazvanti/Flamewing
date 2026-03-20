package tech.petrepopescu.flamewing.format;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class Result extends ResponseEntity<Format> {
    public Result(Format format, HttpStatus status) {
        super(format, HttpStatusCode.valueOf(status.value()));
    }

    public Result(HttpHeaders headers, HttpStatus status) {
        super(headers, HttpStatusCode.valueOf(status.value()));
    }

    public Result(Format format, HttpHeaders headers, HttpStatus status) {
        super(format, headers, HttpStatusCode.valueOf(status.value()));
    }
}
