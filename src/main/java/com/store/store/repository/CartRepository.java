package com.store.store.repository;

import com.store.store.model.Cart;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface CartRepository extends CassandraRepository<Cart, UUID> {
    @AllowFiltering
    Cart findByUserId(UUID userId);
}
