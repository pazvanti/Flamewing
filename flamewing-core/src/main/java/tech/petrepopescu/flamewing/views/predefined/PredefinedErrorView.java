package tech.petrepopescu.flamewing.views.predefined;

public abstract class PredefinedErrorView extends PredefinedView {
    @Override
    String getBorderColor() {
        return "#842029";
    }

    @Override
    String getBackgroundColor() {
        return "#2c0b0e;";
    }

    @Override
    String getTextColor() {
        return "#ea868f";
    }
}
