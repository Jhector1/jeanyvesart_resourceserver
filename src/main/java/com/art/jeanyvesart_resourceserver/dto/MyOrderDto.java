package com.art.jeanyvesart_resourceserver.dto;

import com.art.jeanyvesart_resourceserver.model.MyProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyOrderDto {
    private List<MyProduct> products = new ArrayList<>();
    private String  customerId;
}
