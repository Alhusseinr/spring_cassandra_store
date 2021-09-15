package com.store.store.controller;

import com.store.store.assembler.AddressAssembler;
import com.store.store.model.Address;
import com.store.store.repository.AddressRepository;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
public class AddressController {
    @Autowired
    AddressRepository addressRepository;

    private final AddressAssembler addressAssembler;

    public AddressController(AddressAssembler addressAssembler) {
        this.addressAssembler = addressAssembler;
    }

    @GetMapping("address/user/{id}")
    public ResponseEntity<EntityModel<Address>> getUserAddress(@PathVariable("id") UUID id) {
        try {
            Address address = addressRepository.findAddressByUserId(id);

            return ResponseEntity.ok(addressAssembler.toModel(address));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/address/{id}")
    public ResponseEntity<EntityModel<Address>> getAddress(@PathVariable("id") UUID id) {
        try {
            Optional<Address> address = addressRepository.findById(id);

            return ResponseEntity.ok(addressAssembler.toModel(address.get()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/address")
    public ResponseEntity<CollectionModel<EntityModel<Address>>> allAddresses() {
        try {
            List<EntityModel<Address>> addresses = addressRepository.findAll().stream().map(addressAssembler::toModel).collect(Collectors.toList());

            return ResponseEntity.ok(CollectionModel.of(addresses, linkTo(methodOn(AddressController.class).allAddresses()).withSelfRel()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/address/user/{id}/update")
    public ResponseEntity<EntityModel<Address>> update(@PathVariable("id") UUID userId, @RequestBody Address address) {
        try{
            Address updatedAddress = addressRepository.findAddressByUserId(userId);

            updatedAddress.setAddress(address.getAddress());
            updatedAddress.setAddress2(address.getAddress2());
            updatedAddress.setCity(address.getCity());
            updatedAddress.setState(address.getState());
            updatedAddress.setZipcode(address.getZipcode());

            return ResponseEntity.ok(addressAssembler.toModel(addressRepository.save(updatedAddress)));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
