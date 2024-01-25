package com.art.jeanyvesart_resourceserver.repository;

import com.art.jeanyvesart_resourceserver.model.CustomerData;
import com.art.jeanyvesart_resourceserver.model.CustomerDataHelper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface CustomerDataRepository< H extends CustomerDataHelper,T extends CustomerData<H>> extends CrudRepository<T, Long> {
    Optional<T> findByMyCustomer_IdAndId(String customer_id, long id);

    List<T> findAllByMyCustomer_Id(String aLong);

    Optional<T> findByMyCustomer_Id(String aLong);
    void deleteAllByMyCustomer_Id(String customer_id);
    void deleteByMyCustomer_IdAndId(String customer_id, long id);

}
