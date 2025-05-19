package com.art.jeanyvesart_resourceserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;




        import jakarta.persistence.DiscriminatorValue;
        import jakarta.persistence.Entity;
        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import java.util.ArrayList;
        import java.util.List;

@Entity
@Data
@NoArgsConstructor
@DiscriminatorValue("artwork")

@AllArgsConstructor
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String type;
    @Lob
    @Column(length = 200000)
    private String base64Image;

}
