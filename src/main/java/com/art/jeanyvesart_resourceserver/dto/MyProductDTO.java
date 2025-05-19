package com.art.jeanyvesart_resourceserver.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyProductDTO {
    private String title;
    private String price;
    private String description;
    private int quantity;
    private String medium;
    private String size;

    private String[] imageData;
}
