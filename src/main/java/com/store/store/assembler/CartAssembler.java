package com.store.store.assembler;

import com.store.store.controller.CartController;
import com.store.store.controller.UserController;
import com.store.store.model.Cart;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CartAssembler implements RepresentationModelAssembler<Cart, EntityModel<Cart>> {
    @Override
    public EntityModel<Cart> toModel(Cart cart) {
        return EntityModel.of(cart,
            linkTo(methodOn(CartController.class).getCartPerUser(cart.getUserId())).withRel("userCart"),
            linkTo(methodOn(CartController.class).getCart(cart.getId())).withSelfRel(),
            linkTo(methodOn(CartController.class).getCarts()).withRel("carts")
        );
    }
}
