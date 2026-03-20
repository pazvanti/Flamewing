package tech.petrepopescu.flamewing.views.predefined;

public class View500 extends PredefinedErrorView {
    private static final String ERROR_MESSAGE = "There was an error processing your request.";

    @Override
    String getTitle() {
        return "Internal Server Error";
    }

    @Override
    String getMessage() {
        return ERROR_MESSAGE;
    }
}
