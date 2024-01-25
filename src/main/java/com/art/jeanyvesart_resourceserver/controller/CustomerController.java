package com.art.jeanyvesart_resourceserver.controller;

import com.art.jeanyvesart_resourceserver.dto.MyCurrentUserImpl;
import com.art.jeanyvesart_resourceserver.dto.ReviewDto;
import com.art.jeanyvesart_resourceserver.model.Inventory;
import com.art.jeanyvesart_resourceserver.model.MyCustomer;
import com.art.jeanyvesart_resourceserver.model.MyProduct;
import com.art.jeanyvesart_resourceserver.model.MyReview;
import com.art.jeanyvesart_resourceserver.repository.CustomerRepository;
import com.art.jeanyvesart_resourceserver.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping(path = "/customer", produces = "application/json")

public class CustomerController {
    private final CustomerRepository customerRepository;


    protected CustomerController(CustomerRepository customerRepository, UserDetailsManager userDetailsManager, PasswordEncoder encoder, CustomerService customerService, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;

    }


    @DeleteMapping("/{userId}")
    @Transactional
    //  @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllCartItem(@PathVariable("userId") String id) {
        Optional<MyCustomer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            customerRepository.deleteById(id);
        }
    }

    @GetMapping("/account/{email}")
    public ResponseEntity<MyCustomer> getUserDataByUserId(@PathVariable("email") String email) {
        Optional<MyCustomer> customerOptional = customerRepository.findByEmail(email);
        //log.info("user dat,{}",customerOptional.get());
        if (customerOptional.isPresent()) {
            log.info("user: , {}", customerOptional.get().getUsername());

            return new ResponseEntity<>(customerOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

    }
    @GetMapping("/token/{token}")
    public ResponseEntity<MyCustomer> getUserByResetToken(@PathVariable("token") String token) {
        Optional<MyCustomer> customerOptional = customerRepository.findByResetToken(token);
        //log.info("user dat,{}",customerOptional.get());
        if (customerOptional.isPresent()) {
            log.info("user: , {}", customerOptional.get().getUsername());

            return new ResponseEntity<>(customerOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

    }
    @PatchMapping(path = "/update/token/{token}", consumes = "application/json")
    public ResponseEntity<?> patchCustomer(@PathVariable String token,
                                                    @RequestBody String patch) {

        Optional<MyCustomer> myCustomerOptional = customerRepository.findByResetToken(token);

        if (myCustomerOptional.isPresent()) {


            MyCustomer myCustomer = myCustomerOptional.get();
            long dateRegister = myCustomer.getResetTokenDate().getTime();
            long expiredDate = new Date().getTime() - dateRegister;
            if (expiredDate > 1800000) {
                myCustomer.setResetTokenUsed(true);
            }
         if (myCustomer.isResetTokenUsed()) {

             return new ResponseEntity<>("Sorry token is expired", HttpStatus.UNAUTHORIZED);
            }
            myCustomer.setPassword(patch);

            // Mark token as used
            myCustomer.setResetTokenUsed(true);
            myCustomer.setResetToken(null);


           // BeanUtils.copyProperties(patch, myCustomer, );
            customerRepository.save(myCustomer);
            return ResponseEntity.ok(myCustomer);

        }
        return new ResponseEntity<>("An error occurred, please check the email you provided", HttpStatus.NOT_FOUND);


    }
    @PostMapping(value = "/account/save", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public MyCustomer saveCustomer(@RequestBody MyCustomer myCustomer) {
        // System.out.println(reviewDto);
        try {
            Optional<MyCustomer> optionalMyProduct = customerRepository.findByEmail(myCustomer.getUsername());
            if (optionalMyProduct.isPresent()) {


                return null;

            }
            return customerRepository.save(myCustomer);

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    // rest of the implementation

}
//
