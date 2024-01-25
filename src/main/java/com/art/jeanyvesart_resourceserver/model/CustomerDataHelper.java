package com.art.jeanyvesart_resourceserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@MappedSuperclass
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class CustomerDataHelper {
  @GeneratedValue(strategy = GenerationType.AUTO)

  @Id
    private long id;
    @ManyToOne
    private MyProduct myProduct;
    private int quantity;
  private Date date = new Date();



  public CustomerDataHelper(MyProduct myProduct, int quantity) {
  this.myProduct = myProduct; this.quantity = quantity;
  }
}
