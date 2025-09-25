package edu.ccrm.domain;

import java.time.LocalDate;

/** Enrollment records marks and computes grade */
public class Enrollment {
    private final Student student;
    private final Course course;
    private final LocalDate enrolledOn;
    private Integer marks; // null until graded

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.enrolledOn = LocalDate.now();
    }

    public Student getStudent(){ return student; }
    public Course getCourse(){ return course; }
    public LocalDate getEnrolledOn(){ return enrolledOn; }
    public Integer getMarks(){ return marks; }

    public void recordMarks(int marks) {
        if (marks < 0 || marks > 100) throw new IllegalArgumentException("Marks must be 0-100");
        this.marks = marks;
    }

    public Grade getGrade() {
        if (marks == null) return Grade.I;
        return Grade.fromMarks(marks);
    }

    public String transcriptLine() {
        return String.format("%s | %s | %dcr | Marks=%s | Grade=%s",
            course.getCode(), course.getTitle(), course.getCredits(), marks == null ? "N/A" : marks, getGrade());
    }

    @Override
    public String toString() {
        return String.format("Enrollment[%s -> %s] on %s", student.getRegNo(), course.getCode(), enrolledOn);
    }
}
