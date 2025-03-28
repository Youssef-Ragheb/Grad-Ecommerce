package com.grad.ecommerce_ai.service;


import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.RequestDTO;
import com.grad.ecommerce_ai.entity.Item;
import com.grad.ecommerce_ai.entity.Request;
import com.grad.ecommerce_ai.dto.enums.Status;
import com.grad.ecommerce_ai.entity.User;
import com.grad.ecommerce_ai.entity.details.EmployeeDetails;
import com.grad.ecommerce_ai.repository.EmployeeDetailsRepository;
import com.grad.ecommerce_ai.repository.RequestRepository;
import com.grad.ecommerce_ai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.grad.ecommerce_ai.mappers.CheckoutMapper.toRequestDTO;

@Service
public class RequestService {

    private final JwtService jwtService;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;


    public RequestService(JwtService jwtService, EmployeeDetailsRepository employeeDetailsRepository, UserRepository userRepository, RequestRepository requestRepository) {
        this.jwtService = jwtService;
        this.employeeDetailsRepository = employeeDetailsRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;

    }

    public List<Request> createBranchRequests(List<Item> orderItems, List<Long> branchIds, Map<Long, Map<String, Integer>> branchInventories) {

        List<Request> requests = new ArrayList<>();

        // For each selected branch
        for (Long branchId : branchIds) {
            // Find which items this branch can fulfill
            List<Item> branchItems = new ArrayList<>();
            float branchTotal = 0.0f;
            Map<String, Integer> branchDrugs = branchInventories.get(branchId);

            for (Item item : orderItems) {
                if (branchDrugs.containsKey(item.getDrugId())) {
                    branchItems.add(item);
                    branchTotal += item.getPrice() * item.getQuantity();
                }
            }

            // Create request for this branch
            Request request = new Request();
            request.setBranchId(branchId);
            request.setItems(branchItems);
            request.setStatus(Status.PENDING);
            request.setTotalPriceOfRequest(branchTotal);

            requests.add(request);
        }

        return requests;
    }

    public ApiResponse<List<RequestDTO>> listBranchRequests(String token) {
        ApiResponse<List<RequestDTO>> apiResponse = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            apiResponse.setMessage("User not found");
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        Optional<EmployeeDetails> employeeDetails = employeeDetailsRepository.findByUser(userOptional.get());
        if (employeeDetails.isEmpty()) {
            apiResponse.setMessage("Do not have access");
            apiResponse.setStatusCode(401);
            apiResponse.setStatus(false);
            return apiResponse;
        }

        List<Request> requests = requestRepository.findByBranchId(employeeDetails.get().getBranch().getBranchId());

        apiResponse.setStatus(true);
        apiResponse.setMessage("Success");
        apiResponse.setStatusCode(200);
        List<RequestDTO> requestDTOS = new ArrayList<>();
        for (Request request : requests) {
            User user = userRepository.findById(request.getCustomerId()).orElseThrow();
            RequestDTO dto = toRequestDTO(request, user);
            requestDTOS.add(dto);
        }
        apiResponse.setData(requestDTOS);
        return apiResponse;
    }

    public ApiResponse<RequestDTO> updateRequestFromBranch(RequestDTO requestUpdated, String token) {
        ApiResponse<RequestDTO> apiResponse = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            apiResponse.setMessage("User not found");
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        Optional<EmployeeDetails> employeeDetails = employeeDetailsRepository.findByUser(userOptional.get());
        if (employeeDetails.isEmpty()) {
            apiResponse.setMessage("Do not have access");
            apiResponse.setStatusCode(401);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        Optional<Request> request = requestRepository.findById(requestUpdated.getId());
        if (request.isEmpty()) {
            apiResponse.setMessage("Request not found");
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        // check that employee access right request
        if (!Objects.equals(request.get().getBranchId(), employeeDetails.get().getBranch().getBranchId())) {
            apiResponse.setMessage("Branch id mismatch");
            apiResponse.setStatusCode(401);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        Request newRequest = request.get();
        newRequest.setStatus(requestUpdated.getStatus());
        User client = userRepository.findById(request.get().getCustomerId()).orElseThrow();
        requestRepository.save(newRequest);
        apiResponse.setMessage("Success");
        apiResponse.setStatusCode(200);
        apiResponse.setStatus(true);
        apiResponse.setData(toRequestDTO(newRequest, client));
        return apiResponse;
    }

}
