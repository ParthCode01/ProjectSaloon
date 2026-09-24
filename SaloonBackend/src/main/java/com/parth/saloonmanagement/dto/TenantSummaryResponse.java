package com.parth.saloonmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantSummaryResponse {

    private Long id;
    private String name;
    private String address;
}