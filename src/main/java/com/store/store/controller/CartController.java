package com.store.store.controller;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.store.store.assembler.CartAssembler;
import com.store.store.model.Cart;
import com.store.store.model.CartItems;
import com.store.store.model.Item;
import com.store.store.repository.CartItemsRepository;
import com.store.store.repository.CartRepository;
import com.store.store.repository.ItemRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class CartController {
    @Autowired
    CartRepository cartRepository;

    private final CartAssembler cartAssembler;
    private final CartItemsRepository cartItemsRepository;
    private final ItemRepository itemRepository;

    public CartController(CartAssembler cartAssembler, CartItemsRepository cartItemsRepository, ItemRepository itemRepository) {
        this.cartAssembler = cartAssembler;
        this.cartItemsRepository = cartItemsRepository;
        this.itemRepository = itemRepository;
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

    @GetMapping("/cart/getItems/{id}")
    public ResponseEntity<?> getCartItems(@PathVariable("id") UUID id) {
        try {
            List<CartItems> cartItems = cartItemsRepository.findByCartId(id);
            ArrayList<Item> items = new ArrayList<>();
            HashMap<UUID, Integer> itemsInCart = new HashMap();
            double total = 0;

            for(CartItems cartItem : cartItems) {
                Item fetchItem = itemRepository.findById(cartItem.getItemId()).get();
                if(!itemsInCart.containsKey(fetchItem.getId())) {
                    itemsInCart.put(itemRepository.findById(cartItem.getItemId()).get().getId(), 1);
                } else if(itemsInCart.containsKey(fetchItem.getId())) {
                    itemsInCart.put(
                            itemRepository.findById(cartItem.getItemId()).get().getId(),
                            itemsInCart.get(itemRepository.findById(cartItem.getItemId()).get().getId()) + 1
                    );
                }
            }

            for(UUID key : itemsInCart.keySet()) {
                Item fetchedItem = itemRepository.findById(key).get();
                Item it = new Item();

                it.setId(fetchedItem.getId());
                it.setItemName(fetchedItem.getItemName());
                it.setItemPrice(fetchedItem.getItemPrice());
                it.setItemQuantity(itemsInCart.get(key));
                it.setItemDescription(fetchedItem.getItemDescription());

                items.add(it);
            }

            for(Item item: items) {
                if(item.getItemQuantity() == 1) {
                    total += item.getItemPrice();
                }else if(item.getItemQuantity() > 1) {
                    total += item.getItemPrice() * item.getItemQuantity();
                }
            }

            System.out.println(total);

            JSONObject obj = new JSONObject();
            obj.put("items", items);
            obj.put("total", total);

            return ResponseEntity.ok(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ArrayList<Item> updateItemQty(HashMap<UUID, Integer> itemsInCart) {
        ArrayList<Item> items = new ArrayList<>();

        System.out.println("inside update");

        return items;
    }

    @DeleteMapping("/cart/{cartId}/item/{itemId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable("cartId") UUID cartId, @PathVariable("itemId") UUID itemId) {
        try {
            List<CartItems> cartItems = cartItemsRepository.findByItemId(itemId);
            CartItems deleteCartItem = null;
            int count = 0;

            for(CartItems cartItem : cartItems) {
                if(cartItem.getCartId() == cartId && cartItem.getItemId() == itemId && count < 1) {
                    count++;
                    deleteCartItem = cartItem;
                } else if(count >= 1) {
                    count++;
                }
            }

            cartItemsRepository.delete(deleteCartItem);
            return ResponseEntity.ok(HttpStatus.OK);
            
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
