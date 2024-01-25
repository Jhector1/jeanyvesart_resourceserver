package com.art.jeanyvesart_resourceserver.site_data.paintings;


import com.art.jeanyvesart_resourceserver.model.MyProduct;

import java.util.List;
@FunctionalInterface
public interface Serie {
    List<MyProduct> getArtworkList();
}
