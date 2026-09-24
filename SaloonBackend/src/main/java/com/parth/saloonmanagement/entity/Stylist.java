package com.parth.saloonmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Stylist{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Size(min = 10 , max = 10 , message = "Valid Contact number is required")
    private String contact;

    @ElementCollection
    private List<String> skills;

    @OneToOne
    private User user;

    @ManyToOne
    private Tenant tenant;

    @OneToMany(mappedBy = "stylist")
    private List<Booking> bookings;

    private LocalTime workStartTime;
    private LocalTime workEndTime;


}
