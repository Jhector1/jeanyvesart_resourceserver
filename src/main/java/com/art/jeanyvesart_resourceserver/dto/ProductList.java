package com.art.jeanyvesart_resourceserver.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductList {
    private List<StripeProduct> cartProductList = new ArrayList<>();
    private String customerId;

}
