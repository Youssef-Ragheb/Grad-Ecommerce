package com.grad.ecommerce_ai.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Size(max = 255)
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @Size(max = 255)
    @NotBlank
    private String password;

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

    @Enumerated(EnumType.STRING)
    private Gender gender;
    @CreatedDate
    @Column(name = "date_created", columnDefinition = "DATETIME(6)")
    private OffsetDateTime dateCreated;

    private OffsetDateTime lastLoginDate;
//    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy")
//    private Date birthDate;
    private boolean enabled;
    private UserRoles userRoles;
    private boolean companyRegistrationCompleted = false;



}
