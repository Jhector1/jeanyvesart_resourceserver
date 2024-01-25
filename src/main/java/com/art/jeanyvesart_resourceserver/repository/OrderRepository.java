package com.art.jeanyvesart_resourceserver.repository;

import com.art.jeanyvesart_resourceserver.model.MyOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<MyOrder, Long> {
Optional<MyOrder> findByMyCustomer_Id(String customer_id);

    Iterable<MyOrder> findAllByMyCustomer_Id(String customerId);
}
