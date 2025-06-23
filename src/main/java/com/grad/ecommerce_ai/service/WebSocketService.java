//package com.grad.ecommerce_ai.service;
//
//import com.grad.ecommerce_ai.dto.OrderStatusDTO;
//import com.grad.ecommerce_ai.dto.RequestDTO;
//import com.grad.ecommerce_ai.entity.Status;
//import com.grad.ecommerce_ai.repository.RequestRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class WebSocketService {
//    private final SimpMessagingTemplate messagingTemplate;
//    private final RequestRepository requestRepository;
//
//    public WebSocketService(
//            SimpMessagingTemplate messagingTemplate,
//            RequestRepository requestRepository
//    ) {
//        this.messagingTemplate = messagingTemplate;
//        this.requestRepository = requestRepository;
//    }
//
//
//    public void sendOrderStatusUpdate(String orderId, Status status) {
//        // Get the request IDs for this order from the repository
//        List<String> requestIds = requestRepository.findByOrderId(orderId)
//                .stream()
//                .map(Request::getRequestId)
//                .collect(Collectors.toList());
//
//        OrderStatusDTO statusDTO = new OrderStatusDTO(
//                orderId,
//                status,
//                LocalDateTime.now(),
//                "Order status updated to: " + status,
//                requestIds
//        );
//
//        messagingTemplate.convertAndSend("/topic/orders/" + orderId, statusDTO);
//    }
//
//    // For notifying branches with new requests
//    public void sendNewRequestToBranch(Long branchId, RequestDTO requestDTO) {
//        messagingTemplate.convertAndSend("/topic/branch/" + branchId, requestDTO);
//    }
//
//    // For notifying client when request is updated
//    public void sendRequestStatusToClient(Long userId, RequestDTO requestDTO) {
//        messagingTemplate.convertAndSend("/topic/client/" + userId + "/requests", requestDTO);
//    }
//}