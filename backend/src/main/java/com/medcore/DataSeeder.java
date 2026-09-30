package com.medcore;

import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Inserts the departments and doctors on first run (only if the tables are empty).
@Component
public class DataSeeder implements CommandLineRunner {
    private final DepartmentRepository departments;
    private final DoctorRepository doctors;

    DataSeeder(DepartmentRepository d, DoctorRepository doc) { this.departments = d; this.doctors = doc; }

    @Override
    public void run(String... args) {
        if (departments.count() == 0)
            departments.saveAll(List.of("Cardiology", "Neurology", "Orthopedics", "Pediatrics", "Dental Care", "Diagnostics")
                .stream().map(Department::new).toList());
        if (doctors.count() == 0)
            doctors.saveAll(List.of(
                new Doctor("Dr. Priya Sharma", "Cardiologist", "Cardiology", "12+ Years Experience"),
                new Doctor("Dr. Rahul Mehta", "Neurologist", "Neurology", "10+ Years Experience"),
                new Doctor("Dr. Sneha Patil", "Orthopedic Surgeon", "Orthopedics", "8+ Years Experience"),
                new Doctor("Dr. Amit Deshmukh", "Pediatrician", "Pediatrics", "7+ Years Experience")));
    }
}
