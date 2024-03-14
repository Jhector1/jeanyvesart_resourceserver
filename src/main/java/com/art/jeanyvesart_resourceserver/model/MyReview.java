package com.art.jeanyvesart_resourceserver.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class MyReview implements Serializable {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    @JoinColumn(name = "my_customer_id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private MyCustomer myCustomer;
    private String headline;
    @Column(length = 10000)

    private String reviewText;
    @ManyToOne
    private MyProduct product;
    private int rating;
    private Date date = new Date();
    @Lob
    @Column(length = 200000)
    private byte[] imageData;
    private String imageName;
}
