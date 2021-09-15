package com.store.store.assembler;

import com.store.store.controller.AddressController;
import com.store.store.controller.UserController;
import com.store.store.model.Address;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AddressAssembler implements RepresentationModelAssembler<Address, EntityModel<Address>> {

    @Override
    public EntityModel<Address> toModel(Address address) {
        return EntityModel.of(address,
            linkTo(methodOn(AddressController.class).getAddress(address.getId())).withSelfRel(),
            linkTo(methodOn(UserController.class).getUserById(address.getUserId())).withRel("user_address"),
            linkTo(methodOn(AddressController.class).allAddresses()).withRel("address")
        );
    }
}
