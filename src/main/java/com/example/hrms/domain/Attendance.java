package com.example.hrms.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @Column(name = "clock_in_at")
    private LocalTime clockInAt;

    @Column(name = "clock_out_at")
    private LocalTime clockOutAt;

    @Column(name = "source", length = 50)
    private String source; // web, mobile, kiosk

    @Column(name = "status", length = 20, nullable = false)
    private String status; // Present, Absent, Late, Half-Day

    // Getters, setters, constructors omitted for brevity
}