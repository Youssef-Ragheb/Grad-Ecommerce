package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.Cart;
import com.grad.ecommerce_ai.entity.Item;
import com.grad.ecommerce_ai.repository.CartRepository;
import com.grad.ecommerce_ai.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final JwtService jwtService;
    private final ItemRepository itemRepository;

    public CartService(CartRepository cartRepository, JwtService jwtService, ItemRepository itemRepository) {
        this.cartRepository = cartRepository;
        this.jwtService = jwtService;
        this.itemRepository = itemRepository;
    }

    public void addToCart (Item item, String token){
        ApiResponse<Cart> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if(cartOptional.isEmpty()){
            Cart cart = new Cart();
            List<Item> items = new ArrayList<>();
            items.add(item);
            cart.setUserId(userId);
            cart.setItems(items);
            response.setData(cartRepository.save(cart));
            response.setStatusCode(200);
            response.setMessage("Cart Created and added item");
            response.setStatus(true);
            return;
        }
        Cart cart = cartOptional.get();
        List<Item> items = cart.getItems();
        items.add(item);
        cart.setItems(items);
        response.setData(cartRepository.save(cart));
        response.setStatusCode(200);
        response.setMessage("item added");
        response.setStatus(true);

    }
    public ApiResponse<Cart> removeFromCart(Item item,String token){
        ApiResponse<Cart> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if(cartOptional.isEmpty()){
            response.setStatus(false);
            response.setMessage("cart is empty");
            response.setStatusCode(400);
            return response;
        }
        String itemId = item.getId();
        Cart cart = cartOptional.get();
        List<Item> items = cart.getItems();
        for (int i = 0;i<items.size();i++){
            if(items.get(i).getId().equals(itemId)){
                items.remove(i);
                break;
            }
        }
        itemRepository.deleteById(item.getId());
        cart.setItems(items);
        response.setData(cartRepository.save(cart));
        response.setStatusCode(200);
        response.setMessage("item deleted");
        response.setStatus(true);
        return response;
    }
    public ApiResponse<Cart> getItemsFromCart(String token){
        ApiResponse<Cart> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if(cartOptional.isEmpty()){
            response.setStatus(true);
            response.setData(new Cart("Not an Id",new ArrayList<>(),userId));
            response.setMessage("cart is empty");
            response.setStatusCode(200);
            return response;
        }
        response.setStatus(true);
        response.setData(cartOptional.get());
        response.setMessage("get Cart");
        response.setStatusCode(200);
        return response;
    }
}
