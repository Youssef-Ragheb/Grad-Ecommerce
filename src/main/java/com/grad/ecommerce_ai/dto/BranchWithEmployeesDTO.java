package com.grad.ecommerce_ai.dto;

import com.grad.ecommerce_ai.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class BranchWithEmployeesDTO {
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
   
    @Size(max = 1080)
    private String CompanyLogoURl;
    private List<User> employees;

}
