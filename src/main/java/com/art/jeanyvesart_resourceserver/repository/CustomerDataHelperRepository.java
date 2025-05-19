package com.art.jeanyvesart_resourceserver.repository;

import com.art.jeanyvesart_resourceserver.model.CustomerDataHelper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface CustomerDataHelperRepository<T extends CustomerDataHelper> extends CrudRepository<T, Long> {
    void deleteAllByMyProductId(long productId);

    void deleteByMyProductId(Long aLong);


}
