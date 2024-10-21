package com.grad.ecommerce_ai.mappers;

import com.grad.ecommerce_ai.dto.BranchDTO;
import com.grad.ecommerce_ai.dto.CompanyDTO;
import com.grad.ecommerce_ai.enitity.Branch;
import com.grad.ecommerce_ai.enitity.Company;

import java.util.List;
import java.util.stream.Collectors;

public class DtoConverter {

    public static Branch dtoToBranch(BranchDTO dto) {
        Branch branch = new Branch();

        branch.setBranchId(dto.getBranchId());
        branch.setBranchName(dto.getBranchName());
        branch.setAddress(dto.getAddress());
        branch.setCity(dto.getCity());
        branch.setPhone(dto.getPhone());
        branch.setEmail(dto.getEmail());
        branch.setBranchState(dto.getBranchState());
        branch.setZip(dto.getZip());
        branch.setLat(dto.getLat());
        branch.setLng(dto.getLng());
        return branch;
    }

    public static List<BranchDTO> branchListToDtoList(List<Branch> branchList) {
        return branchList.stream().map(DtoConverter::branchToDto).collect(Collectors.toList());
    }

    public static List<CompanyDTO> companyListToDtoList(List<Company> companyList) {
        return companyList.stream().map(DtoConverter::companyToDto).collect(Collectors.toList());
    }

    public static BranchDTO branchToDto(Branch branch) {
        BranchDTO dto = new BranchDTO();

        dto.setBranchId(branch.getBranchId());
        dto.setBranchName(branch.getBranchName());
        dto.setAddress(branch.getAddress());
        dto.setCity(branch.getCity());
        dto.setPhone(branch.getPhone());
        dto.setEmail(branch.getEmail());
        dto.setBranchState(branch.getBranchState());
        dto.setZip(branch.getZip());
        dto.setLat(branch.getLat());
        dto.setLng(branch.getLng());

        return dto;
    }

//    public static BranchDTO branchToBranchWithCompanyDto(Branch branch) {
//        BranchDTO dto = branchToDto(branch);
//
//        if (branch.getCompany() != null) {
//            dto.setCompanyDto(companyToDto(branch.getCompany()));
//        }
//
//        return dto;
//    }
//
//    public static Company dtoToCompany(CompanyDTO companyDto) {
//        Company company = new Company();
//
//        company.setCompanyId(companyDto.getCompanyId());
//        company.setName(companyDto.getName());
//        company.setCompanyEmail(companyDto.getCompanyEmail());
//        company.setPhone(companyDto.getPhone());
//        company.setLogoUrl(companyDto.getLogoUrl());
//
//        return company;
//    }

    public static CompanyDTO companyToDto(Company company) {
        CompanyDTO dto = new CompanyDTO();

        dto.setCompanyId(company.getCompanyId());
        dto.setName(company.getName());
        dto.setCompanyEmail(company.getCompanyEmail());
        dto.setPhone(company.getPhone());
        dto.setLogoUrl(company.getLogoUrl());

        return dto;
    }

//    public static CompanyDTO companyToCompanyWithBranchesDto(Company company) {
//        CompanyDTO dto = companyToDto(company);
//
//        if (company.getBranchList() != null) {
//            dto.setBranchList(branchListToDtoList(company.getBranchList()));
//        }
//
//        return dto;
//    }
}



