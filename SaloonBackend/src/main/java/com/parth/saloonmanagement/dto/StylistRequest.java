package com.parth.saloonmanagement.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StylistRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Size(min = 10 , max = 10 , message = "Valid Contact number is required")
    private String contact;

    @ElementCollection
    private List<String> skills;

    private LocalTime workingStartTime;
    private LocalTime workingEndTime;



}
