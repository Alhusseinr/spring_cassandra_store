package com.store.store.repository;

import com.store.store.model.CartItems;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.UUID;

public interface CartItemsRepository extends CassandraRepository<CartItems, UUID> {
    List<CartItems> findByCartId(UUID cartId);
}
