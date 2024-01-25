package com.art.jeanyvesart_resourceserver.security.service;

import com.art.jeanyvesart_resourceserver.repository.CustomerRepository;
import com.art.jeanyvesart_resourceserver.model.MyCustomer;
import com.art.jeanyvesart_resourceserver.security.user.SecurityUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
//@Qualifier("jdbcUserDetailsService")
public class JdbcUserDetailsService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final List<SecurityUserDetails> users = new ArrayList<>();


    public JdbcUserDetailsService(CustomerRepository customerRepository) {

        this.customerRepository = customerRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        SecurityUserDetails userDetails;
        Optional<MyCustomer> optionalMyCustomer = customerRepository.findByEmail(username);
        if (optionalMyCustomer.isPresent()) {
            userDetails = new SecurityUserDetails(optionalMyCustomer.get());
        } else {
            System.out.println("User not found");
            throw new UsernameNotFoundException("User not found");

        }
        System.out.println(userDetails);
        return userDetails;
    }
//    return users.stream()
//      .filter(
//         u -> u.getUsername().equals(username)
//      )
//      .findFirst()
//      .orElseThrow(
//        () -> new UsernameNotFoundException("User not found")
//      );
//   }
}