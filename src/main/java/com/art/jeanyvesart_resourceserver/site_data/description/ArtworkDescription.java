package com.art.jeanyvesart_resourceserver.site_data.description;

import java.util.List;

public class ArtworkDescription {
    public List<String> getDescriptions(Description description) {
        return description.getDescriptionList();
    }
}
