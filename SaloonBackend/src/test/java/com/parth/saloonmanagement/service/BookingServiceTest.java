package com.parth.saloonmanagement.service;

import com.parth.saloonmanagement.dto.BookingRequest;
import com.parth.saloonmanagement.dto.BookingResponse;
import com.parth.saloonmanagement.entity.Booking;
import com.parth.saloonmanagement.entity.BookingStatus;
import com.parth.saloonmanagement.entity.Stylist;
import com.parth.saloonmanagement.entity.Tenant;
import com.parth.saloonmanagement.entity.Treatment;
import com.parth.saloonmanagement.entity.User;
import com.parth.saloonmanagement.exception.BookingConflictException;
import com.parth.saloonmanagement.repository.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TreatmentRepository treatmentRepository;

    @Mock
    private StylistRepository stylistRepository;

    @InjectMocks
    private BookingService bookingService;


    // TEST 1
    @Test
    void createBooking_shouldCreateBookingSuccessfully() {

        // Logged-in user
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName())
                .thenReturn("customer@gmail.com");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);


        // Tenant
        Tenant tenant = new Tenant();

        tenant.setId(1L);
        tenant.setName("Test Saloon");


        // User
        User user = new User();

        user.setId(1L);
        user.setEmail("customer@gmail.com");
        user.setTenant(tenant);

        when(userRepository.findByEmail("customer@gmail.com"))
                .thenReturn(Optional.of(user));


        // Treatment
        Treatment treatment = new Treatment();

        treatment.setId(1L);
        treatment.setDurationMinutes(60);
        treatment.setTenant(tenant);

        when(treatmentRepository.findByIdAndTenant(1L, tenant))
                .thenReturn(Optional.of(treatment));


        // Stylist
        Stylist stylist = new Stylist();
        stylist.setId(1L);
        stylist.setTenant(tenant);
        stylist.setWorkStartTime(LocalTime.of(9, 0));  // ADD
        stylist.setWorkEndTime(LocalTime.of(18, 0));   // ADD

        when(stylistRepository.findByIdAndTenantForUpdate(1L, tenant))
                .thenReturn(Optional.of(stylist));


        // Start time
        LocalDateTime startTime = LocalDateTime.now()
                .plusDays(1)
                .withHour(10)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);


        // Request
        BookingRequest request = new BookingRequest(
                1L,
                1L,
                startTime
        );


        // No stylist conflict
        when(bookingRepository
                .findByStylistAndStartTimeLessThanAndEndTimeGreaterThanAndStatusIn(
                        stylist,
                        startTime.plusMinutes(60),
                        startTime,
                        List.of(
                                BookingStatus.PENDING,
                                BookingStatus.CONFIRMED
                        )
                ))
                .thenReturn(List.of());


        // No customer conflict
        when(bookingRepository
                .findByUserAndStartTimeLessThanAndEndTimeGreaterThanAndStatusIn(
                        user,
                        startTime.plusMinutes(60),
                        startTime,
                        List.of(
                                BookingStatus.PENDING,
                                BookingStatus.CONFIRMED
                        )
                ))
                .thenReturn(List.of());


        // Save
        Booking savedBooking = new Booking();

        savedBooking.setId(1L);

        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(savedBooking);


        // Execute
        BookingResponse response =
                bookingService.createBooking(request);


        // Verify
        assertNotNull(response);
        assertEquals(1L, response.getId());

        verify(bookingRepository)
                .save(any(Booking.class));
    }


    // TEST 2
    @Test
    void createBooking_shouldThrowExceptionWhenStylistAlreadyBooked() {

        // Logged-in user
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName())
                .thenReturn("customer@gmail.com");

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);


        // Tenant
        Tenant tenant = new Tenant();

        tenant.setId(1L);
        tenant.setName("Test Saloon");


        // User
        User user = new User();

        user.setId(1L);
        user.setEmail("customer@gmail.com");
        user.setTenant(tenant);

        when(userRepository.findByEmail("customer@gmail.com"))
                .thenReturn(Optional.of(user));


        // Treatment
        Treatment treatment = new Treatment();

        treatment.setId(1L);
        treatment.setDurationMinutes(60);
        treatment.setTenant(tenant);

        when(treatmentRepository.findByIdAndTenant(1L, tenant))
                .thenReturn(Optional.of(treatment));


        // Stylist
        Stylist stylist = new Stylist();

        stylist.setId(1L);
        stylist.setTenant(tenant);
        stylist.setWorkStartTime(LocalTime.of(9, 0));
        stylist.setWorkEndTime(LocalTime.of(18, 0));

        when(stylistRepository.findByIdAndTenantForUpdate(1L, tenant))
                .thenReturn(Optional.of(stylist));


        // Start time
        LocalDateTime startTime = LocalDateTime.now()
                .plusDays(1)
                .withHour(10)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);


        // End time
        LocalDateTime endTime =
                startTime.plusMinutes(60);


        // Request
        BookingRequest request = new BookingRequest(
                1L,
                1L,
                startTime
        );


        // Existing booking = conflict
        Booking existingBooking = new Booking();

        when(bookingRepository
                .findByStylistAndStartTimeLessThanAndEndTimeGreaterThanAndStatusIn(
                        stylist,
                        endTime,
                        startTime,
                        List.of(
                                BookingStatus.PENDING,
                                BookingStatus.CONFIRMED
                        )
                ))
                .thenReturn(List.of(existingBooking));


        // Execute + Verify
        assertThrows(
                BookingConflictException.class,
                () -> bookingService.createBooking(request)
        );


        // Make sure booking was NOT saved
        verify(bookingRepository, never())
                .save(any(Booking.class));
    }
}