package com.grad.ecommerce_ai.service;

import com.grad.ecommerce_ai.dto.ApiResponse;
import com.grad.ecommerce_ai.dto.BranchDTO;
import com.grad.ecommerce_ai.entity.Branch;
import com.grad.ecommerce_ai.entity.Company;
import com.grad.ecommerce_ai.entity.InventoryDrug;
import com.grad.ecommerce_ai.mappers.DtoConverter;
import com.grad.ecommerce_ai.repository.BranchRepository;
import com.grad.ecommerce_ai.repository.CompanyRepository;
import com.grad.ecommerce_ai.repository.InventoryDrugRepository;
import com.grad.ecommerce_ai.utils.CheckAuth;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.grad.ecommerce_ai.mappers.DtoConverter.*;

@Service
public class BranchService {
    private final BranchRepository branchRepository;
    private final CompanyRepository companyRepository;
    private final CheckAuth checkAuth;
    private final JwtService jwtService;
    private final InventoryDrugRepository inventoryDrugRepository;

    public BranchService(BranchRepository branchRepository, CompanyRepository companyRepository, CheckAuth checkAuth, JwtService jwtService, InventoryDrugRepository inventoryDrugRepository) {
        this.branchRepository = branchRepository;
        this.companyRepository = companyRepository;
        this.checkAuth = checkAuth;
        this.jwtService = jwtService;
        this.inventoryDrugRepository = inventoryDrugRepository;
    }

    public ApiResponse<List<BranchDTO>> getAllBranches() {
        ApiResponse<List<BranchDTO>> apiResponse = new ApiResponse<>();
        List<Branch> branches = branchRepository.findAll();
        List<BranchDTO> branchDTOs = branchListToDtoList(branches);
        apiResponse.setData(branchDTOs);
        apiResponse.setMessage("Branches found");
        apiResponse.setStatusCode(200);
        apiResponse.setStatus(true);
        return apiResponse;
    }

    public ApiResponse<BranchDTO> getBranchById(Long branchId) {
        ApiResponse<BranchDTO> apiResponse = new ApiResponse<>();
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isPresent()) {
            BranchDTO branchDTO = branchToDto(branch.get());
            apiResponse.setData(branchDTO);
            apiResponse.setMessage("Branch found");
            apiResponse.setStatusCode(200);
            apiResponse.setStatus(true);
            return apiResponse;
        }
        apiResponse.setMessage("Branch not found");
        apiResponse.setStatusCode(404);
        apiResponse.setStatus(false);
        return apiResponse;
    }

    private boolean checkBranchName(String branchName, Company company) {
        List<Branch> branches = company.getBranchList();
        for (Branch branch : branches) {
            if (branch.getBranchName().equals(branchName)) {
                return true;
            }
        }
        return false;
    }

    public ApiResponse<BranchDTO> createBranch(BranchDTO branchDTO, Long companyId, String token) {
        ApiResponse<BranchDTO> apiResponse = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);

        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        if (optionalCompany.isEmpty()) {
            apiResponse.setMessage("Company ID not found");
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            return apiResponse;
        }

