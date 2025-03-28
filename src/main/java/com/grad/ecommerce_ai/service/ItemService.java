package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.entity.*;
import com.grad.ecommerce_ai.repository.*;
import com.grad.ecommerce_ai.dto.enums.UserRoles;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final RequestService requestService;
    private final CartRepository cartRepository;
    private final InventoryDrugRepository inventoryDrugRepository;

    public ItemService(ItemRepository itemRepository, JwtService jwtService, UserRepository userRepository,RequestService requestService, CartService cartService, CartRepository cartRepository, InventoryDrugRepository inventoryDrugRepository) {
        this.itemRepository = itemRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.requestService = requestService;
        this.cartRepository = cartRepository;
        this.inventoryDrugRepository = inventoryDrugRepository;
    }

    public ApiResponse<Item> save(Item item, String token) {
        ApiResponse<Item> apiResponse = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);
        item.setUserId(userId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent() && userOptional.get().getUserRoles().equals(UserRoles.ROLE_CLIENT)) {

            List<InventoryDrug> inventoryDrug = inventoryDrugRepository.findAllByDrugId(item.getDrugId());
            if (inventoryDrug.isEmpty()) {
                apiResponse.setMessage("drug Is not found");
                apiResponse.setStatusCode(404);
                apiResponse.setStatus(false);
                return apiResponse;
            }
            int maxQuantity = 0;
            float averagePrice = 0;
            for (InventoryDrug drug : inventoryDrug) {
                if (maxQuantity < drug.getStock()) {
                    maxQuantity = drug.getStock();
                }
                averagePrice += drug.getPrice();
            }
            if (maxQuantity < item.getQuantity()) {
                apiResponse.setMessage("Quantity exceeded");
                apiResponse.setStatusCode(400);
                apiResponse.setStatus(false);
                return apiResponse;
            }
            averagePrice = averagePrice / inventoryDrug.size();
            item.setPrice(averagePrice);
            Item savedItem = itemRepository.save(item);
            cartService.addToCart(savedItem, token);
            apiResponse.setMessage("Successfully saved item");
            apiResponse.setStatusCode(200);
            apiResponse.setStatus(true);
            apiResponse.setData(savedItem);
            return apiResponse;
        }
        apiResponse.setMessage("Dont have access");
        apiResponse.setStatusCode(401);
        apiResponse.setStatus(false);
        apiResponse.setData(null);
        return apiResponse;
    }

    public ApiResponse<Item> updateItem(String id, Item item, String token) {
        ApiResponse<Item> apiResponse = new ApiResponse<>();
        item.setId(id);
        Long userId = jwtService.extractUserId(token);
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if (cartOptional.isEmpty()) {
            apiResponse.setMessage("cart does not exist");
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            apiResponse.setData(null);
            return apiResponse;
        }
        Cart cart = cartOptional.get();
        Item savedItem = itemRepository.save(item);
        for (int i = 0; i < cart.getItems().size(); i++) {
            if (cart.getItems().get(i).getId().equals(savedItem.getId())) {
                cart.getItems().remove(i);
                break;
            }
        }
        List<Item> items = cart.getItems();
        items.add(savedItem);
        cart.setItems(items);
        cartRepository.save(cart);
        apiResponse.setMessage("cart and item has been updated");
        apiResponse.setStatusCode(200);
        apiResponse.setStatus(true);
        apiResponse.setData(savedItem);
        return apiResponse;
    }
    /*
    //This function Get the lowest count of  pharmacies that have the drugs for creating
    //the requests :)
//    public Map<Long,List<String>> getItemsForRequest(List<Item> items) {
//        Map<Long,List<String>> map = new HashMap<>();// list of long the branch id and list of drugs that is in this branch
//        ArrayList <String> drugsIds = new ArrayList<>();
//        //get the ids of the drugs they need
//        for(Item item : items) {
//            if(!drugsIds.contains(item.getDrugId())) {
//                drugsIds.add(item.getDrugId());
//            }
//        }
//        List<InventoryDrug> inventoryDrugList = inventoryDrugRepository.
//                findAllByDrugIdIn(drugsIds);
//        //looping and adding each branch and the drugs that found in
//        for (InventoryDrug inventory :inventoryDrugList ){
//            if(map.get(inventory.getBranchId())!= null){
//                List<String> list = map.get(inventory.getBranchId());
//                list.add(inventory.getDrugId());
//                map.put(inventory.getBranchId(), list);
//            }else {
//                List<String> list = new ArrayList<>();
//                list.add(inventory.getDrugId());
//                map.put(inventory.getBranchId(), list);
//            }
//        }
//        for(List<String> num : map.values()){
//            if(num.size() == items.size()){
//
//            }
//        }
//
//
//
//
//
//
//        return map;
//    }

     */
    public List<Request> findBestBranchesForOrder(List<Item> orderItems) {
        // 1. Calculate total needed quantities for each drug
        Map<String, Integer> neededDrugs = calculateNeededDrugs(orderItems);

        // 2. Find all branches that have at least some of the drugs we need
        Map<Long, Map<String, Integer>> branchInventories = getAvailableBranches(neededDrugs);

        // 3. Select branches that can cover all drugs with minimum branches
        List<Long> selectedBranchIds = selectMinimumBranches(neededDrugs, branchInventories);

        // 4. Create requests for each selected branch
        return requestService.createBranchRequests(orderItems, selectedBranchIds, branchInventories);
    }

    // Helper method 1: Calculate how much of each drug we need
    private Map<String, Integer> calculateNeededDrugs(List<Item> items) {
        Map<String, Integer> needed = new HashMap<>();
        for (Item item : items) {
            needed.put(item.getDrugId(), needed.getOrDefault(item.getDrugId(), 0) + item.getQuantity());
        }
        return needed;
    }

    // Helper method 2: Find branches with available stock
    private Map<Long, Map<String, Integer>> getAvailableBranches(Map<String, Integer> neededDrugs) {
        List<InventoryDrug> inventories = inventoryDrugRepository.findAllByDrugIdIn(new ArrayList<>(neededDrugs.keySet()));

        Map<Long, Map<String, Integer>> branchStocks = new HashMap<>();

        for (InventoryDrug inv : inventories) {
            String drugId = inv.getDrugId();
            int needed = neededDrugs.get(drugId);

            if (inv.getStock() >= needed) {
                branchStocks
                        .computeIfAbsent(inv.getBranchId(), k -> new HashMap<>())
                        .put(drugId, inv.getStock());
            }
        }

        return branchStocks;
    }

    // Helper method 3: Select the fewest branches that can fulfill everything
    private List<Long> selectMinimumBranches(Map<String, Integer> neededDrugs,
                                             Map<Long, Map<String, Integer>> branchInventories) {

        Set<String> remainingDrugs = new HashSet<>(neededDrugs.keySet());
        List<Long> selectedBranches = new ArrayList<>();

        while (!remainingDrugs.isEmpty()) {
            // Find the branch that has the most of our remaining drugs
            Long bestBranch = null;
            int mostDrugsCovered = 0;

            for (Long branchId : branchInventories.keySet()) {
                Set<String> branchDrugs = branchInventories.get(branchId).keySet();

                // See how many remaining drugs this branch has
                int covered = 0;
                for (String drugId : branchDrugs) {
                    if (remainingDrugs.contains(drugId)) {
                        covered++;
                    }
                }

                // Track the branch that covers the most
                if (covered > mostDrugsCovered) {
                    mostDrugsCovered = covered;
                    bestBranch = branchId;
                }
            }

//            if (bestBranch == null) {
//                throw new RuntimeException("Cannot fulfill all drugs from available branches");
//            }

            // Add this branch and remove the drugs it covers
            selectedBranches.add(bestBranch);
            remainingDrugs.removeAll(branchInventories.get(bestBranch).keySet());
        }

        return selectedBranches;
    }
}
