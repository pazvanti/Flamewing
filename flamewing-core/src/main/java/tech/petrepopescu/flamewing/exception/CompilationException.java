package tech.petrepopescu.flamewing.exception;

public class CompilationException extends RuntimeException {
    public CompilationException(String errorMessage) {
        super(errorMessage);
    }
}
