package com.art.jeanyvesart_resourceserver.controller;

import com.art.jeanyvesart_resourceserver.model.DepotInventory;
import com.art.jeanyvesart_resourceserver.repository.DepotInventoryRepository;
import com.art.jeanyvesart_resourceserver.repository.InventoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/depot/inventory", produces = "application/json")

public class DepotInventoryController {
    private final DepotInventoryRepository depotInventoryRepository;
    private final InventoryRepository inventoryRepository;
    public DepotInventoryController(DepotInventoryRepository depotInventoryRepository, InventoryRepository inventoryRepository) {
        this.depotInventoryRepository = depotInventoryRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @PostMapping(value = "/save", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public DepotInventory saveToInventory(@RequestBody DepotInventory depotInventory) {
        inventoryRepository.saveAll(depotInventory.getInventories());
       return depotInventoryRepository.save(depotInventory);


    }
}
