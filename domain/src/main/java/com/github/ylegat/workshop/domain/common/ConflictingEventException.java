package com.github.ylegat.workshop.domain.common;

public class ConflictingEventException extends RuntimeException {

    public ConflictingEventException(String message) {
        super(message);
    }
}