//        if (!checkAuth.checkAuthToCompany(userId, companyId)) {
//            apiResponse.setMessage("Unauthorized");
//            apiResponse.setStatusCode(401);
//            apiResponse.setStatus(false);
//            return apiResponse;
//        }

        Branch branch = DtoConverter.branchDTOToBranch(branchDTO);

        branch.setCompany(optionalCompany.get());

        // Check if a branch with the same name exists within the same company
        if (checkBranchName(branchDTO.getBranchName(), optionalCompany.get())) {
            apiResponse.setMessage("branch name is used before ");
            apiResponse.setStatusCode(400);
            apiResponse.setStatus(false);
            return apiResponse;
        }
        branch.setBranchState(true);
        Branch savedBranch = branchRepository.save(branch);
        BranchDTO savedBranchDTO = branchToDto(savedBranch);
        savedBranchDTO.setCompanyDto(companyToDto(savedBranch.getCompany()));

        apiResponse.setData(savedBranchDTO);
        apiResponse.setMessage("Branch created successfully");
        apiResponse.setStatusCode(201);
        apiResponse.setStatus(true);
        return apiResponse;
    }

    public ApiResponse<List<BranchDTO>> getBranchesByPharmacyId(Long pharmacyId) {
        ApiResponse<List<BranchDTO>> apiResponse = new ApiResponse<>();

        Optional<Company> pharmacy = companyRepository.findById(pharmacyId);
        if (pharmacy.isEmpty()) {
            apiResponse.setMessage("Pharmacy not found");
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            return apiResponse;
        }

        Optional<List<Branch>> optionalBranches = branchRepository.findBranchByCompany(pharmacy.get());
        if (optionalBranches.isPresent()) {
            List<BranchDTO> branchDTOs = branchListToDtoList(optionalBranches.get());
            apiResponse.setData(branchDTOs);
            apiResponse.setMessage("Branches found");
            apiResponse.setStatusCode(200);
            apiResponse.setStatus(true);
            return apiResponse;
        }

        apiResponse.setMessage("No branches found");
        apiResponse.setStatusCode(404);
        apiResponse.setStatus(false);
        return apiResponse;
    }

    public ApiResponse<BranchDTO> updateBranch(Long branchId, BranchDTO branchDTO, String token) {
        ApiResponse<BranchDTO> apiResponse = new ApiResponse<>();
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);

        if (optionalBranch.isEmpty()) {
            apiResponse.setMessage("Branch ID not found");
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            return apiResponse;  // Return early if branch is not found
        }

        Branch dbBranch = optionalBranch.get();
        Long userId = jwtService.extractUserId(token);

        // Authorization check
        if (!checkAuth.checkAuthToCompany(userId, dbBranch.getCompany().getCompanyId())) {
            apiResponse.setMessage("Unauthorized");
            apiResponse.setStatusCode(401);
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Check for duplicate branch name within the same company
        if (!dbBranch.getBranchName().equals(branchDTO.getBranchName())) {
            if (checkBranchName(branchDTO.getBranchName(), dbBranch.getCompany())) {
                apiResponse.setMessage("Branch name is already used");
                apiResponse.setStatusCode(400);
                apiResponse.setStatus(false);
                return apiResponse;
            }
        }

        // Update fields only if provided
        if (Objects.nonNull(branchDTO.getBranchName()) && !branchDTO.getBranchName().isEmpty()) {
            dbBranch.setBranchName(branchDTO.getBranchName());
        }
        if (branchDTO.getBranchState() != null) {
            dbBranch.setBranchState(branchDTO.getBranchState());
        }
        if (branchDTO.getEmail() != null && !branchDTO.getEmail().isEmpty()) {
            dbBranch.setEmail(branchDTO.getEmail());
        }
        if (branchDTO.getCity() != null && !branchDTO.getCity().isEmpty()) {
            dbBranch.setCity(branchDTO.getCity());
        }
        if (branchDTO.getAddress() != null && !branchDTO.getAddress().isEmpty()) {
            dbBranch.setAddress(branchDTO.getAddress());
        }
//        if (branchDTO.getLng() != 0) {
//            dbBranch.setLng(branchDTO.getLng());
//        }
//        if (branchDTO.getLat() != 0) {
//            dbBranch.setLat(branchDTO.getLat());
//        }
        if (branchDTO.getZip() != null && !branchDTO.getZip().isEmpty()) {
            dbBranch.setZip(branchDTO.getZip());
        }
        if (branchDTO.getPhone() != null && !branchDTO.getPhone().isEmpty()) {
            dbBranch.setPhone(branchDTO.getPhone());
        }

        // Save the updated branch entity
        Branch updatedBranch = branchRepository.save(dbBranch);
        BranchDTO updatedBranchDTO = branchToDto(updatedBranch);

        apiResponse.setData(updatedBranchDTO);
        apiResponse.setMessage("Branch updated successfully");
        apiResponse.setStatusCode(200);
        apiResponse.setStatus(true);
        return apiResponse;
    }


    public ApiResponse<BranchDTO> deleteBranch(Long branchId, String token) {
        ApiResponse<BranchDTO> apiResponse = new ApiResponse<>();
        Long userId = jwtService.extractUserId(token);

        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()) {
            apiResponse.setMessage("Branch not found");
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            return apiResponse;
        }

        Branch branch = optionalBranch.get();

        // Assuming every branch has an associated company, this check might be redundant
        if (branch.getCompany() == null || branch.getCompany().getCompanyId() == null) {
            apiResponse.setMessage("Company not found");
            apiResponse.setStatusCode(404);
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Authorization check
        if (!checkAuth.checkAuthToCompany(userId, branch.getCompany().getCompanyId())) {
            apiResponse.setMessage("Unauthorized");
            apiResponse.setStatusCode(401);
            apiResponse.setStatus(false);
            return apiResponse;
        }

        // Delete related inventory drugs and the branch itself
        inventoryDrugRepository.deleteAllByBranch_BranchId(branchId);
        branchRepository.delete(branch);

        apiResponse.setMessage("Branch deleted successfully");
        apiResponse.setStatusCode(200);
        apiResponse.setStatus(true);
        return apiResponse;
    }

    public ApiResponse<List<InventoryDrug>> getBranchDrugs(Long branchId) {
        ApiResponse<List<InventoryDrug>> apiResponse = new ApiResponse<>();
        if (!branchRepository.existsById(branchId)) {
            apiResponse.setStatusCode(200);
            apiResponse.setData(null);
            apiResponse.setMessage("Branch not found");
            apiResponse.setStatus(false);
            return apiResponse;
        }
        apiResponse.setData(inventoryDrugRepository.findAllByBranch_BranchId(branchId));
        apiResponse.setStatusCode(200);
        apiResponse.setMessage("drugs found");
        apiResponse.setStatus(true);


        return apiResponse;

    }
}
