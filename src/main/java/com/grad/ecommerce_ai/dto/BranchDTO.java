package com.grad.ecommerce_ai.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class BranchDTO {

    private Long branchId;
    @NotBlank
    private String branchName;
    @NotBlank
    private String address;
    @NotBlank
    private String city;

    private Boolean branchState;

    private String zip;
    @NotBlank
    private String phone;
    @NotBlank
    private String email;

    private double lat;

    private double lng;
    private CompanyDTO companyDto;

}
