package com.medcore;

import java.util.List;
import org.springframework.web.bind.annotation.*;

// Protected by AdminAuthConfig (Authorization: Bearer <ADMIN_TOKEN>)
@RestController
@RequestMapping("/api/admin/appointments")
public class AdminController {
    private final AppointmentService service;
    AdminController(AppointmentService service) { this.service = service; }

    @GetMapping
    public List<Appointment> list(@RequestParam(required = false) String date, @RequestParam(required = false) String status) {
        return service.list(date, status);
    }

    @PatchMapping("/{id}")
    public Appointment update(@PathVariable Long id, @RequestBody StatusUpdate body) {
        return service.updateStatus(id, body.status());
    }
}
