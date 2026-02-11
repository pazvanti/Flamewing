package tech.petrepopescu.flamewing.views.predefined;

public class ViewNotFoundView extends PredefinedErrorView {
    @Override
    String getTitle() {
        return "The view could not be found";
    }

    @Override
    String getMessage() {
        return "The view you are requesting was not found. Please make sure you have everything configured properly.";
    }
}
