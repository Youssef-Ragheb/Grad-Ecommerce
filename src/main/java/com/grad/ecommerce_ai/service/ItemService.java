package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.enitity.Cart;
import com.grad.ecommerce_ai.enitity.Item;
import com.grad.ecommerce_ai.enitity.User;
import com.grad.ecommerce_ai.enitity.UserRoles;
import com.grad.ecommerce_ai.repository.BranchRepository;
import com.grad.ecommerce_ai.repository.CartRepository;
import com.grad.ecommerce_ai.repository.ItemRepository;
import com.grad.ecommerce_ai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;

    public ItemService(ItemRepository itemRepository, JwtService jwtService, UserRepository userRepository, BranchRepository branchRepository, CartService cartService, CartRepository cartRepository) {
        this.itemRepository = itemRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        this.cartService = cartService;
        this.cartRepository = cartRepository;
    }
    public ApiResponse<Item> save(Item item, String token) {
        ApiResponse<Item> apiResponse = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()&& userOptional.get().getUserRoles().equals(UserRoles.ROLE_CLIENT)) {
            if (!branchRepository.existsById(item.getBranchId())){
                apiResponse.setMessage("Branch does not exist");
                apiResponse.setStatusCode(404);
                apiResponse.setStatus(false);
                return apiResponse;

            }
            Item savedItem = itemRepository.save(item);
            cartService.addToCart(savedItem,token);
            apiResponse.setMessage("Successfully saved item");
            apiResponse.setStatusCode(200);
            apiResponse.setStatus(true);
            apiResponse.setData(savedItem);
            return apiResponse;
        }
        apiResponse.setMessage("Dont have access");
        apiResponse.setStatusCode(401);
        apiResponse.setStatus(false);
        apiResponse.setData(null);
        return apiResponse;
    }
    public ApiResponse<Item> updateItem(String id,Item item, String token) {
        ApiResponse<Item> apiResponse = new ApiResponse<>();
        item.setId(id);
        Long userId = jwtService.extractUserId(token);
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (cartOptional.isEmpty()) {
            apiResponse.setMessage("cart does not exist");
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            apiResponse.setData(null);
            return apiResponse;
        }
        Cart cart = cartOptional.get();
        Item savedItem = itemRepository.save(item);
        for (int i = 0; i < cart.getItems().size(); i++) {
            if(cart.getItems().get(i).getId().equals(savedItem.getId())){
                cart.getItems().remove(i);
                break;
            }
        }
        List<Item> items = cart.getItems();
        items.add(savedItem);
        cart.setItems(items);
        cartRepository.save(cart);
        apiResponse.setMessage("cart and item has been updated");
        apiResponse.setStatusCode(200);
        apiResponse.setStatus(true);
        apiResponse.setData(savedItem);
        return apiResponse;
    }
}
