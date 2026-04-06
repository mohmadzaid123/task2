package org.exalt.task2.exception;

public class DoubleBookingException extends RuntimeException {
    public DoubleBookingException(String message) {
        super(message);
    }
}