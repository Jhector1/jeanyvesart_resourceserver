package com.art.jeanyvesart_resourceserver.site_data.paintings;


import com.art.jeanyvesart_resourceserver.model.MyProduct;

import java.util.ArrayList;
import java.util.List;

public class Paintings {
    List<MyProduct> allMyProducts = new ArrayList<>();
  public List<MyProduct> getSeries(Serie serie) {

      return serie.getArtworkList();
    }

}
