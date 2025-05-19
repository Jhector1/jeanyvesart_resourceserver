package com.art.jeanyvesart_resourceserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("artwork")

@AllArgsConstructor
public class ProductImagePreviewer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String type;
    @Lob
    @Column(length = 200000)
    private String base64Image;
    @OneToOne(cascade = CascadeType.ALL)
    private ImageInfoPreviewer infoPreviewer;

}
