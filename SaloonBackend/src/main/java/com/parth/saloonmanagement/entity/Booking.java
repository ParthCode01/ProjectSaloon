package com.parth.saloonmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Treatment treatment;

    @ManyToOne
    private Stylist stylist;

    @ManyToOne
    private Tenant tenant;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;



}
