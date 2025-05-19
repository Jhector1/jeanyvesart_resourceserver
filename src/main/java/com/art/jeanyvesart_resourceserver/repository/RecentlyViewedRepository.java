package com.art.jeanyvesart_resourceserver.repository;

import com.art.jeanyvesart_resourceserver.model.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RecentlyViewedRepository extends CustomerDataRepository<RecentlyViewedHelper, RecentlyViewed> {
    List<RecentlyViewed> findAllByDateBeforeAndMyCustomer_Id(Date expirationDate, String fullName);


    List<RecentlyViewed> findAllByDateBefore(Date now);
}
