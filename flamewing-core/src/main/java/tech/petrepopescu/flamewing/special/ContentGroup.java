package tech.petrepopescu.flamewing.special;

import java.util.HashMap;
import java.util.Map;

public class ContentGroup {
    private Map<String, Boolean> insertedSections = new HashMap<>();

    public void markSectionAsInserted(String sectionName) {
        insertedSections.put(sectionName, true);
    }

    public boolean isSectionInserted(String sectionName) {
        return Boolean.TRUE.equals(insertedSections.get(sectionName));
    }
}
