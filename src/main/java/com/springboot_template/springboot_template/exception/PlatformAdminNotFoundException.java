package com.springboot_template.springboot_template.exception;

public class PlatformAdminNotFoundException extends RuntimeException {

    private final String id;

    public PlatformAdminNotFoundException(String id) {
        super("Platform admin not found: " + id);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
