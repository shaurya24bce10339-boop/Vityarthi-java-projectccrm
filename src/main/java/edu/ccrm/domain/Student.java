package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.*;

/**
 * Student class demonstrating encapsulation & collections.
 */
public class Student extends Person {
    public enum Status { ACTIVE, INACTIVE }

    private final String regNo;
    private Status status;
    private final LocalDate admissionDate;
    // enrolled course codes stored for quick access
    private final Set<String> enrolledCourseCodes = new HashSet<>();

    public Student(String id, String regNo, String fullName, String email) {
        super(id, fullName, email);
        this.regNo = regNo;
        this.status = Status.ACTIVE;
        this.admissionDate = LocalDate.now();
    }

    public String getRegNo() { return regNo; }
    public Status getStatus() { return status; }
    public LocalDate getAdmissionDate() { return admissionDate; }

    public void enrollCourse(String courseCode) { enrolledCourseCodes.add(courseCode); }
    public void unenrollCourse(String courseCode) { enrolledCourseCodes.remove(courseCode); }
    public Set<String> getEnrolledCourseCodes() { return Collections.unmodifiableSet(enrolledCourseCodes); }

    public void deactivate() { this.status = Status.INACTIVE; }

    @Override
    public String profile() {
        return String.format("%s [%s] email=%s status=%s", fullName, regNo, email, status);
    }

    @Override
    public String toString() { return profile(); }
}
