package com.parth.saloonmanagement.repository;


import com.parth.saloonmanagement.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking>findByStylistAndStartTimeLessThanAndEndTimeGreaterThanAndStatusIn(
      Stylist stylist, LocalDateTime endTime ,
      LocalDateTime startTime,
      List<BookingStatus>statuses
    );

    List<Booking> findByUserAndStartTimeLessThanAndEndTimeGreaterThanAndStatusIn(
            User user,
            LocalDateTime startTimeIsLessThan,
            LocalDateTime endTimeIsGreaterThan,
            List<BookingStatus> statuses
    );

    List<Booking> findByTenant(Tenant tenant);
    List<Booking> findByUser(User user);

    Optional<Booking> findByIdAndTenant(Long id, Tenant tenant);

    Optional<Booking> findByIdAndUser(Long id, User user);

    List<Booking> findByUserId(Long id);
}
