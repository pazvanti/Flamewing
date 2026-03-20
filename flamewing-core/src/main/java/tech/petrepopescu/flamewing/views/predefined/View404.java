package tech.petrepopescu.flamewing.views.predefined;

public class View404 extends PredefinedView {
    private static final String ERROR_MESSAGE = "The page you are requesting does not exist.";

    @Override
    String getTitle() {
        return "Page not found";
    }

    @Override
    String getMessage() {
        return ERROR_MESSAGE;
    }
}
