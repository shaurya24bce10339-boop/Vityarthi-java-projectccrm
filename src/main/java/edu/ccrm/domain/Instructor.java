package edu.ccrm.domain;

/**
 * Instructor extends Person.
 */
public class Instructor extends Person {
    private String department;

    public Instructor(String id, String name, String email, String department) {
        super(id, name, email);
        this.department = department;
    }

    public String getDepartment() { return department; }
    @Override
    public String profile() {
        return String.format("Instructor: %s (%s) Dept: %s", fullName, email, department);
    }
}
