package com.art.jeanyvesart_resourceserver.model;

import com.art.jeanyvesart_resourceserver.security.user.MyUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Component
@AllArgsConstructor
@JsonIgnoreProperties({"customerFavorite", "customerCart", "myOrders", "myReviews", "recentlyViewed"})
public class MyCustomer implements Serializable, MyUser {
    //    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_sequence_customer")
//    @SequenceGenerator(name = "my_sequence_customer", sequenceName = "my_customer", initialValue = 0, allocationSize = 1)
    @Id
    @Setter
    @Getter
    private String id;
    @Expose
    @Setter
    @Getter
    @NotBlank(message = "Full Name is required")

    private String fullName;

    @Expose
    @Setter
    @Getter
    @NotBlank(message = "Email is required")

    private String email;
    @Expose
    @Setter
    @Getter
    private String telephone;
    @Expose
    @Setter
    @Getter
    private String authority;
    @Expose
    @Setter
    @Getter
    @NotBlank(message = "Password is required")

    private String password;
    @Expose
    @Setter
    @Getter
    private String resetToken;
    @Expose
    @Setter
    @Getter
    private Date resetTokenDate;
    @Expose
    @Setter
    @Getter
    private boolean resetTokenUsed;
    @Expose
    @Setter
    @Getter
    private Date expirationDate = new Date();
    @Expose
    @Setter
    @Getter
    boolean collector;
    //@ToString.Exclude
    // @JsonBackReference(value="customerFavoriteReference")
    @Setter
    @Getter
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "myCustomer")
    private CustomerFavorite customerFavorite;
    //@ToString.Exclude
    //  @JsonBackReference(value="customerCartReference")

    @Setter
    @Getter
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "myCustomer")
    private CustomerCart customerCart;
    @Setter
    @Getter
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Address> addressList;
    @Setter
    @Getter
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "myCustomer")
    private List<MyOrder> myOrders;

    @Setter
    @Getter
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "myCustomer")
    private RecentlyViewed recentlyViewed;


    @Setter
    @Getter
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "myCustomer")
    private List<MyReview> myReviews;
    public MyCustomer() {
      // this("randomId");
    }

    @PrePersist
    public void ensureId() {
        if (id == null) {
            id = String.valueOf(UUID.randomUUID());
        }
    }

    public MyCustomer(String id) {
        this.fullName = "anonymous";
        this.email = "example@email.com";
        this.id = id;
        this.telephone = "00000000";
        this.collector = false;
        this.password= "Password@StrongerThanYou1794"+id;
    }


    @Override
    public String getUsername() {
        return this.email;
    }


}