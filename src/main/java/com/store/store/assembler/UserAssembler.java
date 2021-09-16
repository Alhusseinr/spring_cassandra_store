package com.store.store.assembler;

import com.store.store.controller.AddressController;
import com.store.store.controller.CartController;
import com.store.store.controller.UserController;
import com.store.store.model.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {
        return EntityModel.of(user,
                linkTo(methodOn(AddressController.class).getUserAddress(user.getId())).withRel("userAddress"),
                linkTo(methodOn(CartController.class).getCartPerUser(user.getId())).withRel("userCart"),
                linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).allUsers()).withRel("users")
        );
    }
}
