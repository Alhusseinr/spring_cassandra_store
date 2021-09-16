package com.store.store.controller;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.store.store.assembler.ItemAssembler;
import com.store.store.model.Item;
import com.store.store.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
public class ItemController {
    @Autowired
    ItemRepository itemRepository;

    private final ItemAssembler itemAssembler;

    public ItemController(ItemAssembler itemAssembler) {
        this.itemAssembler = itemAssembler;
    }
    @PostMapping("/item")
    public ResponseEntity<EntityModel<Item>> addItem(@RequestBody Item item) {
        try{
            Item newItem = itemRepository.save(new Item(Uuids.timeBased(), item.getItemName(), item.getItemPrice(), item.getItemQuantity(), item.getItemDescription()));
            return ResponseEntity.created(linkTo(methodOn(ItemController.class).getItem(newItem.getId())).toUri()).body(itemAssembler.toModel(newItem));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<EntityModel<?>> getItem(@PathVariable("id") UUID id) {
        try {
            Optional<Item> item = itemRepository.findById(id);

            return ResponseEntity.ok(EntityModel.of(item, linkTo(methodOn(ItemController.class).getItem(id)).withSelfRel()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/item")
    public ResponseEntity<CollectionModel<EntityModel<Item>>> getAllItems() {
        try {
            List<EntityModel<Item>> items = itemRepository.findAll().stream().map(itemAssembler::toModel).collect(Collectors.toList());

            return ResponseEntity.ok(CollectionModel.of(items, linkTo(methodOn(ItemController.class).getAllItems()).withSelfRel()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
