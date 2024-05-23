package com.art.jeanyvesart_resourceserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorColumn(name = "product", discriminatorType = DiscriminatorType.STRING)
@AllArgsConstructor
public  class MyProduct implements Serializable  {
    private static final long serialVersionUID = 123456789L;

//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "my_sequence_generator")
//    @SequenceGenerator(name = "my_sequence_generator", sequenceName = "my_sequence", initialValue = 0, allocationSize = 1)
    @Id
    private long id;
    private String imageUrl;
    private String title;
    private String price;
    @Column(length = 10000)
    private String description;
    private int quantity;
    @OneToMany(cascade = CascadeType.ALL)
    private List<MyReview> myReviews = new ArrayList<>();

    public MyProduct(long id,
                     String imageUrl,
                     String title,
                     String price,
                     String description, int quantity

    ) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.price = price;
        this.description = description;
        this.quantity = quantity;

    }

}