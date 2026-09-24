package com.parth.saloonmanagement.controller;


import com.parth.saloonmanagement.dto.TreatmentRequest;
import com.parth.saloonmanagement.dto.TreatmentResponse;
import com.parth.saloonmanagement.service.TreatmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentController {

    private final TreatmentService treatmentService;

    public TreatmentController(TreatmentService treatmentService) {

        this.treatmentService = treatmentService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public TreatmentResponse createTreatment( @Valid @RequestBody TreatmentRequest request){
        return treatmentService.createTreatment(request);
    }

    @GetMapping
    public List<TreatmentResponse> getTreatments() {
        return treatmentService.getTreatments();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTreatment(@PathVariable Long id) {
        treatmentService.deleteTreatment(id);
    }

    @PutMapping("/{id}")
    public TreatmentResponse updateTreatment(
            @PathVariable Long id,
            @Valid @RequestBody TreatmentRequest request) {

        return treatmentService.updateTreatment(id, request);
    }


}
