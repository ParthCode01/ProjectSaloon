package com.parth.saloonmanagement.repository;

import com.parth.saloonmanagement.entity.Tenant;
import com.parth.saloonmanagement.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    List<Treatment>findByTenant(Tenant tenant);
    Optional<Treatment> findByIdAndTenant(Long id, Tenant tenant);
}
