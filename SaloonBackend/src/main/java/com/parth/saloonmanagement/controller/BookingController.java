package com.parth.saloonmanagement.controller;

import com.parth.saloonmanagement.dto.BookingRequest;
import com.parth.saloonmanagement.dto.BookingResponse;
import com.parth.saloonmanagement.dto.BookingStatusRequest;
import com.parth.saloonmanagement.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.parth.saloonmanagement.exception.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        BookingResponse response = bookingService.getBookingById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BookingResponse>updateBookingStatus(
            @PathVariable Long id ,
            @RequestBody BookingStatusRequest request
            ) throws AccessDeniedException {

        BookingResponse response = bookingService.updateBookingStatus(id , request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookingResponse> deleteBooking(@PathVariable Long id) throws AccessDeniedException {
        BookingResponse response = bookingService.deleteBooking(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserId() {
        return ResponseEntity.ok(bookingService.getMyUserBookings());
    }


    @PreAuthorize("hasRole('OWNER')")
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getMyTenantBookings() {
        return ResponseEntity.ok(bookingService.getMyTenantBookings());
    }

    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmBooking(@PathVariable Long id) {
        bookingService.confirmBooking(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/{id}/completed")
    public ResponseEntity<Void> completeBooking(@PathVariable Long id) {
        bookingService.completeBooking(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('OWNER', 'CUSTOMER')")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok().build();
    }

    }
