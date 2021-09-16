package com.store.store.repository;

import com.store.store.model.OrderItems;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemsRepository extends CassandraRepository<OrderItems, UUID> {
    List<OrderItems> findByOrderId(UUID orderId);
}