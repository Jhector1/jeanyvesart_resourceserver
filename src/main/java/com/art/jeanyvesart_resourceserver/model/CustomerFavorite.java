package com.art.jeanyvesart_resourceserver.model;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
//@DiscriminatorValue("customerFavorite")
@NoArgsConstructor
public class CustomerFavorite extends CustomerData<CustomerFavoriteHelper>{

    public CustomerFavorite(MyCustomer myCustomer, List<CustomerFavoriteHelper> customerDataHelpers){
       super(myCustomer, customerDataHelpers);
    }
}
