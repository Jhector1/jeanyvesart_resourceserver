package com.art.jeanyvesart_resourceserver.repository;

import com.art.jeanyvesart_resourceserver.model.CustomerCartHelper;
import com.art.jeanyvesart_resourceserver.model.RecentlyViewedHelper;
import org.springframework.stereotype.Repository;

@Repository
public interface RecentlyViewedHelperRepository extends CustomerDataHelperRepository<RecentlyViewedHelper> {
}
