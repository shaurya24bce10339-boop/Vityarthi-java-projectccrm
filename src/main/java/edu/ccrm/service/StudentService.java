package edu.ccrm.service;

import edu.ccrm.config.DataStore;
import edu.ccrm.domain.Student;

import java.util.List;
import java.util.UUID;

/**
 * Basic StudentService with create/list/update operations.
 */
public class StudentService {
    private final DataStore ds = DataStore.getInstance();

    public Student createStudent(String regNo, String fullName, String email) {
        String id = UUID.randomUUID().toString();
        Student s = new Student(id, regNo, fullName, email);
        ds.addStudent(s);
        return s;
    }

    public List<Student> listStudents() { return ds.listStudents(); }

    public void deactivateStudent(String id) {
        ds.findStudentById(id).ifPresent(Student::deactivate);
    }
}
