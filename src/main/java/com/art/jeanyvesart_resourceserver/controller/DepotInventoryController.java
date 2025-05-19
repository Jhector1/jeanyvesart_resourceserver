package com.art.jeanyvesart_resourceserver.controller;

import com.art.jeanyvesart_resourceserver.model.Artwork;
import com.art.jeanyvesart_resourceserver.model.DepotInventory;
import com.art.jeanyvesart_resourceserver.model.Inventory;
import com.art.jeanyvesart_resourceserver.model.MyProduct;
import com.art.jeanyvesart_resourceserver.repository.DepotInventoryRepository;
import com.art.jeanyvesart_resourceserver.repository.InventoryRepository;
import com.art.jeanyvesart_resourceserver.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping(path = "/depot/inventory", produces = "application/json")

public class DepotInventoryController {
    private final DepotInventoryRepository depotInventoryRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    public DepotInventoryController(DepotInventoryRepository depotInventoryRepository, InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.depotInventoryRepository = depotInventoryRepository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    @PostMapping(value = "/save", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)

    public DepotInventory saveToInventory(@RequestBody DepotInventory depotInventory) {
        System.out.println("hello");
        System.out.println(depotInventory.getInventories());
        List<Inventory> inventoryList = depotInventory.getInventories();
        AtomicLong count = new AtomicLong(inventoryRepository.count());
        inventoryList.forEach(inventory -> {
            count.addAndGet(1);
            Artwork product =  (Artwork)inventory.getMyProduct();
            product.setId(count.get());
            productRepository.save(product);
            inventory.setId("00000"+count.get());
            inventoryRepository.save(inventory);
        });
        //inventoryRepository.saveAll(depotInventory.getInventories());
        Optional<DepotInventory> depotInventory1 = depotInventoryRepository.findDepotInventoryByCategory(depotInventory.getCategory());
        if(depotInventory1.isPresent()){
            depotInventory1.get().getInventories().addAll(depotInventory.getInventories());
            return depotInventoryRepository.save(depotInventory1.get());
        }
       return depotInventoryRepository.save(depotInventory);


    }
}
