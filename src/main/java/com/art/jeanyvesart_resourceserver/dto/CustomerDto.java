package com.art.jeanyvesart_resourceserver.dto;

import com.art.jeanyvesart_resourceserver.model.Address;
import com.art.jeanyvesart_resourceserver.validation.PasswordMatches;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor



public class CustomerDto implements MyCurrentUser, Serializable {
    private String fullName;
    private String id;
    @PasswordMatches(message = "Password Invalid. Password" +
            " must be at least 8 characters and include a lowercase " +
            "letter, uppercase letter, and digit.")
    private String password;
    private String matchingPassword;
   // @ValidEmail(message ="Email is Invalid")
    private String telephone;
    private  Address address;
    private String email;
}
