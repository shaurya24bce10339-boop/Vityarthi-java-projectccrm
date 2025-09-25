package edu.ccrm.config;

import edu.ccrm.domain.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Simple in-memory thread-safe data store (Singleton).
 */
public final class DataStore {
    private static final DataStore INSTANCE = new DataStore();
    private final Map<String, Student> students = new ConcurrentHashMap<>();
    private final Map<String, Course> courses = new ConcurrentHashMap<>();
    private final List<Enrollment> enrollments = Collections.synchronizedList(new ArrayList<>());

    private DataStore() {}

    public static DataStore getInstance() { return INSTANCE; }

    // Student ops
    public void addStudent(Student s) { students.put(s.getId(), s); }
    public Optional<Student> findStudentById(String id) { return Optional.ofNullable(students.get(id)); }
    public Optional<Student> findStudentByRegNo(String regNo) {
        return students.values().stream().filter(s -> s.getRegNo().equals(regNo)).findFirst();
    }
    public List<Student> listStudents() { return new ArrayList<>(students.values()); }

    // Course ops
    public void addCourse(Course c) { courses.put(c.getCode(), c); }
    public Optional<Course> findCourseByCode(String code) { return Optional.ofNullable(courses.get(code)); }
    public List<Course> listCourses() { return new ArrayList<>(courses.values()); }
    public List<Course> searchCoursesByDepartment(String dept) {
        return courses.values().stream().filter(c -> c.getDepartment().equalsIgnoreCase(dept)).collect(Collectors.toList());
    }

    // Enrollment ops
    public void addEnrollment(Enrollment e) { enrollments.add(e); }
    public List<Enrollment> listEnrollments() { return new ArrayList<>(enrollments); }
    public List<Enrollment> listEnrollmentsForStudent(Student s) {
        return enrollments.stream().filter(e -> e.getStudent().getId().equals(s.getId())).collect(Collectors.toList());
    }

    // Example stream-based report: top students by average grade points
    public List<Student> topStudentsByGPA(int limit) {
        return students.values().stream()
            .sorted((a,b) -> Double.compare(averageGpa(b), averageGpa(a)))
            .limit(limit)
            .collect(Collectors.toList());
    }

    private double averageGpa(Student s) {
        List<Enrollment> le = listEnrollmentsForStudent(s);
        return le.stream()
            .filter(en -> en.getMarks() != null)
            .mapToInt(en -> en.getGrade().getPoints())
            .average()
            .orElse(0.0);
    }
}
