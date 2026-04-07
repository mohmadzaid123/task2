package org.exalt.task2.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // ─── Pointcuts ────────────────────────────────────────────────

    @Pointcut("execution(* org.exalt.task2.service.*.*(..))")
    public void allServiceMethods() {}

    @Pointcut("execution(* org.exalt.task2.service.AppointmentService.bookAppointment(..))")
    public void appointmentBooking() {}

    @Pointcut("execution(* org.exalt.task2.service.PrescriptionService.addPrescription(..))")
    public void prescriptionUpdate() {}

    // ─── Advices ──────────────────────────────────────────────────

    // Runs BEFORE every service method
    @Before("allServiceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info(">>> Calling: {}.{}() with args: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    // Runs AFTER bookAppointment returns successfully
    @AfterReturning(pointcut = "appointmentBooking()", returning = "result")
    public void logAfterBooking(Object result) {
        log.info(">>> Appointment booked successfully: {}", result);
    }

    // Runs AFTER any service method throws an exception
    @AfterThrowing(pointcut = "allServiceMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        // Special message for double booking
        if (ex.getClass().getSimpleName().equals("DoubleBookingException")) {
            log.warn(">>> Double booking attempt prevented for method: {} - {}",
                    joinPoint.getSignature().getName(),
                    ex.getMessage());
        } else {
            log.error(">>> Exception in {}.{}(): {} - {}",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(),
                    ex.getClass().getSimpleName(),
                    ex.getMessage());
        }
    }

    // Runs AROUND bookAppointment — measures execution time
    @Around("appointmentBooking()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed(); // actually runs the method

        long duration = System.currentTimeMillis() - start;
        log.info(">>> bookAppointment() executed in {} ms", duration);

        return result;
    }
}