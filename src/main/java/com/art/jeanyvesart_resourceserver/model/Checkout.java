package com.art.jeanyvesart_resourceserver.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Checkout implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date placedAt = new Date();
    @OneToOne(cascade = CascadeType.ALL)
  //  @Column(length = 500)
    private Address billingAddress;
    @OneToMany(cascade = CascadeType.ALL)
  //  @Column(length = 1000)

    private List<MyProduct>artworks;
    @OneToOne(cascade = CascadeType.ALL)
   // @Column(length = 500)
    private MyCustomer buyer;
//    @NotEmpty(message = "Card holder name is required")
//    public String ccName;
////    @NotEmpty
//    @CreditCardNumber(message = "Not a valid credit card number")
//    private String ccNumber;
//    @NotBlank(message = "Expiration month is required")
//    private String ccExpirationMonth;
//    @NotBlank(message = "Expiration year is required")
//    private String ccExpirationYear;
//    //@Digits(integer = 3, fraction = 0, message = "Invalid CVV")
//    @NotEmpty
//   // @Size(min = 3, max = 4)
//    private String ccCVV;


}
