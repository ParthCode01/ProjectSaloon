package com.parth.saloonmanagement.dto;


import com.parth.saloonmanagement.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponse {

    private Long id;
    private BookingStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String treatmentName;
    private String stylistName;

}
