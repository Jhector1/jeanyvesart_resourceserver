package com.art.jeanyvesart_resourceserver.repository;

import com.art.jeanyvesart_resourceserver.model.CustomerFavorite;
import com.art.jeanyvesart_resourceserver.model.CustomerFavoriteHelper;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerFavoriteRepository extends CustomerDataRepository<CustomerFavoriteHelper, CustomerFavorite> {
}
