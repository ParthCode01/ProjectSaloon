package com.parth.saloonmanagement.dto;

import com.parth.saloonmanagement.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingStatusRequest {

    private BookingStatus status;



}
