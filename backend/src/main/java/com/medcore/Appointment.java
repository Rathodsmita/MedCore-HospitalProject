package com.medcore;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "appointments", indexes = @Index(name = "idx_doctor_date", columnList = "doctor,appointmentDate"))
public class Appointment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 20) private String reference;
    @Column(nullable = false, length = 100) private String name;
    @Column(nullable = false, length = 20) private String phone;
    @Column(nullable = false, length = 100) private String department;
    @Column(nullable = false, length = 100) private String doctor;
    @Column(nullable = false) private LocalDate appointmentDate;
    @Column(nullable = false, length = 20) private String status = "pending";
    @Column(nullable = false) private Instant createdAt = Instant.now();

    protected Appointment() {}
    public Appointment(String reference, String name, String phone, String department, String doctor, LocalDate date) {
        this.reference = reference; this.name = name; this.phone = phone;
        this.department = department; this.doctor = doctor; this.appointmentDate = date;
    }
    public Long getId() { return id; }
    public String getReference() { return reference; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getDepartment() { return department; }
    public String getDoctor() { return doctor; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setStatus(String status) { this.status = status; }
}
