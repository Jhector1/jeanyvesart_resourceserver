package com.art.jeanyvesart_resourceserver.controller;

import com.art.jeanyvesart_resourceserver.model.ProductImagePreviewer;
import com.art.jeanyvesart_resourceserver.repository.ProductImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(path = "/preview/product-previewer/", produces = "application/json")
public class ProductImagePreviewerController {
    private final ProductImageRepository imageRepository;

    public ProductImagePreviewerController(ProductImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<ProductImagePreviewer> getProductPreviewer(@PathVariable Long id){
        Optional<ProductImagePreviewer> previewerOptional = imageRepository.findById(id);
        return previewerOptional.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

}
