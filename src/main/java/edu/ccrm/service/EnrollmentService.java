package edu.ccrm.service;

import edu.ccrm.config.DataStore;
import edu.ccrm.domain.*;
import edu.ccrm.exception.DuplicateEnrollmentException;
import edu.ccrm.exception.MaxCreditLimitExceededException;

import java.util.List;

/**
 * Enrollment service: enroll/unenroll and record grades.
 * Demonstrates business rule: max credits per semester (example=18).
 */
public class EnrollmentService {
    private final DataStore ds = DataStore.getInstance();
    private static final int MAX_CREDITS = 18;

    public Enrollment enroll(Student s, Course c) throws DuplicateEnrollmentException {
        // duplicate check
        if (s.getEnrolledCourseCodes().contains(c.getCode())) {
            throw new DuplicateEnrollmentException("Student already enrolled in " + c.getCode());
        }
        // basic credit check (sum of current credits in enrolled codes)
        int currentCredits = s.getEnrolledCourseCodes().stream()
                .map(code -> ds.findCourseByCode(code).map(Course::getCredits).orElse(0))
                .mapToInt(Integer::intValue).sum();
        if (currentCredits + c.getCredits() > MAX_CREDITS) {
            throw new MaxCreditLimitExceededException("Enrolling exceeds max credits " + MAX_CREDITS);
        }
        Enrollment e = new Enrollment(s, c);
        s.enrollCourse(c.getCode());
        ds.addEnrollment(e);
        return e;
    }

    public void recordMarks(Enrollment e, int marks) {
        e.recordMarks(marks);
    }

    public List<Enrollment> listEnrollmentsForStudent(Student s) {
        return ds.listEnrollmentsForStudent(s);
    }
}
