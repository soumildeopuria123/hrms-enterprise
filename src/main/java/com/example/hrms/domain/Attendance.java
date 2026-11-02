package com.example.hrms.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "punch_date", nullable = false)
    private LocalDate punchDate;

    @Column(name = "clock_in_time")
    private LocalTime clockInTime;

    @Column(name = "clock_out_time")
    private LocalTime clockOutTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private AttendanceStatus status = AttendanceStatus.PRESENT;

    @Column(name = "source", length = 20)
    private String source;

    @Column(name = "notes", length = 500)
    private String notes;

    public Attendance() {}

    public Attendance(Employee employee, LocalDate punchDate, LocalTime clockInTime, String source) {
        this.employee = employee;
        this.punchDate = punchDate;
        this.clockInTime = clockInTime;
        this.source = source;
        this.status = AttendanceStatus.PRESENT;
    }

    public String displayLabel() {
        return employee.getFullName() + " — " + punchDate;
    }

    // --- accessors ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public LocalDate getPunchDate() { return punchDate; }
    public void setPunchDate(LocalDate punchDate) { this.punchDate = punchDate; }

    public LocalTime getClockInTime() { return clockInTime; }
    public void setClockInTime(LocalTime clockInTime) { this.clockInTime = clockInTime; }

    public LocalTime getClockOutTime() { return clockOutTime; }
    public void setClockOutTime(LocalTime clockOutTime) { this.clockOutTime = clockOutTime; }

    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}

enum AttendanceStatus {
    PRESENT,
    LATE,
    ABSENT,
    HALF_DAY,
    ON_LEAVE
}
