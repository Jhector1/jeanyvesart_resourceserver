package com.art.jeanyvesart_resourceserver.repository;

import com.art.jeanyvesart_resourceserver.model.DepotInventory;
import com.art.jeanyvesart_resourceserver.model.MyCustomer;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepotInventoryRepository extends CrudRepository<DepotInventory, Long> {

    List<DepotInventory> findByCategory(String category);
    List<DepotInventory> findByCategory(Sort sort, String category);
    List<DepotInventory> findByCategoryOrderByIdDesc(String category);
    List<DepotInventory> findByCategoryOrderByIdAsc(String category);



    Optional<DepotInventory> findDepotInventoryByCategory(String category);

    Iterable<DepotInventory> findAll(Sort id);
}
