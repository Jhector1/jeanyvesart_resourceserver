package com.art.jeanyvesart_resourceserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProduct {
    private long productId;
   private String customerId;
    private int quantity;
}
