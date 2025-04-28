package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.OrderDTO;
import com.grad.ecommerce_ai.dto.enums.Status;
import com.grad.ecommerce_ai.entity.*;
import com.grad.ecommerce_ai.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.grad.ecommerce_ai.mappers.DtoConverter.orderDtoToOrder;
import static com.grad.ecommerce_ai.mappers.DtoConverter.orderToDto;

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
    public ApiResponse<OrderDTO> createOrder(OrderDTO orderDTO, String token) {
        ApiResponse<OrderDTO> apiResponse = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            apiResponse.setMessage("User not found");
            apiResponse.setStatus(false);
            apiResponse.setStatusCode(404);
            return apiResponse;
        }
        orderDTO.setUser(orderDTO.getUser());
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (cartOptional.isEmpty()) {
            apiResponse.setMessage("Cart not found");
            apiResponse.setStatus(false);
            apiResponse.setStatusCode(404);
            return apiResponse;
        }
        orderDTO.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        Order newOrder = orderRepository.save(orderDtoToOrder(orderDTO));
        List<Request> requests = itemService.findBestBranchesForOrder(cartOptional.get().getItems());
        newOrder.setRequests(requests);
        List<Request> updatedRequests = new ArrayList<>();
        float orderPrice = 0;
        for (Request request : requests) {
            orderPrice += request.getTotalPriceOfRequest();
            request.setOrder(newOrder);
            request.setCustomer(user.get());
            request = requestRepository.save(request);
            updatedRequests.add(request);
        }
        newOrder.setStatus(Status.PENDING);
        newOrder.setRequests(updatedRequests);
        newOrder.setUser(user.get());
        newOrder.setTotalPrice(orderPrice);
        newOrder = orderRepository.save(newOrder);

        completeOrder(requests, cartOptional.get());

        apiResponse.setData(orderToDto(newOrder));

        return apiResponse;
    }

    private void completeOrder(List<Request> requests, Cart cart) {

        for (Request request : requests) {

            Long branchId = request.getBranch().getBranchId();

            for (Item item : request.getItems()) {
                Optional<InventoryDrug> inventoryDrug = inventoryDrugRepository.findByDrug_IdAndBranch_BranchId(item.getDrug().getId(), branchId);

                if (inventoryDrug.isPresent()) {
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

    public ApiResponse<OrderDTO> getOrderById(Long id, String token) {
        ApiResponse<OrderDTO> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent() && orderOptional.get().getUser().getId().equals(userId)) {
            response.setStatus(true);
            response.setStatusCode(200);
            response.setMessage("Order found");
            response.setData(orderToDto(orderOptional.get()));
        } else {
            response.setStatus(false);
            response.setStatusCode(404);
            response.setMessage("Order not found or unauthorized");
        }

        return response;
    }

    public ApiResponse<List<OrderDTO>> getUserOrders(String token) {
        ApiResponse<List<OrderDTO>> response = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);

        List<Order> userOrders = orderRepository.findAllByUserId(userId);
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : userOrders) {
            orderDTOs.add(orderToDto(order));
        }
        response.setStatus(true);
        response.setStatusCode(200);
        response.setMessage("Orders retrieved");
        response.setData(orderDTOs);

        return response;
    }
}
