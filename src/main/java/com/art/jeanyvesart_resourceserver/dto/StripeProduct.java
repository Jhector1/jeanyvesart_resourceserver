package com.art.jeanyvesart_resourceserver.dto;

import com.art.jeanyvesart_resourceserver.model.Artwork;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StripeProduct implements Serializable {

    private int inventory;
    private Artwork myProduct;
    private int quantity;

}