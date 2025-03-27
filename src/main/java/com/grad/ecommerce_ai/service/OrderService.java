package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.*;
import com.grad.ecommerce_ai.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ItemService itemService;
    private final RequestRepository requestRepository;
    private final InventoryDrugRepository inventoryDrugRepository;


    public OrderService(OrderRepository orderRepository, JwtService jwtService, UserRepository userRepository, CartRepository cartRepository, ItemService itemService, RequestRepository requestRepository, InventoryDrugRepository inventoryDrugRepository) {
        this.orderRepository = orderRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.itemService = itemService;
        this.requestRepository = requestRepository;
        this.inventoryDrugRepository = inventoryDrugRepository;
    }
    @Transactional
    public ApiResponse<Order> createOrder(Order order, String token) {
        ApiResponse<Order> apiResponse = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            apiResponse.setMessage("User not found");
            apiResponse.setStatus(false);
            apiResponse.setStatusCode(404);
            return apiResponse;
        }
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if(cartOptional.isEmpty()){
            apiResponse.setMessage("Cart not found");
            apiResponse.setStatus(false);
            apiResponse.setStatusCode(404);
            return apiResponse;
        }
        Order newOrder = orderRepository.save(order); ;
        List<Request> requests = itemService.findBestBranchesForOrder(cartOptional.get().getItems());

        List<String> requestIds = new ArrayList<>();
        float orderPrice = 0;
        for(Request request : requests){
            orderPrice += request.getTotalPriceOfRequest();
            request.setOrderId(newOrder.getId());
            request.setCustomerId(userId);
            request = requestRepository.save(request);
            requestIds.add(request.getRequestId());
        }
        newOrder.setStatus(Status.PENDING);
        newOrder.setRequestsIds(requestIds);
        newOrder.setUserId(userId);
        newOrder.setTotalPrice(orderPrice);
        newOrder = orderRepository.save(newOrder);

        completeOrder(requests,cartOptional.get());

        apiResponse.setData(newOrder);//edit this shit



        //////////////////////////////////////////////////////////////////////////
        return apiResponse;
    }
    public ApiResponse<Order> saveOrder(Order order, String token) {
        ApiResponse<Order> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        order.setUserId(userId);

        Order savedOrder = orderRepository.save(order);
        response.setStatus(true);
        response.setStatusCode(200);
        response.setMessage("Order placed successfully");
        response.setData(savedOrder);

        return response;
    }
    private void completeOrder (List<Request> requests, Cart cart) {

        for(Request request : requests){

           Long branchId = request.getBranchId();

           for(Item item : request.getItems()){
               Optional<InventoryDrug> inventoryDrug = inventoryDrugRepository
                       .findByDrugIdAndBranchId(item.getDrugId(),branchId);

               if(inventoryDrug.isPresent() ) {
                   int currentStock = inventoryDrug.get().getStock();
                   currentStock -= item.getQuantity();
                   inventoryDrug.get().setStock(currentStock);
                   inventoryDrugRepository.save(inventoryDrug.get());
               }
           }
        }
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public ApiResponse<Order> getOrderById(String id, String token) {
        ApiResponse<Order> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);

        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent() && orderOptional.get().getUserId().equals(userId)) {
            response.setStatus(true);
            response.setStatusCode(200);
            response.setMessage("Order found");
            response.setData(orderOptional.get());
        } else {
            response.setStatus(false);
            response.setStatusCode(404);
            response.setMessage("Order not found or unauthorized");
        }

        return response;
    }

    public ApiResponse<List<Order>> getUserOrders(String token) {
        ApiResponse<List<Order>> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);

        List<Order> userOrders = orderRepository.findAllByUserId(userId);
        response.setStatus(true);
        response.setStatusCode(200);
        response.setMessage("Orders retrieved");
        response.setData(userOrders);

        return response;
    }
}
