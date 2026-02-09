package tech.petrepopescu.flamewing.format;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

public class FlamewingResponse {
    private FlamewingResponse() {
        // utility class
    }

    public static Result ok(Object object) {
        return withHttpStatus(object, HttpStatus.OK);
    }

    public static Result ok() {
        return withHttpStatus(new JsonFormat(), HttpStatus.OK);
    }

    public static Result ok(Format format) {
        return withHttpStatus(format, HttpStatus.OK);
    }

    public static Result notFound() {
        return withHttpStatus(new JsonFormat(), HttpStatus.NOT_FOUND);
    }

    public static Result notFound(Object object) {
        return withHttpStatus(object, HttpStatus.NOT_FOUND);
    }

    public static Result notFound(Format format) {
        return withHttpStatus(format, HttpStatus.NOT_FOUND);
    }

    public static Result redirect(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(UriComponentsBuilder.fromUriString(url).build().toUri());
        return new Result(headers, HttpStatus.FOUND);
    }

    public static Result withHttpStatus(Object object, HttpStatus status) {
        return withHttpStatus(new JsonFormat(object), status);
    }

    public static Result withHttpStatus(Format format, HttpStatus status) {
        return new Result(format, status);
    }

    public static Result withHttpStatus(Object object, HttpHeaders headers, HttpStatus status) {
        return withHttpStatus(new JsonFormat(object), headers, status);
    }

    public static Result withHttpStatus(Format format, HttpHeaders headers,HttpStatus status) {
        return new Result(format, headers, status);
    }
}
