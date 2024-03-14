package com.art.jeanyvesart_resourceserver.controller;

import com.art.jeanyvesart_resourceserver.dto.CustomerDto;
import com.art.jeanyvesart_resourceserver.model.Address;
import com.art.jeanyvesart_resourceserver.model.MyCustomer;
import com.art.jeanyvesart_resourceserver.repository.CustomerRepository;
import com.art.jeanyvesart_resourceserver.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping(path = "/customer", produces = "application/json")

public class CustomerController {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;


    protected CustomerController(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;

        this.passwordEncoder = passwordEncoder;
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
    @PatchMapping(path = "/account/update/{id}", consumes = "application/json")
    public ResponseEntity<?> patchCustomer(@PathVariable String id,
                                                    @RequestBody CustomerDto patch) {

        Optional<MyCustomer> myCustomerOptional = customerRepository.findById(id);

        log.info("cusyomer, {}", patch);

        if (myCustomerOptional.isPresent()) {


            MyCustomer myCustomer = myCustomerOptional.get();
            log.info("cusyomer, {}", myCustomer);
         if (patch.getFullName()!=null) {
             myCustomer.setFullName(patch.getFullName());

            }
            if (patch.getTelephone()!=null) {
                myCustomer.setTelephone(patch.getTelephone());

            }  if (patch.getEmail()!=null) {
                myCustomer.setEmail(patch.getEmail());

            }  if (patch.getPassword()!=null) {
                myCustomer.setPassword(passwordEncoder.encode(patch.getPassword()));

            }  if (patch.getAddress()!=null) {
                boolean flag = true;

                Set<Address> set = myCustomer.getAddressList();
                for (Address address : set) {
                    if (address.getZip().equals(patch.getAddress().getZip()) && address.getStreet().equals(patch.getAddress().getStreet())) {
                        address.updateAddress(patch.getAddress());
                        flag = false;
                        break;
                    }

                }
                if (flag) {
                    set.add(patch.getAddress());
                }
                myCustomer.setAddressList(set);
            }

            customerRepository.save(myCustomer);
            return ResponseEntity.ok(myCustomer);

        }
        return new ResponseEntity<>("An error occurred", HttpStatus.NOT_FOUND);


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


    @PatchMapping(path = "/update/info/{token}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> patchCustomer(@PathVariable String token,
                                           @RequestBody String patch) {
        Optional<MyCustomer> myCustomerOptional = customerRepository.findByResetToken(token);
        log.info("patch , {}", patch);
        log.info("patch , {}", token);


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
            String jsonString = "{\"message\": \"Data Update Successfully!\"}"; // Your JSON string

            return new ResponseEntity<>(jsonString, HttpStatus.OK);

        }
        String jsonString = "{\"error\": \"An error occurred, unrecognized token!\"}"; // Your JSON string

        return new ResponseEntity<>(jsonString, HttpStatus.NOT_FOUND);


    }


    // rest of the implementation

}
//
