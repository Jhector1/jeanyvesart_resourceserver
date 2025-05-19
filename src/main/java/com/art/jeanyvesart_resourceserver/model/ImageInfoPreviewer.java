package com.art.jeanyvesart_resourceserver.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;




        import com.fasterxml.jackson.annotation.JsonSubTypes;
        import com.fasterxml.jackson.annotation.JsonTypeInfo;
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

public class ImageInfoPreviewer implements Serializable {
    private static final long serialVersionUID = 123456789L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String imageUrl;
    private String title;
    private String price;
    @Column(length = 10000)
    private String description;
    private String medium;
    private String size;
    private String category;
    private int quantity;

}