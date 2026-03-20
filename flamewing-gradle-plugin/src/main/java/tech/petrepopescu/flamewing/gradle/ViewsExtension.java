package tech.petrepopescu.flamewing.gradle;

import org.gradle.api.provider.Property;

public interface ViewsExtension {
    Property<String> getPath();
    Property<String> getExtension();
}
