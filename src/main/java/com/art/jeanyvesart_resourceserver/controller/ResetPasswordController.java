package com.art.jeanyvesart_resourceserver.controller;

import com.art.jeanyvesart_resourceserver.dto.CustomerDto;
import com.art.jeanyvesart_resourceserver.model.MyCustomer;
import com.art.jeanyvesart_resourceserver.repository.CustomerRepository;
import com.art.jeanyvesart_resourceserver.validation.PasswordMatches;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@Controller

@RequestMapping(path = "/reset-password", produces = "application/json")

public class ResetPasswordController {
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    public ResetPasswordController(PasswordEncoder passwordEncoder, CustomerRepository customerRepository) {
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public String getResetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset_password";
    }

    @ModelAttribute
    public CustomerDto customerDto() {
        return new CustomerDto();
    }

    @PostMapping
    public String resetPassword(@RequestParam @NonNull String token,
                                @RequestParam("password") @PasswordMatches String newPassword,
                                @RequestParam String confirm_new_password,
                                Model model,
                                @Valid @ModelAttribute("customerDto") CustomerDto customerDto,
                                BindingResult bindingResult) {
        // Validate the token

        Optional<MyCustomer> myCustomerOptional = customerRepository.findByResetToken(token);
        if (myCustomerOptional.isEmpty()) {
            model.addAttribute("error", "Hmm! Something is wrong, please try again later.");
            return "reset_password";
        }

        MyCustomer myCustomer = myCustomerOptional.get();
        long dateRegister = myCustomer.getResetTokenDate().getTime();
        long expiredDate = new Date().getTime() - dateRegister;
        if (expiredDate > 1800000) {
            myCustomer.setResetTokenUsed(true);
        }
        if (bindingResult.hasErrors()) {
            //model.addAttribute("customerDto", new CustomerDto());

            return "reset_password";
        } else if (myCustomer.isResetTokenUsed()) {
            model.addAttribute("error", "Sorry token is expired");
            return "reset_password";
        } else if (!newPassword.equals(confirm_new_password)) {
            model.addAttribute("error", "Password does not match");
            model.addAttribute("validated", "was-validated");

            return "reset_password";
        }
        // Set new password
        myCustomer.setPassword(passwordEncoder.encode(newPassword));

        // Mark token as used
        myCustomer.setResetTokenUsed(true);
        myCustomer.setResetToken(null);

        // Save the user
        customerRepository.save(myCustomer);

        return "redirect:/login";
    }

}
