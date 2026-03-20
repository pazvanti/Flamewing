package tech.petrepopescu.flamewing.gradle;

import org.gradle.api.Action;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Nested;

public interface FlamewingExtension {
    Property<String> getControllersPackage();

    @Nested
    ViewsExtension getViews();

    default void views(Action<? super ViewsExtension> action) {
        action.execute(getViews());
    }
}
