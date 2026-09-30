# MedCore Hospital — Frontend + Backend

    medcore-project/
    ├── frontend/   HTML + CSS + JavaScript  (index.html, style.css, script.js, config.js, images/)
    └── backend/    Java Spring Boot + MySQL (pom.xml, src/main/java/com/medcore/...)

## 1) Backend  (JDK 17+, Maven, MySQL 8)
    cd backend
    export DB_USER=root DB_PASSWORD=yourpassword ADMIN_TOKEN=my-secret
    mvn spring-boot:run          # http://localhost:8080
The `medcore` database + tables are created automatically; departments and doctors are seeded on first run.

## 2) Frontend
Open a second terminal:
    cd frontend
    python3 -m http.server 5500   # or use VS Code "Live Server"
Then open http://localhost:5500
(Double-clicking index.html also works.)

The frontend talks to the backend at the URL in `frontend/config.js` (default http://localhost:8080).

## API
| Method | URL | Auth | Purpose |
|---|---|---|---|
| GET | /api/departments | – | departments |
| GET | /api/doctors?department=Cardiology | – | doctors |
| POST | /api/appointments | – | book: {name, phone, department, doctor, date} |
| GET | /api/admin/appointments?date=YYYY-MM-DD&status=pending | Bearer | list bookings |
| PATCH | /api/admin/appointments/{id} {"status":"confirmed"} | Bearer | confirm / cancel |

Example: curl -H "Authorization: Bearer my-secret" http://localhost:8080/api/admin/appointments
