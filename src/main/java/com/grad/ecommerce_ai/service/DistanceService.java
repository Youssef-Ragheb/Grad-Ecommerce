package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.entity.Branch;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DistanceService {

    private static final int EARTH_RADIUS_KM = 6371;

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    public Map<Long, Double> getBranchDistances(double userLat, double userLng, List<Branch> branches) {
        Map<Long, Double> distances = new HashMap<>();
        for (Branch branch : branches) {
            double distance = calculateDistance(userLat, userLng, branch.getLat(), branch.getLng());
            distances.put(branch.getBranchId(), distance);
        }
        return distances;
    }

    public List<Branch> sortBranchesByDistance(double userLat, double userLng, List<Branch> branches) {
        return branches.stream()
                .sorted(Comparator.comparingDouble(branch -> calculateDistance(userLat, userLng, branch.getLat(), branch.getLng())))
                .collect(Collectors.toList());
    }

    public Optional<Branch> getClosestBranch(double userLat, double userLng, List<Branch> branches) {
        return branches.stream()
                .min(Comparator.comparingDouble(branch -> calculateDistance(userLat, userLng, branch.getLat(), branch.getLng())));
    }
}
