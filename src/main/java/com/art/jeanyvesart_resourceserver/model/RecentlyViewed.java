package com.art.jeanyvesart_resourceserver.model;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
//@DiscriminatorValue("customerCart")
//@Data
public class RecentlyViewed extends CustomerData<RecentlyViewedHelper> {
  //  @ElementCollection
  //  private List<Integer> quantities;
    public RecentlyViewed(MyCustomer myCustomer, List<RecentlyViewedHelper> customerDataHelpers){
       super(myCustomer, customerDataHelpers);
      // this.quantities = quantities;
    }

}


