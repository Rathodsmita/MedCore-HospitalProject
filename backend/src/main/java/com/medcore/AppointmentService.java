package com.medcore;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AppointmentService {
    private static final int MAX_PER_DOCTOR_PER_DAY = 10;
    private static final Pattern PHONE = Pattern.compile("^\\+?[0-9 ]{10,14}$");

    private final AppointmentRepository appointments;
    private final DoctorRepository doctors;
    private final DepartmentRepository departments;

    AppointmentService(AppointmentRepository a, DoctorRepository d, DepartmentRepository dep) {
        this.appointments = a; this.doctors = d; this.departments = dep;
    }

    @Transactional
    public BookingResponse book(BookingRequest r) {
        String name = clean(r.name(), 100), phone = clean(r.phone(), 20);
        String dept = clean(r.department(), 100), doctorName = clean(r.doctor(), 100);

        if (name.length() < 2) throw bad("Please enter your full name.");
        if (!PHONE.matcher(phone).matches()) throw bad("Please enter a valid phone number.");
        boolean deptOk = departments.findAll().stream().anyMatch(d -> d.getName().equals(dept));
        if (!deptOk) throw bad("Please select a valid department.");
        Doctor doc = doctors.findByName(doctorName).orElseThrow(() -> bad("Please select a valid doctor."));
        if (!doc.getDepartment().equals(dept)) throw bad(doc.getName() + " does not belong to " + dept + ".");

        LocalDate date;
        try { date = LocalDate.parse(clean(r.date(), 10)); } catch (Exception e) { throw bad("Please select a valid date."); }
        if (date.isBefore(LocalDate.now())) throw bad("Appointment date cannot be in the past.");

        long taken = appointments.countByDoctorAndAppointmentDateAndStatusNot(doc.getName(), date, "cancelled");
        if (taken >= MAX_PER_DOCTOR_PER_DAY)
            throw new ResponseStatusException(HttpStatus.CONFLICT, doc.getName() + " is fully booked on " + date + ". Please choose another date.");

        String ref = "MC" + Long.toString(System.currentTimeMillis(), 36).toUpperCase();
        Appointment a = appointments.save(new Appointment(ref, name, phone, dept, doc.getName(), date));
        return new BookingResponse(a.getReference(), a.getName(), a.getDoctor(), date.toString(), a.getStatus());
    }

    public List<Appointment> list(String date, String status) {
        return appointments.findAllByOrderByCreatedAtDesc().stream()
            .filter(a -> date == null || a.getAppointmentDate().toString().equals(date))
            .filter(a -> status == null || a.getStatus().equals(status))
            .toList();
    }

    @Transactional
    public Appointment updateStatus(Long id, String status) {
        if (status == null || !List.of("pending", "confirmed", "cancelled").contains(status))
            throw bad("Invalid status.");
        Appointment a = appointments.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found."));
        a.setStatus(status);
        return a;
    }

    private static ResponseStatusException bad(String msg) { return new ResponseStatusException(HttpStatus.BAD_REQUEST, msg); }

    private static String clean(String v, int max) {
        if (v == null) return "";
        String s = v.replaceAll("[<>]", "").trim();
        return s.length() > max ? s.substring(0, max) : s;
    }
}
