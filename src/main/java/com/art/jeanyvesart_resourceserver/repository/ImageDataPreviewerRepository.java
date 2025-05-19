package com.art.jeanyvesart_resourceserver.repository;

import com.art.jeanyvesart_resourceserver.model.ImageDataPreviewer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageDataPreviewerRepository extends CrudRepository<ImageDataPreviewer, Long> {


    Optional<ImageDataPreviewer> findImageDataPreviewerByCustomerId(String customer_id);
    void deleteAllByCustomerId(String customer_id);
}
