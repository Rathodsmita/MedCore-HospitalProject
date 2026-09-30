package com.medcore;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface DepartmentRepository extends JpaRepository<Department, Long> {}

interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByDepartment(String department);
    java.util.Optional<Doctor> findByName(String name);
}

interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    long countByDoctorAndAppointmentDateAndStatusNot(String doctor, LocalDate date, String status);
    List<Appointment> findAllByOrderByCreatedAtDesc();
}
