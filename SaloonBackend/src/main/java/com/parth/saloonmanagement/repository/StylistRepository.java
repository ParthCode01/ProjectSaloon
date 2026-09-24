package com.parth.saloonmanagement.repository;

import com.parth.saloonmanagement.entity.Stylist;
import com.parth.saloonmanagement.entity.Tenant;
import com.parth.saloonmanagement.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PessimisticLockScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

public interface StylistRepository extends JpaRepository<Stylist, Long> {

    Optional<Stylist> findByIdAndTenant(Long id, Tenant tenant);

    Optional<Stylist> findByUserAndTenant(User user, Tenant tenant);

    List<Stylist> findByTenant(Tenant tenant);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT s FROM Stylist s
    WHERE s.id = :id
    AND s.tenant = :tenant
""")
    Optional<Stylist> findByIdAndTenantForUpdate(
            @Param("id") Long id,
            @Param("tenant") Tenant tenant
    );
}