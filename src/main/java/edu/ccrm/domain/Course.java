package edu.ccrm.domain;

import java.util.Objects;

/**
 * Course with Builder (static nested) and an immutable CourseCode value object.
 */
public class Course {
    // Immutable CourseCode nested static final class (value object)
    public static final class CourseCode {
        private final String code;
        private CourseCode(String code) { this.code = Objects.requireNonNull(code).toUpperCase(); }
        public String code() { return code; }
        @Override public String toString() { return code; }
    }

    private final CourseCode code;
    private String title;
    private int credits;
    private Instructor instructor;
    private Semester semester;
    private String department;
    private boolean active = true;

    private Course(Builder b) {
        this.code = new CourseCode(b.code);
        this.title = b.title;
        this.credits = b.credits;
        this.instructor = b.instructor;
        this.semester = b.semester;
        this.department = b.department;
    }

    public static class Builder {
        private final String code;
        private String title = "Untitled";
        private int credits = 3;
        private Instructor instructor;
        private Semester semester = Semester.FALL;
        private String department = "General";

        public Builder(String code) { this.code = code; }
        public Builder title(String t){ this.title = t; return this; }
        public Builder credits(int c){ this.credits = c; return this; }
        public Builder instructor(Instructor i){ this.instructor = i; return this; }
        public Builder semester(Semester s){ this.semester = s; return this; }
        public Builder department(String d){ this.department = d; return this; }
        public Course build(){ return new Course(this); }
    }

    /* getters */
    public CourseCode getCodeObj() { return code; }
    public String getCode() { return code.toString(); }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public Instructor getInstructor() { return instructor; }
    public Semester getSemester() { return semester; }
    public String getDepartment() { return department; }
    public boolean isActive() { return active; }

    public void setInstructor(Instructor instructor) { this.instructor = instructor; }
    public void deactivate() { this.active = false; }

    @Override
    public String toString() {
        return String.format("%s - %s (%dcr) [%s] Dept:%s Instr:%s",
                code, title, credits, semester, department, instructor == null ? "TBD" : instructor.getFullName());
    }
}
