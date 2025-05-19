package com.art.jeanyvesart_resourceserver.repository;

import com.art.jeanyvesart_resourceserver.model.ImageData;
import com.art.jeanyvesart_resourceserver.model.ImageInfoPreviewer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageInfoRepository extends CrudRepository<ImageInfoPreviewer, Long> {
}
