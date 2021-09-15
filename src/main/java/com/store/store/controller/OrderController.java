package com.store.store.controller;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.store.store.model.Order;
import com.store.store.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
public class OrderController {
    @Autowired
    OrderRepository orderRepository;

    @PostMapping("/order/{userId}")
    public ResponseEntity<?> createNewOrder(@PathVariable("userId") UUID userId, @RequestBody Order order) {
        try{
            Order newOrder = orderRepository.save(new Order(Uuids.timeBased(), userId, order.getStatus(), order.getOrderTotal()));

            return ResponseEntity.ok(newOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
