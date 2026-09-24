package com.parth.saloonmanagement.controller;


import com.parth.saloonmanagement.dto.BookingResponse;
import com.parth.saloonmanagement.dto.StylistRequest;
import com.parth.saloonmanagement.dto.StylistResponse;
import com.parth.saloonmanagement.exception.AccessDeniedException;
import com.parth.saloonmanagement.service.StylistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stylists")
public class StylistController {

    private final StylistService stylistService;

    public StylistController(StylistService  stylistService ) {
        this.stylistService  = stylistService;
    }

    @GetMapping
    public List<StylistResponse>getStylists(){
        return stylistService.getStylists();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StylistResponse createStylist(@Valid @RequestBody StylistRequest request){
        return stylistService.createStylist(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStylist(@PathVariable Long id){
        stylistService.deleteStylist(id);
    }

    @PutMapping("/{id}")
    public StylistResponse updateStylist(@PathVariable Long id , @Valid @RequestBody StylistRequest request){
        return stylistService.updateStylist(id , request);
    }


    @GetMapping("/me/bookings")
    public ResponseEntity<List<BookingResponse>> getMyBookings() throws AccessDeniedException {

        List<BookingResponse> bookings = stylistService.getMyBookings();

        return ResponseEntity.ok(bookings);
    }
}
