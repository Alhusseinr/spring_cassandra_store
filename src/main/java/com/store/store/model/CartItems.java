package com.store.store.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table
public class CartItems {
    private @PrimaryKey UUID id;
    private UUID cartId;
    private UUID itemId;

    public CartItems() {}

    public CartItems(UUID id, UUID cartId, UUID itemId) {
        this.id = id;
        this.cartId = cartId;
        this.itemId = itemId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return "CartItems{" +
                "id=" + id +
                ", cartId=" + cartId +
                ", itemId=" + itemId +
                '}';
    }
}
