package com.art.jeanyvesart_resourceserver.repository;

import com.art.jeanyvesart_resourceserver.model.CustomerCart;
import com.art.jeanyvesart_resourceserver.model.CustomerCartHelper;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerCartRepository extends CustomerDataRepository<CustomerCartHelper, CustomerCart> {

}
