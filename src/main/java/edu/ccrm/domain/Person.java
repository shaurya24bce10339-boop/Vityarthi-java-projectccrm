package edu.ccrm.domain;

import java.time.LocalDate;

/**
 * Abstract Person demonstrating abstraction and inheritance.
 */
public abstract class Person {
    protected final String id; // immutable identity
    protected String fullName;
    protected String email;
    protected LocalDate createdAt;

    protected Person(String id, String fullName, String email) {
        assert id != null && !id.isBlank() : "id must be non-null and non-blank";
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.createdAt = LocalDate.now();
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public LocalDate getCreatedAt() { return createdAt; }

    public abstract String profile();
}
