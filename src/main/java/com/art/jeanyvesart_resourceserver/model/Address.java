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
    //@NotBlank(message = "Delivery street is required")
    private String street;
    //@NotBlank(message = "Delivery city is required")

    private String city;
    //@NotBlank(message = "Delivery state is required")
    private String state;
    //@NotBlank(message = "Zip code is required")
    private String zip;
    private String apartment;

    public void updateAddress(Address address) {
        if (address.getCountry() != null) {
            this.setCountry(address.getCountry());
        }
        if (address.getStreet() != null) {
            this.setStreet(address.getStreet());
        }
        if (address.getCity() != null) {
            this.setCity(address.getZip());
        }
        if (address.getState() != null) {
            this.setState(address.getState());
        }
        if (address.getZip() != null) {
            this.setZip(address.getZip());
        }
    }
}