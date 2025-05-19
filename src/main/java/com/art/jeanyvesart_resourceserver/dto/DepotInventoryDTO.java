package com.art.jeanyvesart_resourceserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepotInventoryDTO {
    private String category;
    private List<InventoryDTO> inventoryDTOList;



}
