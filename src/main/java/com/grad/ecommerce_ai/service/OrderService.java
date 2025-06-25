package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.OrderDetailsDTO;
import com.grad.ecommerce_ai.dto.RequestDTO;
import com.grad.ecommerce_ai.entity.*;
import com.grad.ecommerce_ai.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.grad.ecommerce_ai.mappers.CheckoutMapper.toRequestDTO;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ItemService itemService;
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final DrugService drugService;


    public OrderService(OrderRepository orderRepository, JwtService jwtService, UserRepository userRepository, CartRepository cartRepository, @Lazy ItemService itemService, RequestRepository requestRepository, ItemRepository itemRepository, DrugService drugService) {
        this.orderRepository = orderRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.itemService = itemService;
        this.requestRepository = requestRepository;
        this.itemRepository = itemRepository;
        this.drugService = drugService;
    }

    @Transactional
    public ApiResponse<Order> createOrder(Order order, String token) {
        ApiResponse<Order> apiResponse = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            apiResponse.setMessage("User not found");
            apiResponse.setStatus(false);
            apiResponse.setStatusCode(404);
            return apiResponse;
        }
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (cartOptional.isEmpty()) {
            apiResponse.setMessage("Cart not found");
            apiResponse.setStatus(false);
            apiResponse.setStatusCode(404);
            return apiResponse;
        }
        Order newOrder = orderRepository.save(order);
        List<Request> requests = itemService.findBestBranchesForOrder(cartOptional.get().getItems());

        List<String> requestIds = new ArrayList<>();
        float orderPrice = 0;
        for (Request request : requests) {
            orderPrice += request.getTotalPriceOfRequest();
            request.setOrderId(newOrder.getId());
            request.setCustomerId(userId);
            request.setRequestDate(LocalDateTime.now());
            request = requestRepository.save(request);
            requestIds.add(request.getRequestId());
            RequestDTO dto = toRequestDTO(request, user.get());
            // webSocketService.sendNewRequestToBranch(request.getBranchId(), dto);
        }
        newOrder.setStatus(Status.PENDING);
        newOrder.setRequestsIds(requestIds);
        newOrder.setUserId(userId);
        newOrder.setTotalPrice(orderPrice);
        newOrder.setOrderDateAndTime(LocalDateTime.now());
        newOrder = orderRepository.save(newOrder);
        // Notify user about the overall order
//        OrderStatusDTO orderStatusDTO = new OrderStatusDTO(
//                newOrder.getId(),
//                Status.PENDING,
//                LocalDateTime.now(),
//                "Order has been placed successfully",
//                newOrder.getRequestsIds() // Include all request IDs
//        );
        //webSocketService.sendOrderStatusUpdate(newOrder.getId(), orderStatusDTO);
        // completeOrder(requests,cartOptional.get());
        //webSocketService.sendOrderStatusUpdate(newOrder.getId(), Status.PENDING);
        apiResponse.setData(newOrder);//edit this shit
        //cartOptional
        cartOptional.get().getItems().clear();
        cartRepository.save(cartOptional.get());

        //////////////////////////////////////////////////////////////////////////
        return apiResponse;
    }
//    public ApiResponse<Order> saveOrder(Order order, String token) {
//        ApiResponse<Order> response = new ApiResponse<>();
//        Long userId = jwtService.extractUserId(token);
//        order.setUserId(userId);
//
//        Order savedOrder = orderRepository.save(order);
//        response.setStatus(true);
//        response.setStatusCode(200);
//        response.setMessage("Order placed successfully");
//        response.setData(savedOrder);
//
//        return response;
//    }
//    private void completeOrder (List<Request> requests, Cart cart) {
//
//        for(Request request : requests){
//
//           Long branchId = request.getBranchId();
//
//           for(Item item : request.getItems()){
//               Optional<InventoryDrug> inventoryDrug = inventoryDrugRepository
//                       .findByDrugIdAndBranchId(item.getDrugId(),branchId);
//
//               if(inventoryDrug.isPresent() ) {
//                   int currentStock = inventoryDrug.get().getStock();
//                   currentStock -= item.getQuantity();
//                   inventoryDrug.get().setStock(currentStock);
//                   inventoryDrugRepository.save(inventoryDrug.get());
//               }
//           }
//        }
//        cart.getItems().clear();
//        cartRepository.save(cart);
//    }

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

    public ApiResponse<List<OrderDetailsDTO>> getOrderDetails(String orderId) {
        ApiResponse<List<OrderDetailsDTO>> response = new ApiResponse<>();
        List<OrderDetailsDTO> orderDetailsDTOList = new ArrayList<>();
        List<Request> requestList = requestRepository.findByOrderId(orderId);
        List<String> itemsIds = new ArrayList<>();
        for (Request request : requestList) {
            for (Item item : request.getItems()) {
                itemsIds.add(item.getId());
            }
        }

        List<Item> itemList = itemRepository.findByIdIn(itemsIds);
        for (Item item : itemList) {
            OrderDetailsDTO dto = new OrderDetailsDTO();
            dto.setPrice(item.getPrice());
            dto.setQuantity(item.getQuantity());
            dto.setDrugId(item.getDrugId());
            Drugs drugs = drugService.findDrug(item.getDrugId()).orElse(null);
            dto.setDrugName(drugs.getDrugName());
            orderDetailsDTOList.add(dto);
        }
        response.setStatus(true);
        response.setStatusCode(200);
        response.setMessage("Order details retrieved");
        response.setData(orderDetailsDTOList);
        return response;
    }

    public void updateOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        List<Request> requestList = requestRepository.findByOrderId(orderId);
        Status currentStatus = order.getStatus();

        Status newStatus = calculateOrderStatus(requestList);
        if (currentStatus != newStatus) {
            order.setStatus(newStatus);
            orderRepository.save(order);
        }
    }

    private Status calculateOrderStatus(List<Request> requests) {
        boolean allShipped = true;
        boolean allReadyOrShipped = true;
        boolean anyPreparing = false;
        boolean anyPending = false;
        boolean allCanceled = true;

        for (Request request : requests) {
            Status status = request.getStatus();

            if (status != Status.CANCELED) {
                allCanceled = false;

                if (status != Status.SHIPPED) {
                    allShipped = false;
                }
                if (status != Status.SHIPPED && status != Status.READY) {
                    allReadyOrShipped = false;
                }
                if (status == Status.PREPARING) {
                    anyPreparing = true;
                }
                if (status == Status.PENDING) {
                    anyPending = true;
                }
            }
        }

        if (allCanceled) return Status.CANCELED;
        if (allShipped) return Status.SHIPPED;
        if (allReadyOrShipped) return Status.READY;
        if (anyPreparing && !anyPending) return Status.PREPARING;
        return Status.PENDING;
    }


}
