package edu.ccrm.io;

import edu.ccrm.config.DataStore;
import edu.ccrm.domain.Student;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Semester;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.CourseService;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Import / Export CSV simple implementation using NIO.2 + Streams.
 *
 * Expected student CSV: id,regNo,fullName,email
 * Expected course CSV: code,title,credits,department,semester
 */
public class ImportExportService {
    private final DataStore ds = DataStore.getInstance();
    private final StudentService ss = new StudentService();
    private final CourseService cs = new CourseService();

    public void importStudents(Path csv) throws IOException {
        try (var lines = Files.lines(csv)) {
            lines.skip(1).forEach(l -> {
                String[] t = l.split(",", -1);
                String id = t[0].isBlank() ? UUID.randomUUID().toString() : t[0];
                String regNo = t[1];
                String name = t[2];
                String email = t[3];
                Student s = new Student(id, regNo, name, email);
                ds.addStudent(s);
            });
        }
    }

    public void importCourses(Path csv) throws IOException {
        try (var lines = Files.lines(csv)) {
            lines.skip(1).forEach(l -> {
                String[] t = l.split(",", -1);
                String code = t[0];
                String title = t[1];
                int credits = Integer.parseInt(t[2]);
                String dept = t[3];
                Semester sem = Semester.valueOf(t[4].toUpperCase());
                Course c = new Course.Builder(code).title(title).credits(credits).department(dept).semester(sem).build();
                ds.addCourse(c);
            });
        }
    }

    public void exportStudents(Path out) throws IOException {
        var lines = ds.listStudents().stream()
                .map(s -> String.join(",", s.getId(), s.getRegNo(), s.getFullName(), s.getEmail()))
                .collect(Collectors.toList());
        lines.add(0, "id,regNo,fullName,email");
        Files.write(out, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public void exportCourses(Path out) throws IOException {
        var lines = ds.listCourses().stream()
                .map(c -> String.join(",", c.getCode(), c.getTitle(), String.valueOf(c.getCredits()), c.getDepartment(), c.getSemester().name()))
                .collect(Collectors.toList());
        lines.add(0, "code,title,credits,department,semester");
        Files.write(out, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
