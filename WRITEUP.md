# Phase 1 Written Tasks

## Properties vs YAML

`.properties` uses flat key=value format:
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db

`.yml` uses indented hierarchical format:
spring:
datasource:
url: jdbc:mysql://localhost:3306/hospital_db

For a team project I would choose `.yml` because it is easier
to read, avoids repetition of prefixes, and groups related
config visually.

## Constructor vs Setter vs Field Injection

**Field injection** (@Autowired on field):
- Simplest to write but worst practice
- Cannot be used in tests without Spring context
- Hides dependencies

**Setter injection** (@Autowired on setter method):
- Allows optional dependencies
- Still not ideal for required dependencies

**Constructor injection** (preferred):
- Dependencies are explicit and required
- Works in plain unit tests without Spring
- Supports immutability (final fields)
- Recommended by Spring team and this project

## Bean Scopes

- Singleton: one instance shared across the entire application
- Prototype: new instance created every time the bean is requested
- Request: new instance created per HTTP request (web apps only)
```

---

## Phase 1 Checklist ✅
```
✅ Spring Boot project builds and runs
✅ application.yml configured
✅ HospitalConfig with @Bean for ModelMapper and PasswordEncoder
✅ AuditService with @PostConstruct and @PreDestroy
✅ Singleton, Prototype, Request scope demonstrated
✅ Unit test proving singleton vs prototype behaviour
✅ HospitalBootstrapRunner printing startup banner
✅ WRITEUP.md with written answers