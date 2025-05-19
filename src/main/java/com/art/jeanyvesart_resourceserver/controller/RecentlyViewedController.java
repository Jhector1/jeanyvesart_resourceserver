package com.art.jeanyvesart_resourceserver.controller;

import com.art.jeanyvesart_resourceserver.model.CustomerCart;
import com.art.jeanyvesart_resourceserver.model.CustomerCartHelper;
import com.art.jeanyvesart_resourceserver.model.RecentlyViewed;
import com.art.jeanyvesart_resourceserver.model.RecentlyViewedHelper;
import com.art.jeanyvesart_resourceserver.repository.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/recently-viewed/artworks", produces = "application/json")

public class RecentlyViewedController extends CustomerDataController<RecentlyViewedHelper, RecentlyViewedRepository, RecentlyViewed> {
    public RecentlyViewedController(RecentlyViewedRepository repository,
                                    ProductRepository productRepository,
                                    CustomerRepository customerRepository,
                                    InventoryRepository inventoryRepository, RecentlyViewedHelperRepository recentlyViewedHelperRepository) {
        super(repository, productRepository, customerRepository, inventoryRepository, recentlyViewedHelperRepository);
    }

}
//
