package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.Cart;
import com.grad.ecommerce_ai.entity.Item;
import com.grad.ecommerce_ai.entity.User;
import com.grad.ecommerce_ai.repository.CartRepository;
import com.grad.ecommerce_ai.repository.ItemRepository;
import com.grad.ecommerce_ai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final JwtService jwtService;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, JwtService jwtService, ItemRepository itemRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.jwtService = jwtService;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public ApiResponse<Cart> addToCart(Item item, String token) {
        Long userId = jwtService.extractUserId(token);
        User user = userRepository.findById(userId).orElseThrow();
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setItems(new ArrayList<>());
            return cartRepository.save(newCart);
        });

        cart.getItems().add(item);
        cartRepository.save(cart);

        return new ApiResponse<>(200, "Cart retrieved", true, cart);
    }

    public ApiResponse<Cart> removeFromCart(Item item, String token) {
        Long userId = jwtService.extractUserId(token);
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (cartOptional.isEmpty()) {
            return new ApiResponse<>(400, "Cart is empty", false, null);
        }

        Cart cart = cartOptional.get();
        cart.getItems().removeIf(i -> i.getId().equals(item.getId()));
        itemRepository.deleteById(item.getId());
        cartRepository.save(cart);

        return new ApiResponse<>(200, "Cart retrieved", true, cart);
    }

    public ApiResponse<Cart> getItemsFromCart(String token) {
        Long userId = jwtService.extractUserId(token);
        Cart cart = cartRepository.findByUserId(userId).orElseThrow();
        return new ApiResponse<>(200, "Cart retrieved", true, cart);
    }
}
