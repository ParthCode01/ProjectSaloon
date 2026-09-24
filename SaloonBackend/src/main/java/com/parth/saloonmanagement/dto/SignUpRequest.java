package com.parth.saloonmanagement.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

@Data
public class SignUpRequest {

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Salon name is required")
    private String tenantName;

    @NotBlank(message = "Salon address is required")
    private String tenantAddress;

    @Size(min =10 , max = 10 , message = "Contact number must be of 10 digits")
    private String contact;

}