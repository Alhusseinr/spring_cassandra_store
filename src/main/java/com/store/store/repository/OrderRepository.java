package com.store.store.repository;

import com.store.store.model.Order;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends CassandraRepository<Order, UUID> {
    List<Order> findByUserId(UUID userId);
}
