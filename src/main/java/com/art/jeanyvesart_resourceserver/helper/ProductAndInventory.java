package com.art.jeanyvesart_resourceserver.helper;

import com.art.jeanyvesart_resourceserver.model.MyProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAndInventory implements Serializable {

    private int inventory;
    private MyProduct myProduct;
    private int quantity;

}
