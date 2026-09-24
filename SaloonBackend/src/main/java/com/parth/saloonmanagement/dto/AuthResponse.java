package com.parth.saloonmanagement.dto;


import com.parth.saloonmanagement.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;

    private String email;

    private long tenantId;

    private Role role;

}
