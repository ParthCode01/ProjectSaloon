package com.parth.saloonmanagement.service;

import com.parth.saloonmanagement.dto.TenantSummaryResponse;
import com.parth.saloonmanagement.entity.Tenant;
import com.parth.saloonmanagement.repository.TenantRepository;
import org.springframework.stereotype.Service;
import com.parth.saloonmanagement.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TenantService {

    private  final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public List<TenantSummaryResponse> getTenantSummary(){
        List<Tenant> tenants =  tenantRepository.findAll();

        List<TenantSummaryResponse> responses=new ArrayList<>();

        for(Tenant tenant: tenants){
            TenantSummaryResponse response = new TenantSummaryResponse(
                    tenant.getId(),
                    tenant.getName(),
                    tenant.getAddress()
            );

            responses.add(response);
        }

        return responses;
    }

    public TenantSummaryResponse getTenantById(Long tenantId) {

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));

        return new TenantSummaryResponse(
                tenant.getId(),
                tenant.getName(),
                tenant.getAddress()
        );
    }
}
