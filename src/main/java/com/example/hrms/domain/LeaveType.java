package com.example.hrms.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "leave_types")
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Annual, Sick, Personal

    @Column(columnDefinition = "boolean default true")
    private boolean paid;

    @Column(name = "requires_approval", columnDefinition = "boolean default true")
    private boolean requiresApproval;

    // Getters, setters, constructors omitted for brevity
}