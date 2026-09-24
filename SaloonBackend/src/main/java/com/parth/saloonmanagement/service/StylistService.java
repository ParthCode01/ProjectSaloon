package com.parth.saloonmanagement.service;


import com.parth.saloonmanagement.dto.BookingResponse;
import com.parth.saloonmanagement.dto.StylistRequest;
import com.parth.saloonmanagement.dto.StylistResponse;
import com.parth.saloonmanagement.entity.*;
import com.parth.saloonmanagement.exception.AccessDeniedException;
import com.parth.saloonmanagement.exception.ResourceNotFoundException;
import com.parth.saloonmanagement.repository.StylistRepository;
import com.parth.saloonmanagement.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StylistService {


    private final StylistRepository stylistRepository;
    private final UserRepository userRepository;


    public StylistService(UserRepository userRepository, StylistRepository stylistRepository) {
        this.userRepository = userRepository;
        this.stylistRepository = stylistRepository;
    }


    public StylistResponse createStylist(StylistRequest request) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tenant tenant = user.getTenant();

        Stylist stylist = new Stylist();

        stylist.setName(request.getName());
        stylist.setContact(request.getContact());
        stylist.setSkills(request.getSkills());
        stylist.setTenant(tenant);
        stylist.setWorkStartTime(request.getWorkingStartTime());
        stylist.setWorkEndTime(request.getWorkingEndTime());

        Stylist savedStylist = stylistRepository.save(stylist);

        return new StylistResponse(
                savedStylist.getId(),
                savedStylist.getName(),
                savedStylist.getContact(),
                savedStylist.getSkills()
        );
    }


    public List<StylistResponse> getStylists(){

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tenant tenant = user.getTenant();

        List<Stylist>stylists = stylistRepository.findByTenant(tenant);

        List<StylistResponse> responses = new ArrayList<>();

        for(Stylist stylist : stylists){
            StylistResponse response = new StylistResponse(
                    stylist.getId(),
                    stylist.getName(),
                    stylist.getContact(),
                    stylist.getSkills()
            );

        responses.add(response);
        }
        return responses;

    }


    public StylistResponse updateStylist(Long id , StylistRequest request){
        User user = userRepository.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tenant tenant = user.getTenant();

        Stylist stylist = stylistRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new ResourceNotFoundException("Stylist not found"));

        stylist.setName(request.getName());
        stylist.setContact(request.getContact());
        stylist.setSkills(request.getSkills());

        Stylist savedStylist = stylistRepository.save(stylist);

        return new StylistResponse(
                savedStylist.getId(),
                savedStylist.getName(),
                savedStylist.getContact(),
                savedStylist.getSkills()
        );

    }


    public void deleteStylist(Long id){
        User user = userRepository.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tenant tenant = user.getTenant();

        Stylist stylist = stylistRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new ResourceNotFoundException("Stylist not found"));

        stylistRepository.delete(stylist);
    }


    public List<BookingResponse> getMyBookings()
    throws AccessDeniedException{


        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getRole().equals(Role.ROLE_STYLIST)) {
            throw new AccessDeniedException("Not allowed");
        }
        Tenant tenant = user.getTenant();

        Stylist stylist = stylistRepository.findByUserAndTenant(user, tenant)
                .orElseThrow(() -> new ResourceNotFoundException("Stylist not found"));


        List<Booking> bookings = stylist.getBookings();

        List<BookingResponse> responses = new ArrayList<>();

        for (Booking booking : bookings) {

            BookingResponse response = BookingResponse.builder()
                    .id(booking.getId())
                    .status(booking.getStatus())
                    .startTime(booking.getStartTime())
                    .endTime(booking.getEndTime())
                    .treatmentName(booking.getTreatment() != null ? booking.getTreatment().getName() : null)
                    .stylistName(stylist.getName())
                    .build();

            responses.add(response);
        }

        return responses;
    }
}