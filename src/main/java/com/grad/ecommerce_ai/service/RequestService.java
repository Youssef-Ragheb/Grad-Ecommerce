package com.grad.ecommerce_ai.service;


import com.grad.ecommerce_ai.entity.Item;
import com.grad.ecommerce_ai.entity.Request;
import com.grad.ecommerce_ai.entity.Status;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RequestService {

    public List<Request> createBranchRequests(List<Item> orderItems,
                                              List<Long> branchIds,
                                              Map<Long, Map<String, Integer>> branchInventories) {

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

}
