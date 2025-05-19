package com.art.jeanyvesart_resourceserver.controller;

import com.art.jeanyvesart_resourceserver.model.*;
import com.art.jeanyvesart_resourceserver.repository.ImageDataPreviewerRepository;
import com.art.jeanyvesart_resourceserver.repository.ImageInfoRepository;
import com.art.jeanyvesart_resourceserver.repository.ProductImagePreviewerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(path = "/preview/products", produces = "application/json")
public class ImageDataPreviewerController {
    private final static String TEST_CUSTOMER_ID = "0001";
    private final ImageDataPreviewerRepository imageDataPreviewerRepository;
    private final ProductImagePreviewerRepository productImagePreviewerRepository;
    private final ImageInfoRepository imageInfoRepository;


    public ImageDataPreviewerController(ImageDataPreviewerRepository previewerRepository, ProductImagePreviewerRepository imagePreviewerRepository, ProductImagePreviewerRepository productImagePreviewerRepository, ImageInfoRepository imageInfoRepository) {
        this.imageDataPreviewerRepository = previewerRepository;
        this.productImagePreviewerRepository = productImagePreviewerRepository;
        this.imageInfoRepository = imageInfoRepository;
    }

    @PostMapping()
    public ResponseEntity<List<ProductImagePreviewer>> previewData(@RequestBody ImageDataPreviewer imageData) {

        try {
            // log.info(String.valueOf(imageData));
            Optional<ImageDataPreviewer> previewerOptional = imageDataPreviewerRepository.findImageDataPreviewerByCustomerId(TEST_CUSTOMER_ID);
            if (previewerOptional.isPresent()) {
                ImageDataPreviewer dataPreviewer = previewerOptional.get();
                dataPreviewer.getImageData().addAll(imageData.getImageData());
                imageDataPreviewerRepository.save(dataPreviewer);
                return ResponseEntity.ok(previewerOptional.get().getImageData());
            } else {
                MyCustomer myCustomer = new MyCustomer();
                myCustomer.setId(TEST_CUSTOMER_ID);
                imageData.setCustomer(myCustomer);

                imageDataPreviewerRepository.save(imageData);

                return ResponseEntity.ok(imageData.getImageData());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Transactional
    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<?> patchCustomer(@PathVariable Long id,
                                           @RequestBody ImageInfoPreviewer patch) {

        Optional<ImageDataPreviewer> imageDataPreviewer = imageDataPreviewerRepository.findImageDataPreviewerByCustomerId(TEST_CUSTOMER_ID);


        if (imageDataPreviewer.isPresent()) {


            List<ProductImagePreviewer> productPreviewerList = imageDataPreviewer.get().getImageData();
            ProductImagePreviewer previewer = productPreviewerList.stream().filter(productImagePreviewer -> productImagePreviewer.getId() == id).findFirst().get();

            ImageInfoPreviewer infoPreviewer = previewer.getInfoPreviewer();
            if(infoPreviewer==null) {
                infoPreviewer = new ImageInfoPreviewer();
            }   if (patch.getPrice() != null) {
                    infoPreviewer.setPrice(patch.getPrice());
                }
                if (patch.getDescription() != null) {
                    infoPreviewer.setDescription(patch.getDescription());
                }
                if (patch.getSize() != null) {
                    infoPreviewer.setSize(patch.getSize());
                }
                if (patch.getMedium() != null) {
                    infoPreviewer.setMedium(patch.getMedium());
                }
            if (patch.getCategory() != null) {
                infoPreviewer.setCategory(patch.getCategory());
                if (patch.getQuantity() >= 0) {
                    infoPreviewer.setQuantity(patch.getQuantity());
                }
            }
                if (patch.getTitle() != null) {
                    infoPreviewer.setTitle(patch.getTitle());
                }

            imageInfoRepository.save(infoPreviewer);
                previewer.setInfoPreviewer(infoPreviewer);



        return ResponseEntity.ok(imageDataPreviewer);

    }
        return new ResponseEntity<>("An error occurred",HttpStatus.INTERNAL_SERVER_ERROR);


}

    @GetMapping(path = "/{id}")
    public ResponseEntity<List<ProductImagePreviewer>> getAllProductPreviewer(@PathVariable String id){
        Optional<ImageDataPreviewer> previewerOptional = imageDataPreviewerRepository.findImageDataPreviewerByCustomerId(id);
       if(previewerOptional.isPresent()){
           return ResponseEntity.ok(previewerOptional.get().getImageData());
       }
        return null;
    }


    @Transactional
    @DeleteMapping(path = "/{id}")

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteProductPreviewer(@PathVariable Long id) {
        Optional<ImageDataPreviewer> previewerOptional = imageDataPreviewerRepository.findImageDataPreviewerByCustomerId(TEST_CUSTOMER_ID);
        if (previewerOptional.isPresent()) {
            log.info("Product remove successfully");
            Optional<ProductImagePreviewer> productImagePreviewer = productImagePreviewerRepository.findById(id);
            if(productImagePreviewer.isPresent())
                previewerOptional.get().getImageData().remove(productImagePreviewer.get());
            productImagePreviewerRepository.deleteById(id);
            return  ResponseEntity.ok(previewerOptional.get());
        }
        log.info("error could not remove product");
        return null;

    }
    @Transactional
    @DeleteMapping(path = "/delete-all/{userId}")

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteAllProductPreviewer(@PathVariable String userId) {
        Optional<ImageDataPreviewer> previewerOptional = imageDataPreviewerRepository.findImageDataPreviewerByCustomerId(userId);
        if (previewerOptional.isPresent()) {

        imageDataPreviewerRepository.deleteAllByCustomerId(userId);
            log.info("All Product remove successfully");

            return  ResponseEntity.ok("All Product remove successfully");
        }
        log.info("error could not remove product");
        return null;

    }
}
