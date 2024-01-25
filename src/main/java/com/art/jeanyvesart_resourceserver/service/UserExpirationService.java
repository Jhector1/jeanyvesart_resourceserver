package com.art.jeanyvesart_resourceserver.service;

import com.art.jeanyvesart_resourceserver.model.MyCustomer;
import com.art.jeanyvesart_resourceserver.repository.CustomerRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserExpirationService {

    private final CustomerRepository customerRepository; // Your UserRepository interface

    public UserExpirationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Scheduled(cron = "0 0 0 1,16 * ?") // Runs on the 1st and 16th day of each month
    //    0 0 0: Specifies midnight (00:00:00) as the time of day.
    //    1,16: Specifies the 1st and 16th day of the month.
    //    *: Wildcard for all months.
    //    ?: Used in place of the day-of-week field (not relevant in this context).
    //    So, the task will be scheduled to run at midnight on the 1st and 16th day of each month, effectively every 15 days.
  // @Scheduled(cron = "0 */2 * * * ?") // Runs every 2 minutes

   public void removeExpiredUsers() {
        Date now = new Date();
        List<MyCustomer> expiredUsers = customerRepository.findByExpirationDateBeforeAndFullName(now, "anonymous");
       List<MyCustomer> customerList = new ArrayList<>(expiredUsers);

        if (!expiredUsers.isEmpty()) {
            customerRepository.deleteAll(customerList);
            // You can also perform additional actions here, like sending notifications
        }
    }
}
