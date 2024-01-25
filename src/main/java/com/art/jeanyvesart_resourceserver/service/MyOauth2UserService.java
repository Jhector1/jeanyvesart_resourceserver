package com.art.jeanyvesart_resourceserver.service;

import com.art.jeanyvesart_resourceserver.helper.Helper;
import com.art.jeanyvesart_resourceserver.model.MyCustomer;
import com.art.jeanyvesart_resourceserver.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@Slf4j
public class MyOauth2UserService {
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    public MyOauth2UserService(PasswordEncoder passwordEncoder, CustomerRepository customerRepository) {
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
    }



    public void processOAuth2User(OAuth2User oAuth2User) {

//log.info("user data:, {}", oAuth2User.getAttributes());

        // Check if the user already exists in the database
        Optional<MyCustomer> existingUser = customerRepository.findByEmail(Helper.checkEmail(oAuth2User));

        if (existingUser.isEmpty()) {
            // User doesn't exist, create a new account
            MyCustomer newUser = createUserFromOAuth2User(oAuth2User);
            customerRepository.save(newUser);
        }
    }

    private MyCustomer createUserFromOAuth2User(OAuth2User oAuth2User) {
        // Customize this method to create a User entity from OAuth2 user information
        MyCustomer newUser = new MyCustomer();
        newUser.setEmail(Helper.checkEmail(oAuth2User));
        newUser.setPassword(passwordEncoder.encode("NewPassword2@"));
        newUser.setFullName(oAuth2User.getAttribute("name"));
        // Set other user details as needed
        return newUser;
    }
}
