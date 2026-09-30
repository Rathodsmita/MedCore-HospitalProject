package com.medcore;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PublicController {
    private final DepartmentRepository departments;
    private final DoctorRepository doctors;
    private final AppointmentService service;

    PublicController(DepartmentRepository dep, DoctorRepository doc, AppointmentService s) {
        this.departments = dep; this.doctors = doc; this.service = s;
    }

    @GetMapping("/health")
    public Map<String, Boolean> health() { return Map.of("ok", true); }

    @GetMapping("/departments")
    public List<String> departments() { return departments.findAll().stream().map(Department::getName).toList(); }

    @GetMapping("/doctors")
    public List<Doctor> doctors(@RequestParam(required = false) String department) {
        return department == null || department.isBlank() ? doctors.findAll() : doctors.findByDepartment(department);
    }

    @PostMapping("/appointments")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse book(@RequestBody BookingRequest request) { return service.book(request); }
}
