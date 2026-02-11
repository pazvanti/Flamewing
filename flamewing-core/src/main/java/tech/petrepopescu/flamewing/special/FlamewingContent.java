package tech.petrepopescu.flamewing.special;

public abstract class FlamewingContent {
    protected final StringBuilder contentBuilder = new StringBuilder();
    public abstract String render();

    @Override
    public String toString() {
        return render();
    }
}
