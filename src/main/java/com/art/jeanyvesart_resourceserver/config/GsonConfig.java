package com.art.jeanyvesart_resourceserver.config;

import com.art.jeanyvesart_resourceserver.adapter.CustomerDataTypeAdapter;
import com.art.jeanyvesart_resourceserver.adapter.MyOrderAdapter;
import com.art.jeanyvesart_resourceserver.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GsonConfig {
    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(CustomerFavorite.class, new CustomerDataTypeAdapter<CustomerFavorite, CustomerFavoriteHelper>())
                .registerTypeAdapter(CustomerCart.class, new CustomerDataTypeAdapter<CustomerCart, CustomerCartHelper>())
                .registerTypeAdapter(MyOrder.class, new MyOrderAdapter())
                //.registerTypeAdapter(Person.class, new PersonTypeAdapter())
                .create();
    }

}
