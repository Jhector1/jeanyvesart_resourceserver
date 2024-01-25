package com.art.jeanyvesart_resourceserver.service;

import com.art.jeanyvesart_resourceserver.exceptionHandler.UserAlreadyExistException;
import com.art.jeanyvesart_resourceserver.dto.CustomerDto;
import com.art.jeanyvesart_resourceserver.model.MyCustomer;
import com.art.jeanyvesart_resourceserver.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerService implements IUserService<ResponseEntity<?>> {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<?> registerNewCustomerAccount(CustomerDto customerDto) throws UserAlreadyExistException {
        if (emailExists(customerDto.getEmail())) {
            throw new UserAlreadyExistException("An account is already existed with that email address: " + customerDto.getEmail());
        }
        MyCustomer myCustomer = new MyCustomer();
        myCustomer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        myCustomer.setEmail(customerDto.getEmail());
        myCustomer.setFullName(customerDto.getFullName());
        customerRepository.save(myCustomer);
        return new ResponseEntity<>(HttpStatus.CREATED);

        // the rest of the registration operation
    }
    private boolean emailExists(String email) {
        return customerRepository.findByEmail(email).isPresent();
    }
}