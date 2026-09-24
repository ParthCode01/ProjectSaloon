package com.parth.saloonmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentResponse {

    private long id;

    private String name;

    private Integer durationMinutes;

    private BigDecimal price;

    private String description;

}
