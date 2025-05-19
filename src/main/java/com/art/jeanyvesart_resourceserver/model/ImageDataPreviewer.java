package com.art.jeanyvesart_resourceserver.model;

import com.stripe.model.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("artwork")

@AllArgsConstructor
public class ImageDataPreviewer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(cascade=CascadeType.ALL)
    private MyCustomer customer;
    @ManyToMany(cascade=CascadeType.ALL)
    private List<ProductImagePreviewer> imageData;
}
