package com.grad.ecommerce_ai.service;


import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.RequestDTO;
import com.grad.ecommerce_ai.dto.enums.Status;
import com.grad.ecommerce_ai.entity.Branch;
import com.grad.ecommerce_ai.entity.Item;
import com.grad.ecommerce_ai.entity.Request;
import com.grad.ecommerce_ai.entity.User;
import com.grad.ecommerce_ai.entity.details.EmployeeDetails;
import com.grad.ecommerce_ai.repository.BranchRepository;
import com.grad.ecommerce_ai.repository.EmployeeDetailsRepository;
import com.grad.ecommerce_ai.repository.RequestRepository;
import com.grad.ecommerce_ai.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.grad.ecommerce_ai.mappers.DtoConverter.RequestToRequestDTO;


@Service
public class RequestService {

    private final JwtService jwtService;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final BranchRepository branchRepository;


    public RequestService(JwtService jwtService, EmployeeDetailsRepository employeeDetailsRepository, UserRepository userRepository, RequestRepository requestRepository, BranchRepository branchRepository) {
        this.jwtService = jwtService;
        this.employeeDetailsRepository = employeeDetailsRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.branchRepository = branchRepository;
    }

    public List<Request> createBranchRequests(List<Item> orderItems, List<Long> branchIds, Map<Long, Map<Long, Integer>> branchInventories) {

        List<Request> requests = new ArrayList<>();

        // For each selected branch
        for (Long branchId : branchIds) {
            // Find which items this branch can fulfill
            List<Item> branchItems = new ArrayList<>();
            float branchTotal = 0.0f;
            Map<Long, Integer> branchDrugs = branchInventories.get(branchId);

            for (Item item : orderItems) {
                if (branchDrugs.containsKey(item.getDrug().getId())) {
                    branchItems.add(item);
                    branchTotal += item.getPrice() * item.getQuantity();
                }
            }

            // Create request for this branch
            Request request = new Request();
            Branch branch = branchRepository.findById(branchId).orElseThrow();
            request.setBranch(branch);
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

        List<Request> requests = requestRepository.findAllByBranch_BranchId(employeeDetails.get().getBranch().getBranchId());

        apiResponse.setStatus(true);
        apiResponse.setMessage("Success");
        apiResponse.setStatusCode(200);
        List<RequestDTO> requestDTOS = new ArrayList<>();
        for (Request request : requests) {

            RequestDTO dto = RequestToRequestDTO(request);
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
        Optional<Request> request = requestRepository.findById(requestUpdated.getRequestId());
        if (request.isEmpty()) {
            apiResponse.setMessage("Request not found");
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        // check that employee access right request
        if (!Objects.equals(request.get().getBranch().getBranchId(), employeeDetails.get().getBranch().getBranchId())) {
            apiResponse.setMessage("Branch id mismatch");
            apiResponse.setStatusCode(401);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        Request newRequest = request.get();
        newRequest.setStatus(requestUpdated.getStatus());
        requestRepository.save(newRequest);
        apiResponse.setMessage("Success");
        apiResponse.setStatusCode(200);
        apiResponse.setStatus(true);
        apiResponse.setData(RequestToRequestDTO(newRequest));
        return apiResponse;
    }

}
