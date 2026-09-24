package com.parth.saloonmanagement.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NotNull(message = "Stylist id is required")
    private Long stylistId;

    @NotNull(message = "Treatment id is required")
    private Long treatmentId;

    @NotNull(message = "Start time is required")
    @Future
    private LocalDateTime startTime;

}
