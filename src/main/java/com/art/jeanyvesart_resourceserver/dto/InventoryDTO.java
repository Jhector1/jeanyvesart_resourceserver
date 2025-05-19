package com.art.jeanyvesart_resourceserver.dto;

import com.art.jeanyvesart_resourceserver.model.MyProduct;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {


    private MyProductDTO myProductDTO;
    private String category;
    private int quantity;
    List<String> metadata = new ArrayList<>();
}
