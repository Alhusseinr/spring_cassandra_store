package com.store.store.controller;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.store.store.assembler.UserAssembler;
import com.store.store.model.Address;
import com.store.store.model.Cart;
import com.store.store.model.User;
import com.store.store.repository.AddressRepository;
import com.store.store.repository.CartRepository;
import com.store.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    CartRepository cartRepository;

    private final UserAssembler userAssembler;

    public UserController(UserAssembler userAssembler) {
        this.userAssembler = userAssembler;
    }

    @PostMapping("/user")
    public ResponseEntity<?> addNewUser(@RequestBody User user) {
        try {
            if(userExists(user)) {
                User newUser = userRepository.save(new User(Uuids.timeBased(), user.getFirstName(), user.getLastName(), user.getEmail(), BCrypt.hashpw(user.getPassword(), BCrypt.gensalt())));
                addressRepository.save(new Address(Uuids.timeBased(), newUser.getId(), "", "", "", "", ""));
                cartRepository.save(new Cart(Uuids.timeBased(), newUser.getId()));
                return ResponseEntity.created(linkTo(methodOn(UserController.class).getUserById(newUser.getId())).toUri()).body(userAssembler.toModel(newUser));
            } else {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body("Email already in use");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<CollectionModel<EntityModel<User>>> allUsers() {
        try{
            List<EntityModel<User>> users = userRepository.findAll().stream().map(userAssembler::toModel).collect(Collectors.toList());

            return ResponseEntity.ok(CollectionModel.of(users, linkTo(methodOn(UserController.class).allUsers()).withSelfRel()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<EntityModel<User>> getUserById(@PathVariable("id") UUID id) {
        try {
            Optional<User> pulledUser = userRepository.findById(id);

            return ResponseEntity.ok(userAssembler.toModel(pulledUser.get()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean userExists(User user) {
        if(userRepository.findByEmail(user.getEmail()) == null) {
            return true;
        }
        return false;
    }
}
