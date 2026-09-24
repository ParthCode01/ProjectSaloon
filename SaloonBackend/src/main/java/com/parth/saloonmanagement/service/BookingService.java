package com.parth.saloonmanagement.service;


import com.parth.saloonmanagement.dto.BookingRequest;
import com.parth.saloonmanagement.dto.BookingResponse;
import com.parth.saloonmanagement.dto.BookingStatusRequest;
import com.parth.saloonmanagement.entity.*;
import com.parth.saloonmanagement.exception.BookingConflictException;
import com.parth.saloonmanagement.exception.ResourceNotFoundException;
import com.parth.saloonmanagement.repository.BookingRepository;
import com.parth.saloonmanagement.repository.StylistRepository;
import com.parth.saloonmanagement.repository.TreatmentRepository;
import com.parth.saloonmanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.parth.saloonmanagement.exception.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TreatmentRepository treatmentRepository;
    private final StylistRepository stylistRepository;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, TreatmentRepository treatmentRepository, StylistRepository stylistRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.treatmentRepository = treatmentRepository;
        this.stylistRepository = stylistRepository;
    }


    @Transactional
    public BookingResponse createBooking(BookingRequest bookingRequest){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Treatment treatment = treatmentRepository.findByIdAndTenant(bookingRequest.getTreatmentId(), user.getTenant())
                .orElseThrow(() -> new ResourceNotFoundException("Treatment not found"));

        Stylist stylist = stylistRepository.findByIdAndTenantForUpdate(bookingRequest.getStylistId(), user.getTenant())
                .orElseThrow(() -> new ResourceNotFoundException("Stylist not found"));


        LocalDateTime startTime = bookingRequest.getStartTime();
        LocalDateTime endTime = startTime.plusMinutes(treatment.getDurationMinutes());

        List<Booking> conflicts = bookingRepository
                .findByStylistAndStartTimeLessThanAndEndTimeGreaterThanAndStatusIn(
                        stylist ,
                        endTime ,
                        startTime,
                        List.of(BookingStatus.PENDING , BookingStatus.CONFIRMED));

        List<Booking> conflictOfUser =
                bookingRepository.findByUserAndStartTimeLessThanAndEndTimeGreaterThanAndStatusIn(
                        user,
                        endTime,
                        startTime,
                        List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED)
                );

        if(!conflicts.isEmpty()){
            throw new BookingConflictException("Stylist is already booked");
        }
        if(!conflictOfUser.isEmpty()){
            throw new BookingConflictException("User is already booked");
        }

        if (stylist.getWorkStartTime() != null && stylist.getWorkEndTime() != null) {
            if (startTime.toLocalTime().isBefore(stylist.getWorkStartTime())
                    || endTime.toLocalTime().isAfter(stylist.getWorkEndTime())) {
                throw new BookingConflictException("Booking is outside stylist working hours");
            }
        } else {
            throw new BookingConflictException("Stylist work hours not configured");
        }

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setStylist(stylist);
        booking.setTreatment(treatment);
        booking.setTenant(stylist.getTenant());
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);

        return BookingResponse.builder()
                .id(savedBooking.getId())
                .status(savedBooking.getStatus())
                .startTime(savedBooking.getStartTime())
                .endTime(savedBooking.getEndTime())
                .treatmentName(treatment.getName())
                .stylistName(stylist.getName())
                .build();

    }

    public BookingResponse deleteBooking(Long id)
            throws AccessDeniedException{
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tenant tenant = user.getTenant();
        Role role = user.getRole();

        Booking booking;
        if (role == Role.ROLE_OWNER) {

            booking = bookingRepository.findByIdAndTenant(id, tenant)
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        } else if (role == Role.ROLE_CUSTOMER) {

            booking = bookingRepository.findByIdAndUser(id, user)
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        } else {

            throw new AccessDeniedException("You are not allowed to delete booking");
        }



        BookingResponse response = BookingResponse.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .treatmentName(booking.getTreatment().getName())
                .stylistName(booking.getStylist().getName())
                .build();

        bookingRepository.delete(booking);
        return response;




    }

    public BookingResponse updateBookingStatus(Long id, BookingStatusRequest request)
            throws AccessDeniedException {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tenant tenant = user.getTenant();
        Role role = user.getRole();

        Booking booking;

        if (role == Role.ROLE_OWNER) {

            booking = bookingRepository.findByIdAndTenant(id, tenant)
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        } else if (role == Role.ROLE_CUSTOMER) {

            booking = bookingRepository.findByIdAndUser(id, user)
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        } else {

            throw new AccessDeniedException("You are not allowed to update booking status");
        }

        if (role == Role.ROLE_CUSTOMER
                && request.getStatus() != BookingStatus.CANCELLED) {

            throw new AccessDeniedException("Customer can only cancel bookings");
        }

        booking.setStatus(request.getStatus());

        bookingRepository.save(booking);

        return BookingResponse.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .treatmentName(booking.getTreatment().getName())
                .stylistName(booking.getStylist().getName())
                .build();
    }
    public BookingResponse getBookingById(Long id) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tenant tenant = user.getTenant();
        Role role = user.getRole();

        Booking booking;

        if (role == Role.ROLE_OWNER) {

            booking = bookingRepository.findByIdAndTenant(id, tenant)
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        } else if (role == Role.ROLE_CUSTOMER) {

            booking = bookingRepository.findByIdAndUser(id, user)
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        } else {
            throw new ResourceNotFoundException("Booking not found");
        }

        return BookingResponse.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .treatmentName(booking.getTreatment().getName())
                .stylistName(booking.getStylist().getName())
                .build();
    }



    public List<BookingResponse> getMyTenantBookings(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tenant tenant = user.getTenant();

        List<Booking> bookings = bookingRepository.findByTenant(tenant);

        List<BookingResponse>responses = new ArrayList<>();

        for(Booking booking : bookings){
            BookingResponse response = new BookingResponse(
                    booking.getId(),
                    booking.getStatus(),
                    booking.getStartTime(),
                    booking.getEndTime(),
                    booking.getTreatment().getName(),
                    booking.getStylist().getName()

            );

            responses.add(response);
        }
        return responses;


    }


    public List<BookingResponse> getMyUserBookings(){
     String email = SecurityContextHolder.getContext().getAuthentication().getName();

     User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


     List<Booking> bookings = bookingRepository.findByUserId(user.getId());

     List<BookingResponse>responses = new ArrayList<>();

     for(Booking booking : bookings){
         BookingResponse response = new BookingResponse(
                 booking.getId(),
                 booking.getStatus(),
                 booking.getStartTime(),
                 booking.getEndTime(),
                 booking.getTreatment().getName(),
                 booking.getStylist().getName()

         );

         responses.add(response);
     }
     return responses;


    }


    public void confirmBooking(Long id){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tenant tenant = user.getTenant();

        Booking booking = bookingRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));



        if(booking.getStatus() != BookingStatus.PENDING){
            throw new BookingConflictException("Only pending bookings can be confirmed");
        }

            booking.setStatus(BookingStatus.CONFIRMED);


        bookingRepository.save(booking);


    }

    public void completeBooking(Long id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tenant tenant = user.getTenant();

        Booking booking = bookingRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));


        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new BookingConflictException(
                    "Only confirmed bookings can be completed"
            );
        }

        if(user.getRole() == Role.ROLE_OWNER){
            booking.setStatus(BookingStatus.COMPLETED);
        }
        bookingRepository.save(booking);

    }

    public void cancelBooking(Long id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tenant tenant = user.getTenant();

        Booking booking = bookingRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));


        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingConflictException("Booking is already cancelled");
        }



        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BookingConflictException("Completed booking cannot be cancelled");
        }


        if (user.getRole() == Role.ROLE_OWNER) {
            booking.setStatus(BookingStatus.CANCELLED);
        }
        else if (user.getRole() == Role.ROLE_CUSTOMER
                && booking.getUser().getId().equals(user.getId())) {

            booking.setStatus(BookingStatus.CANCELLED);
        }
        else {
            throw new AccessDeniedException("You cannot cancel this booking");
        }

        bookingRepository.save(booking);


    }

}
