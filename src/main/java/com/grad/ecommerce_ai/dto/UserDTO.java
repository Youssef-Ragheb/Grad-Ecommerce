package com.grad.ecommerce_ai.dto;

import com.grad.ecommerce_ai.entity.Gender;
import com.grad.ecommerce_ai.entity.UserRoles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Date;

@Data
public class UserDTO {

    private Long id;
    @NotBlank
    @Size(max = 255)
    private String email;

    @NotBlank
    private String password;
    @NotBlank
    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @Size(max = 255)
    private String phone;

    @Size(max = 255)
    private String address;

    @Size(max = 255)
    private String city;

    private Gender gender;

    private OffsetDateTime dateCreated;

    private OffsetDateTime lastLoginDate;

    private boolean enabled;
    private UserRoles userRoles;
    private Date birthDate;
    private boolean companyRegistrationCompleted = false;
}
