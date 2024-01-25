package com.art.jeanyvesart_resourceserver.controller;

import com.art.jeanyvesart_resourceserver.model.CustomerCart;
import com.art.jeanyvesart_resourceserver.model.CustomerCartHelper;
import com.art.jeanyvesart_resourceserver.repository.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/cart/artworks", produces = "application/json")

public class CartRestController  extends CustomerDataController<CustomerCartHelper, CustomerCartRepository, CustomerCart> {
    public CartRestController(CustomerCartRepository repository,
                              ProductRepository productRepository,
                              CustomerRepository customerRepository,
                              InventoryRepository inventoryRepository, CustomerCartHelperRepository customerCartHelperRepository) {
        super(repository, productRepository, customerRepository, inventoryRepository, customerCartHelperRepository);
    }

}
//
