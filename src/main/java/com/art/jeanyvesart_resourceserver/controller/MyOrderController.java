package com.art.jeanyvesart_resourceserver.controller;

import com.art.jeanyvesart_resourceserver.model.MyOrder;
import com.art.jeanyvesart_resourceserver.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/order/customer", produces = "application/json")
@Slf4j
public class MyOrderController {
    private final OrderRepository orderRepository;

    public MyOrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/{userID}")
    public ResponseEntity<List<MyOrder>> allMyOrders(@PathVariable("userID") String userID) {
        Optional<List<MyOrder>> items = orderRepository.findByMyCustomer_Id(userID);

        if (items.isPresent()) {
            List<MyOrder> products = items.get();
log.info("orders, {}", products);
            return new ResponseEntity<>(products, HttpStatus.OK);
        }

        return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);

    }

}
