package com.store.store.controller;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.store.store.assembler.CartAssembler;
import com.store.store.model.Cart;
import com.store.store.model.CartItems;
import com.store.store.repository.CartItemsRepository;
import com.store.store.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class CartController {
    @Autowired
    CartRepository cartRepository;

    private final CartAssembler cartAssembler;
    private final CartItemsRepository cartItemsRepository;

    public CartController(CartAssembler cartAssembler, CartItemsRepository cartItemsRepository) {
        this.cartAssembler = cartAssembler;
        this.cartItemsRepository = cartItemsRepository;
    }

    @PostMapping("/cart/{userId}")
    public ResponseEntity<EntityModel<Cart>> createCart(@PathVariable("userId") UUID id) {
        try {
            Cart cart = cartRepository.save(new Cart(Uuids.timeBased(), id));

            return ResponseEntity.created(linkTo(methodOn(CartController.class).getCart(cart.getId())).toUri())
                    .body(cartAssembler.toModel(cart));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/cart/{id}")
    public ResponseEntity<EntityModel<Cart>> getCart(@PathVariable("id") UUID id) {
        try {
            Optional<Cart> cart = cartRepository.findById(id);

            return ResponseEntity.ok(cartAssembler.toModel(cart.get()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/cart")
    public ResponseEntity<CollectionModel<EntityModel<Cart>>> getCarts() {
        try {
            List<EntityModel<Cart>> carts = cartRepository.findAll().stream().map(cartAssembler::toModel).collect(Collectors.toList());
            
            return ResponseEntity.ok(CollectionModel.of(carts, linkTo(methodOn(UserController.class).allUsers()).withSelfRel()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/cart/user/{userId}")
    public ResponseEntity<EntityModel<Cart>> getCartPerUser(@PathVariable("userId") UUID id) {
        try {
            Cart cart = cartRepository.findByUserId(id);

            return ResponseEntity.ok(cartAssembler.toModel(cart));
            // TODO: this is missing getting all items for cart will be implemented later
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cart/add/{cartId}/item/{itemId}")
    public ResponseEntity<EntityModel<CartItems>> addItemToCart(@PathVariable("cartId") UUID cartId, @PathVariable("itemId") UUID itemId) {
        try {
            CartItems cartItem = cartItemsRepository.save(new CartItems(Uuids.timeBased(), cartId, itemId));

            return ResponseEntity.ok(EntityModel.of(cartItem));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
