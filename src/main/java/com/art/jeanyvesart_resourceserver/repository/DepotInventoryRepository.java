package com.art.jeanyvesart_resourceserver.repository;

import com.art.jeanyvesart_resourceserver.model.DepotInventory;
import com.art.jeanyvesart_resourceserver.model.MyCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepotInventoryRepository extends CrudRepository<DepotInventory, Long> {

    Iterable<DepotInventory> findByCategory(String category);
}
