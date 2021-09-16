package com.store.store.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table
public class Cart {
    private @PrimaryKey UUID id;
    private UUID userId;
    private UUID cartItemsId;

    public Cart() {}

    public Cart(UUID id, UUID userId, UUID cartItemsId) {
        this.id = id;
        this.userId = userId;
        this.cartItemsId = cartItemsId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getCartItemsId() {
        return cartItemsId;
    }

    public void setCartItemsId(UUID cartItemsId) {
        this.cartItemsId = cartItemsId;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", userId=" + userId +
                ", cartItemsId=" + cartItemsId +
                '}';
    }
}
