package com.parth.saloonmanagement.service;


import com.parth.saloonmanagement.dto.TreatmentRequest;
import com.parth.saloonmanagement.dto.TreatmentResponse;
import com.parth.saloonmanagement.entity.Tenant;
import com.parth.saloonmanagement.entity.Treatment;
import com.parth.saloonmanagement.entity.User;
import com.parth.saloonmanagement.exception.ResourceNotFoundException;
import com.parth.saloonmanagement.repository.TreatmentRepository;
import com.parth.saloonmanagement.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final UserRepository userRepository;

    public TreatmentService(TreatmentRepository treatmentRepository, UserRepository userRepository) {
        this.treatmentRepository = treatmentRepository;
        this.userRepository = userRepository;
    }

    public TreatmentResponse createTreatment(TreatmentRequest request){


        String email = SecurityContextHolder.getContext().getAuthentication().getName();


            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            Tenant tenant = user.getTenant();
            Treatment treatment = new Treatment();

            treatment.setName(request.getName());
            treatment.setDurationMinutes(request.getDurationMinutes());
            treatment.setPrice(request.getPrice());
            treatment.setDescription(request.getDescription());
            treatment.setTenant(tenant);

            Treatment savedTreatment = treatmentRepository.save(treatment);

            return new TreatmentResponse(
                    savedTreatment.getId(),
                    savedTreatment.getName(),
                    savedTreatment.getDurationMinutes(),
                    savedTreatment.getPrice(),
                    savedTreatment.getDescription()
            );
        }



        public List<TreatmentResponse> getTreatments(){

        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tenant tenant = user.getTenant();

        List<Treatment>treatments = treatmentRepository.findByTenant(tenant);
        List<TreatmentResponse> responses = new ArrayList<>();

        for(Treatment treatment : treatments){
            TreatmentResponse response = new TreatmentResponse(
                    treatment.getId(),
                    treatment.getName(),
                    treatment.getDurationMinutes(),
                    treatment.getPrice(),
                    treatment.getDescription()
            );

            responses.add(response);
        }

        return responses;
        }


    public void deleteTreatment(Long id) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tenant tenant = user.getTenant();

        Treatment treatment = treatmentRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment not found"));

        treatmentRepository.delete(treatment);
    }


    public TreatmentResponse updateTreatment(Long id, TreatmentRequest request) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tenant tenant = user.getTenant();


        Treatment treatment = treatmentRepository.findByIdAndTenant(id, tenant)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment not found"));

        treatment.setName(request.getName());
        treatment.setDurationMinutes(request.getDurationMinutes());
        treatment.setPrice(request.getPrice());
        treatment.setDescription(request.getDescription());

        Treatment savedTreatment = treatmentRepository.save(treatment);

        return new TreatmentResponse(
                savedTreatment.getId(),
                savedTreatment.getName(),
                savedTreatment.getDurationMinutes(),
                savedTreatment.getPrice(),
                savedTreatment.getDescription()
        );

    }


    public List<TreatmentResponse> getTreatmentsByTenant(Long tenantId) {

        List<Treatment> treatments =
                treatmentRepository.findByTenantId(tenantId);

        List<TreatmentResponse> responses = new ArrayList<>();

        for (Treatment treatment : treatments) {

            TreatmentResponse response = new TreatmentResponse(
                    treatment.getId(),
                    treatment.getName(),
                    treatment.getDurationMinutes(),
                    treatment.getPrice(),
                    treatment.getDescription()
            );

            responses.add(response);
        }

        return responses;
    }
}
