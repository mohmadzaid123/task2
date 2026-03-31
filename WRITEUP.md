## Phase 2 — JPQL vs Native SQL

**JPQL** works with Java class and field names:
SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId

**Native SQL** works with actual table and column names:
SELECT * FROM appointments WHERE patient_id = :pid

Use JPQL when you want database independence and type safety.
Use native SQL when you need DB-specific features like
LIMIT, complex joins, or stored procedures.

## Hibernate Caching

**First-level cache**: automatic, per-session (transaction).
Calling findById twice in the same transaction only hits
the DB once — the second call comes from memory.

**Second-level cache**: shared across sessions. Configured
with @Cacheable. Survives between different transactions.
```

---

## Phase 2 Checklist ✅
```
✅ 4 JPA entities with relationships and constraints
✅ @PrePersist on Appointment sets createdAt automatically
✅ 3 repositories with derived queries
✅ JPQL and native SQL queries in AppointmentRepository
✅ @Cacheable and @CacheEvict on DoctorService
✅ First-level cache test
✅ WRITEUP.md updated