package com.springboot_template.springboot_template.exception;

public class DuplicatePlatformAdminFieldException extends RuntimeException {

    private final String field;
    private final String value;

    public DuplicatePlatformAdminFieldException(String field, String value) {
        super("Platform admin " + field + " already exists: " + value);
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
