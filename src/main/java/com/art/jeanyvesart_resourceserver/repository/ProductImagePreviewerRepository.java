package com.art.jeanyvesart_resourceserver.repository;

import com.art.jeanyvesart_resourceserver.model.MyProduct;
import com.art.jeanyvesart_resourceserver.model.ProductImagePreviewer;
import org.springframework.data.repository.CrudRepository;

public interface ProductImagePreviewerRepository extends CrudRepository<ProductImagePreviewer, Long> {
}
