package com.art.jeanyvesart_resourceserver.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String country;
    private String fullname;
    //@NotBlank(message = "Delivery street is required")

    private String street;
    //@NotBlank(message = "Delivery city is required")

    private String city;
    //@NotBlank(message = "Delivery state is required")
    private String state;
    //@NotBlank(message = "Zip code is required")

    private String zip;
}