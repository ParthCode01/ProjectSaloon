package com.parth.saloonmanagement.controller;

import com.parth.saloonmanagement.dto.TenantSummaryResponse;
import com.parth.saloonmanagement.dto.TreatmentResponse;
import com.parth.saloonmanagement.service.TenantService;
import com.parth.saloonmanagement.service.TreatmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;
    private final TreatmentService treatmentService;

    public TenantController(
            TenantService tenantService,
            TreatmentService treatmentService) {

        this.tenantService = tenantService;
        this.treatmentService = treatmentService;
    }

    @GetMapping
    public List<TenantSummaryResponse> getTenantSummary() {
        return tenantService.getTenantSummary();
    }
    @GetMapping("/{tenantId}")
    public TenantSummaryResponse getTenantById(@PathVariable Long tenantId) {
        return tenantService.getTenantById(tenantId);
    }

    @GetMapping("/{tenantId}/treatments")
    public List<TreatmentResponse> getTreatmentsByTenant(@PathVariable Long tenantId) {
        return treatmentService.getTreatmentsByTenant(tenantId);
    }
}