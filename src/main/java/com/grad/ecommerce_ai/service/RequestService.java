package com.grad.ecommerce_ai.service;


import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.RequestDTO;
import com.grad.ecommerce_ai.entity.*;
import com.grad.ecommerce_ai.entity.details.CompanyDetails;
import com.grad.ecommerce_ai.entity.details.EmployeeDetails;
import com.grad.ecommerce_ai.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.grad.ecommerce_ai.mappers.CheckoutMapper.toRequestDTO;

@Service
public class RequestService {

    private final JwtService jwtService;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final InventoryDrugRepository inventoryDrugRepository;
    private final CompanyDetailsRepository companyDetailsRepository;
    private final BranchRepository branchRepository;
    private final OrderService orderService;
    private final InventoryDrugService inventoryDrugService;


    public RequestService(JwtService jwtService, EmployeeDetailsRepository employeeDetailsRepository, UserRepository userRepository, RequestRepository requestRepository, InventoryDrugRepository inventoryDrugRepository, CompanyDetailsRepository companyDetailsRepository, BranchRepository branchRepository, OrderService orderService, InventoryDrugService inventoryDrugService) {
        this.jwtService = jwtService;
        this.employeeDetailsRepository = employeeDetailsRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.inventoryDrugRepository = inventoryDrugRepository;
        this.companyDetailsRepository = companyDetailsRepository;
        this.branchRepository = branchRepository;
        this.orderService = orderService;
        this.inventoryDrugService = inventoryDrugService;
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

    public ApiResponse<List<RequestDTO>> listBranchRequestsByCompany(Long branchId) {
        ApiResponse<List<RequestDTO>> apiResponse = new ApiResponse<>();

        List<Request> requests = requestRepository.findByBranchId(branchId);

        List<RequestDTO> requestDTOS = new ArrayList<>();
        for (Request request : requests) {
            User user = userRepository.findById(request.getCustomerId()).orElseThrow();
            RequestDTO dto = toRequestDTO(request, user);
            requestDTOS.add(dto);
        }
        apiResponse.setData(requestDTOS);
        apiResponse.setStatus(true);
        apiResponse.setMessage("Success");
        apiResponse.setStatusCode(200);
        return apiResponse;
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

    @Transactional
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

        if (Status.SHIPPED.equals(requestUpdated.getStatus())) {
            List<String> inventoryDrugIds = new ArrayList<>();
            HashMap<String, Integer> mapOfInventoryDrugsAndQuantity = new HashMap<>();
            for (Item item : request.get().getItems()) {
                inventoryDrugIds.add(item.getDrugId());
                mapOfInventoryDrugsAndQuantity.put(item.getDrugId(), item.getQuantity());
            }

            List<InventoryDrug> inventoryDrugList = inventoryDrugRepository.findAllByBranchIdAndDrugIdIn(requestUpdated.getBranchId(), inventoryDrugIds);
            for (InventoryDrug inventoryDrug : inventoryDrugList) {
                int quantity = mapOfInventoryDrugsAndQuantity.get(inventoryDrug.getDrugId());
                if (inventoryDrug.getStock() < quantity) {
                    apiResponse.setMessage("Insufficient stock for drug " + inventoryDrug.getDrugName());
                    apiResponse.setStatus(false);
                    apiResponse.setStatusCode(400);
                    return apiResponse;
                }
                inventoryDrug.setStock(inventoryDrug.getStock() - quantity);
                inventoryDrugService.saveInventory(inventoryDrug);
            }


        }

        User client = userRepository.findById(request.get().getCustomerId()).orElseThrow();
        //check to update order status
        requestRepository.save(newRequest);
        apiResponse.setMessage("Success");
        apiResponse.setStatusCode(200);
        apiResponse.setStatus(true);
        apiResponse.setData(toRequestDTO(newRequest, client));
        orderService.updateOrder(requestUpdated.getOrderId());
        //webSocketService.sendOrderStatusUpdate(requestUpdated.getOrderId(), newRequest.getStatus());
        return apiResponse;
    }

    @Transactional
    public ApiResponse<RequestDTO> updateRequestFromCompany(RequestDTO requestUpdated, String token) {
        ApiResponse<RequestDTO> apiResponse = new ApiResponse<>();

        Long userId = jwtService.extractUserId(token);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            apiResponse.setMessage("User not found");
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            return apiResponse;
        }

        Optional<CompanyDetails> companyDetails = companyDetailsRepository.findByUser(userOptional.get());
        if (companyDetails.isEmpty()) {
            apiResponse.setMessage("Company not found");
            apiResponse.setStatusCode(404);
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

        boolean notFound = true;
        Optional<List<Branch>> branchList = branchRepository.findBranchByCompanyCompanyId(companyDetails.get()
                .getCompany().getCompanyId());
        for (Branch branch : branchList.get()) {
            if(branch.getBranchId().equals(requestUpdated.getBranchId())){
                notFound = false;
                break;
            }
        }
        if(notFound){
            apiResponse.setMessage("Do not have access to the branch");
            apiResponse.setStatusCode(401);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        Request newRequest = request.get();
        newRequest.setStatus(requestUpdated.getStatus());

        if (Status.SHIPPED.equals(requestUpdated.getStatus())) {
            List<String> inventoryDrugIds = new ArrayList<>();
            HashMap<String, Integer> mapOfInventoryDrugsAndQuantity = new HashMap<>();
            for (Item item : request.get().getItems()) {
                inventoryDrugIds.add(item.getDrugId());
                mapOfInventoryDrugsAndQuantity.put(item.getDrugId(), item.getQuantity());
            }

            List<InventoryDrug> inventoryDrugList = inventoryDrugRepository.findAllByBranchIdAndDrugIdIn(requestUpdated.getBranchId(), inventoryDrugIds);
            for (InventoryDrug inventoryDrug : inventoryDrugList) {
                int quantity = mapOfInventoryDrugsAndQuantity.get(inventoryDrug.getDrugId());
                if (inventoryDrug.getStock() < quantity) {
                    apiResponse.setMessage("Insufficient stock for drug " + inventoryDrug.getDrugName());
                    apiResponse.setStatus(false);
                    apiResponse.setStatusCode(400);
                    return apiResponse;
                }
                inventoryDrug.setStock(inventoryDrug.getStock() - quantity);
                inventoryDrugService.saveInventory(inventoryDrug);
            }
        }

        User client = userRepository.findById(request.get().getCustomerId()).orElseThrow();
        //check to update order status
        requestRepository.save(newRequest);
        apiResponse.setMessage("Success");
        apiResponse.setStatusCode(200);
        apiResponse.setStatus(true);
        apiResponse.setData(toRequestDTO(newRequest, client));
        orderService.updateOrder(requestUpdated.getOrderId());
        //webSocketService.sendOrderStatusUpdate(requestUpdated.getOrderId(), newRequest.getStatus());
        return apiResponse;

    }

    public Long pendingRequestsForCompany(List<Long> branchIds){
        return requestRepository.countByBranchIdInAndStatus(branchIds,Status.PENDING);
    }

    public float calculateMonthlyRevenue(Long branchId) {
        LocalDateTime startOfLastMonth = LocalDateTime.now().minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfLastMonth = startOfLastMonth.withDayOfMonth(startOfLastMonth.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        List<Request> shippedRequests = requestRepository.findByBranchIdAndStatusAndRequestDateBetween(
                branchId,
                Status.SHIPPED,
                startOfLastMonth,
                endOfLastMonth
        );
        System.out.println(shippedRequests.get(0));

        return (float) shippedRequests.stream()
                .mapToDouble(Request::getTotalPriceOfRequest)
                .sum();
    }



}


