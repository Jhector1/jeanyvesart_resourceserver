package com.art.jeanyvesart_resourceserver.repository;

import com.art.jeanyvesart_resourceserver.model.MyProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface ProductRepository extends CrudRepository<MyProduct, Long> {
    Optional<MyProduct> findByIdOrderByIdAsc(Long aLong);

    Optional<MyProduct> findByIdOrderByIdDesc(Long aLong);
}
