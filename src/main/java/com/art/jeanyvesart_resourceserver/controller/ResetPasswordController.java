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
import java.util.regex.Pattern;

@Controller
@RequestMapping(path = "/reset-password", produces = "application/json")
public class ResetPasswordController {
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    // Added: server-side strength rule to match the client one
    private static final Pattern STRONG_PWD =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,72}$");

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

        // 1) Cheap early exits before DB hit
        if (bindingResult.hasErrors()) {
            return "reset_password";
        }
        if (newPassword == null || !newPassword.equals(confirm_new_password)) {
            model.addAttribute("error", "Password does not match");
            model.addAttribute("validated", "was-validated");
            return "reset_password";
        }
        if (!STRONG_PWD.matcher(newPassword).matches()) {
            model.addAttribute("error", "Use 8–72 chars with upper, lower, number, and symbol.");
            model.addAttribute("validated", "was-validated");
            return "reset_password";
        }

        // 2) Token validation
        Optional<MyCustomer> myCustomerOptional = customerRepository.findByResetToken(token);
        if (myCustomerOptional.isEmpty()) {
            model.addAttribute("error", "Hmm! Something is wrong, please try again later.");
            return "reset_password";
        }

        MyCustomer myCustomer = myCustomerOptional.get();

        // 3) Expiry check with null guard + persist token invalidation
        Date tokenDate = myCustomer.getResetTokenDate();
        long now = System.currentTimeMillis();
        boolean expired = (tokenDate == null) || (now - tokenDate.getTime() > 30 * 60 * 1000L); // 30 min

        if (expired) {
            myCustomer.setResetTokenUsed(true);
            myCustomer.setResetToken(null);
            customerRepository.save(myCustomer); // persist invalidation
            model.addAttribute("error", "Sorry token is expired");
            return "reset_password";
        }

        if (myCustomer.isResetTokenUsed()) {
            model.addAttribute("error", "Sorry token is expired");
            return "reset_password";
        }

        // 4) Save new password + retire token
        myCustomer.setPassword(passwordEncoder.encode(newPassword));
        myCustomer.setResetTokenUsed(true);
        myCustomer.setResetToken(null);
        customerRepository.save(myCustomer);

        return "redirect:/login";
    }
}
