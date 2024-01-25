package com.art.jeanyvesart_resourceserver.repository;

import com.art.jeanyvesart_resourceserver.model.MyCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<MyCustomer, String> {
    List<MyCustomer> findByExpirationDateBeforeAndFullName(Date expirationDate,  String fullName);
Optional<MyCustomer>findByResetToken(String token);
    Optional<MyCustomer> findByEmail(String email);
}
