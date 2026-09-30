package com.medcore;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 100) private String name;
    @Column(nullable = false, length = 100) private String specialty;
    @Column(nullable = false, length = 100) private String department;
    @Column(length = 60) private String experience;

    protected Doctor() {}
    public Doctor(String name, String specialty, String department, String experience) {
        this.name = name; this.specialty = specialty; this.department = department; this.experience = experience;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public String getDepartment() { return department; }
    public String getExperience() { return experience; }
}
