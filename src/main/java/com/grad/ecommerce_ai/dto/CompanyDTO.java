package com.grad.ecommerce_ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CompanyDTO {

        private Long companyId;
        @Size(max = 255)
        @NotBlank
        private String name;
        @Size(max = 255)
        @NotBlank
        private String companyEmail;
        private String phone;
        public String logoUrl;

        private List<BranchDTO> branchList;

}
