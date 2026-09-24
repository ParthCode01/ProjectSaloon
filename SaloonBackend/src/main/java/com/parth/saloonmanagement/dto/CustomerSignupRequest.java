package com.parth.saloonmanagement.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CustomerSignupRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Tenant ID is required")
    private Long tenantId;

    @NotBlank(message = "Contact is required")
    @Pattern(regexp = "\\d{10}", message = "Contact number must be of 10 digits")
    private String contact;
}
