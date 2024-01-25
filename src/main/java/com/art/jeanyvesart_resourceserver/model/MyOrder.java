package com.art.jeanyvesart_resourceserver.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class MyOrder {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Expose
    private long id;

    private Date date = new Date();
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE})
//    @JoinTable(
//            name = "my_order_my_customer ",
//            joinColumns = @JoinColumn(name = "my_order_id"),
//            inverseJoinColumns = @JoinColumn(name = "my_customer_id")
//    )
   // @JoinColumn(name = "my_customer_id")

    private MyCustomer myCustomer;
    @ManyToMany
    private List<MyProduct> myProducts = new ArrayList<>();

    public MyOrder(List<MyProduct> myProducts){
        this.myProducts = myProducts;
        this.myCustomer = new MyCustomer();
    }

    public MyOrder(MyCustomer myCustomer, List<MyProduct> myProducts) {
        this.myCustomer = myCustomer; this.myProducts = myProducts;
    }

}
