package com.store.store.assembler;

import com.store.store.controller.ItemController;
import com.store.store.model.Item;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ItemAssembler implements RepresentationModelAssembler<Item, EntityModel<Item>> {

    @Override
    public EntityModel<Item> toModel(Item item) {
        return EntityModel.of(item,
            linkTo(methodOn(ItemController.class).getItem(item.getId())).withSelfRel(),
            linkTo(methodOn(ItemController.class).getAllItems()).withRel("items")
        );
    }
}
