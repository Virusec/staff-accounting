package com.example.staffaccounting.exception;

/**
 * @author Anatoliy Shikin
 */
public class ResourceNotFountException extends RuntimeException {
    public ResourceNotFountException(String resourceName, String fieldName, Object object) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, object));
    }
}
