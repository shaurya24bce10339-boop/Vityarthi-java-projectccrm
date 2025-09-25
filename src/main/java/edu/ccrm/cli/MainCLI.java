package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.config.DataStore;
import edu.ccrm.domain.*;
import edu.ccrm.exception.DuplicateEnrollmentException;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.service.*;
import edu.ccrm.util.BackupUtil;

import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

/**
 * Main CLI entrypoint: menu-driven using switch (enhanced) and various loops.
 */
public class MainCLI {
    private static final StudentService studentService = new StudentService();
    private static final CourseService courseService = new CourseService();
    private static final EnrollmentService enrollmentService = new EnrollmentService();
    private static final ImportExportService ioService = new ImportExportService();
    private static final DataStore ds = DataStore.getInstance();

    public static void main(String[] args) {
        AppConfig cfg = AppConfig.getInstance();
        System.out.println("Welcome to Campus Course & Records Manager (CCRM)");
        System.out.println("Config loaded: " + cfg);
        Scanner sc = new Scanner(System.in);

        mainLoop:
        while (true) {
            printMainMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> manageStudents(sc);
                case "2" -> manageCourses(sc);
                case "3" -> manageEnrollment(sc);
                case "4" -> importExport(sc);
                case "5" -> backup(sc);
                case "6" -> reports(sc);
                case "0" -> { System.out.println("Exiting. Goodbye!"); break mainLoop; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1) Manage Students");
        System.out.println("2) Manage Courses");
        System.out.println("3) Enrollment & Grades");
        System.out.println("4) Import / Export");
        System.out.println("5) Backup Data");
        System.out.println("6) Reports");
        System.out.println("0) Exit");
        System.out.print("Select> ");
    }

    // Student management simple flows
    private static void manageStudents(Scanner sc) {
        while (true) {
            System.out.println("\n-- STUDENTS --");
            System.out.println("1) Add Student");
            System.out.println("2) List Students");
            System.out.println("3) Deactivate Student");
            System.out.println("0) Back");
            System.out.print("choice> ");
            String c = sc.nextLine();
            switch (c) {
                case "1" -> {
                    System.out.print("RegNo: "); String reg = sc.nextLine();
                    System.out.print("Full name: "); String name = sc.nextLine();
                    System.out.print("Email: "); String email = sc.nextLine();
                    var s = studentService.createStudent(reg, name, email);
                    System.out.println("Created: " + s);
                }
                case "2" -> {
                    List<Student> list = studentService.listStudents();
                    list.forEach(System.out::println);
                }
                case "3" -> {
                    System.out.print("Student ID to deactivate: "); String id = sc.nextLine();
                    studentService.deactivateStudent(id);
                    System.out.println("If exists, deactivated.");
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid.");
            }
        }
    }

    private static void manageCourses(Scanner sc) {
        while (true) {
            System.out.println("\n-- COURSES --");
            System.out.println("1) Add Course");
            System.out.println("2) List Courses");
            System.out.println("0) Back");
            System.out.print("choice> ");
            String c = sc.nextLine();
            switch (c) {
                case "1" -> {
                    System.out.print("Code (e.g. CS101): "); String code = sc.nextLine();
                    System.out.print("Title: "); String title = sc.nextLine();
                    System.out.print("Credits: "); int cr = Integer.parseInt(sc.nextLine());
                    System.out.print("Department: "); String dept = sc.nextLine();
                    System.out.print("Semester (SPRING/SUMMER/FALL): "); Semester sem = Semester.valueOf(sc.nextLine().toUpperCase());
                    courseService.createCourse(code, title, cr, dept, sem);
                    System.out.println("Course created.");
                }
                case "2" -> courseService.listCourses().forEach(System.out::println);
                case "0" -> { return; }
                default -> System.out.println("Invalid.");
            }
        }
    }

    private static void manageEnrollment(Scanner sc) {
        while (true) {
            System.out.println("\n-- ENROLLMENT --");
            System.out.println("1) Enroll student to course");
            System.out.println("2) Record marks");
            System.out.println("3) Print transcript for student");
            System.out.println("0) Back");
            System.out.print("choice> ");
            String c = sc.nextLine();
            switch (c) {
                case "1" -> {
                    System.out.print("Student RegNo: "); String reg = sc.nextLine();
                    System.out.print("Course code: "); String code = sc.nextLine().toUpperCase();
                    var stOpt = DataStore.getInstance().findStudentByRegNo(reg);
                    var coOpt = DataStore.getInstance().findCourseByCode(code);
                    if (stOpt.isEmpty() || coOpt.isEmpty()) {
                        System.out.println("Student or Course not found.");
                        break;
                    }
                    try {
                        var e = enrollmentService.enroll(stOpt.get(), coOpt.get());
                        System.out.println("Enrolled: " + e);
                    } catch (DuplicateEnrollmentException ex) {
                        System.out.println("Already enrolled: " + ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Error: " + ex.getMessage());
                    }
                }
                case "2" -> {
                    System.out.print("Student RegNo: "); String reg = sc.nextLine();
                    System.out.print("Course code: "); String code = sc.nextLine().toUpperCase();
                    var sOpt = DataStore.getInstance().findStudentByRegNo(reg);
                    var eOpt = DataStore.getInstance().listEnrollments().stream()
                            .filter(e -> e.getStudent().equals(sOpt.orElse(null)) && e.getCourse().getCode().equals(code))
                            .findFirst();
                    if (eOpt.isEmpty()) { System.out.println("Enrollment not found."); break; }
                    System.out.print("Marks (0-100): "); int marks = Integer.parseInt(sc.nextLine());
                    enrollmentService.recordMarks(eOpt.get(), marks);
                    System.out.println("Marks recorded. Grade: " + eOpt.get().getGrade());
                }
                case "3" -> {
                    System.out.print("Student RegNo: "); String reg = sc.nextLine();
                    var sOpt = DataStore.getInstance().findStudentByRegNo(reg);
                    if (sOpt.isEmpty()) { System.out.println("Not found."); break; }
                    printTranscript(sOpt.get());
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid.");
            }
        }
    }

    private static void printTranscript(Student s) {
        System.out.println("\nTranscript for: " + s.profile());
        List<Enrollment> el = DataStore.getInstance().listEnrollmentsForStudent(s);
        if (el.isEmpty()) System.out.println("No enrollments.");
        else el.forEach(e -> System.out.println(e.transcriptLine()));
    }

    private static void importExport(Scanner sc) {
        System.out.println("\n-- IMPORT/EXPORT --");
        System.out.println("1) Import students from test-data/students.csv");
        System.out.println("2) Import courses from test-data/courses.csv");
        System.out.println("3) Export students to exports/students.csv");
        System.out.println("4) Export courses to exports/courses.csv");
        System.out.println("0) Back");
        System.out.print("choice> ");
        String c = sc.nextLine();
        try {
            switch (c) {
                case "1" -> ioService.importStudents(Paths.get("test-data/students.csv"));
                case "2" -> ioService.importCourses(Paths.get("test-data/courses.csv"));
                case "3" -> {
                    Path out = Paths.get("exports");
                    Files.createDirectories(out);
                    ioService.exportStudents(out.resolve("students.csv"));
                    System.out.println("Exported to exports/students.csv");
                }
                case "4" -> {
                    Path out = Paths.get("exports");
                    Files.createDirectories(out);
                    ioService.exportCourses(out.resolve("courses.csv"));
                    System.out.println("Exported to exports/courses.csv");
                }
                case "0" -> { return; }
                default -> System.out.println("Invalid.");
            }
        } catch (Exception ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    private static void backup(Scanner sc) {
        try {
            Path source = Paths.get(System.getProperty("user.dir")); // backup working dir for demo
            Path backups = Paths.get("backups");
            Files.createDirectories(backups);
            Path created = BackupUtil.copyToTimestampedBackup(source, backups);
            long size = BackupUtil.computeDirectorySize(created);
            System.out.println("Backup created at: " + created);
            System.out.println("Backup total size (bytes): " + size);
        } catch (Exception ex) {
            System.out.println("Backup failed: " + ex.getMessage());
        }
    }

    private static void reports(Scanner sc) {
        System.out.println("\n-- REPORTS --");
        System.out.println("Top students by GPA:");
        DataStore.getInstance().topStudentsByGPA(5).forEach(s -> System.out.println(s.profile()));
    }
}
