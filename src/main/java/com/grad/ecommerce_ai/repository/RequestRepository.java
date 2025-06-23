package com.grad.ecommerce_ai.repository;

import com.grad.ecommerce_ai.entity.Request;
import com.grad.ecommerce_ai.entity.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface RequestRepository extends MongoRepository<Request, String> {
    List<Request> findByBranchId(Long branchId);
    Long countByBranchIdInAndStatus(List<Long> branchIds, Status status);
    List<Request> findByOrderId(String orderId);
    //List<Request> findByOrderId(String orderId);
}
