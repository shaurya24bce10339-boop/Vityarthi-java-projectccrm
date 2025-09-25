# Campus Course & Records Manager (CCRM)

This is a full example Java SE console-based project implementing the Campus Course & Records Manager (CCRM) specification.

See `USAGE.md` for quick start and `test-data/` for sample CSVs.

🚀 Features

Student Management → Add, list, update, deactivate students.

Course Management → Add, list, update, deactivate courses; search & filter.

Enrollment & Grading → Enroll students, enforce credit limits, record marks, compute GPA.

Transcripts → Generate student transcripts with letter grades.

File Operations → Import/export CSV datasets, backup system with recursion.

Reports → GPA distribution, top students, course enrollments.

📂 Project Structure
edu.ccrm
├─ cli/        → Menu-driven CLI
├─ domain/     → Entities: Student, Instructor, Course, Enrollment, Enums
├─ service/    → Business logic (StudentService, CourseService, etc.)
├─ io/         → File import/export (CSV, JSON-like)
├─ util/       → Validators, Comparators, recursive utilities
├─ config/     → Singleton AppConfig, builders
datasets/      → Sample CSV datasets (students, courses, instructors, enrollments)

⚡ Technical Demonstrations

This project is designed to showcase Java concepts:

Core Java

Primitive types, operators (arithmetic, logical, relational, bitwise).

Decision structures → if, if-else, nested if, switch.

Loops → for, while, do-while, enhanced for; includes break, continue, and a labeled jump.

Arrays & Arrays utility methods (searching/sorting).

String operations → substring, split, join, equals, compareTo.

OOP & Type System

Encapsulation → private fields, getters/setters.

Inheritance → Person (abstract) → Student / Instructor.

Abstraction → abstract methods in Person.

Polymorphism → toString() overrides, service interfaces, transcript rendering.

Access Modifiers → private, protected, public, and package-private.

Constructors & super calls in inheritance.

Immutable Class → CourseCode.

Nested Classes → one static and one inner class.

Interfaces → Persistable, Searchable<T>; diamond problem resolution via default methods.

Lambdas & Functional Interfaces → Comparators, Predicates.

Anonymous Inner Class → callback in CLI.

Enums → Semester, Grade with fields & constructors.

Advanced Concepts

Casting → upcasting, downcasting with instanceof.

Overloading & Overriding.

Design Patterns:

Singleton → AppConfig.

Builder → Course.Builder, Transcript.Builder.

Exceptions:

Checked & unchecked exceptions.

Custom exceptions → DuplicateEnrollmentException, MaxCreditLimitExceededException.

try/catch/finally, multi-catch, throw / throws.

Assertions for invariants (IDs non-null, credit limits).

Java APIs

NIO.2 → Path, Files (copy/move/delete).

Streams API → filtering, mapping, GPA aggregation.

Date/Time API → enrollment dates, backup timestamps.

Recursion → compute backup directory size.

📊 Datasets

Located in the datasets/ folder:

students.csv

instructors.csv

courses.csv

enrollments.csv

Example students.csv:

id,regNo,fullName,email,status,enrollmentDate
1,REG1001,John Doe,john@example.com,ACTIVE,2023-09-01
2,REG1002,Jane Smith,jane@example.com,ACTIVE,2023-09-02

📥 Running the Project
Compile
javac -d bin src/edu/ccrm/**/*.java   # Linux/Mac
# or on Windows PowerShell
dir -Recurse -Filter *.java | ForEach-Object { $_.FullName } > sources.txt
javac -d bin @sources.txt

Run
java -cp bin edu.ccrm.cli.MainCLI

Run with Assertions
java -ea -cp bin edu.ccrm.cli.MainCLI

🖥️ CLI Demo Flow

On start → AppConfig (Singleton) loads config.

Main menu:

1. Manage Students
2. Manage Courses
3. Enrollment & Grading
4. Reports
5. Import/Export Data
6. Backup & Utilities
0. Exit


Import datasets → datasets/students.csv, etc.

Enroll students, record grades, print transcript.

Export & run backup (timestamped folder).

Reports → top students, GPA distribution.

📜 Java Platform Notes
Evolution of Java

1995 → Java 1.0 (Sun Microsystems).

2004 → Java 5 (Generics, Annotations).

2011 → Java 7 (try-with-resources, NIO.2).

2014 → Java 8 (Lambdas, Streams).

2017 → Java 9 (Modules).

2021 → Java 17 (LTS).

2023+ → Java 21 (LTS, latest features).

Java Editions
Edition	Scope	Use Cases
Java SE	Core libraries, JVM, APIs	Desktop, CLI apps (like CCRM)
Java EE (Jakarta EE)	Adds web, enterprise APIs	Web apps, servers
Java ME	Lightweight subset	Embedded, mobile
Java Architecture

JDK → Development Kit (compiler, tools).

JRE → Runtime (libraries, JVM).

JVM → Executes bytecode, platform-independent.

🖥️ Setup Notes
Install Java on Windows

Download JDK from Oracle
.

Install & set JAVA_HOME.

Verify:

java -version
javac -version

VS Code Setup

Install Extension Pack for Java.

Compile & run using tasks (Ctrl+Shift+B) or MainCLI.java run button.

🗂️ Mapping (Syllabus → Code)
Concept	Where implemented
Encapsulation	Student.java (private fields + getters/setters)
Inheritance	Person.java → Student.java, Instructor.java
Abstraction	Person.java (abstract)
Polymorphism	TranscriptService + toString() overrides
Singleton	AppConfig.java
Builder	Course.Builder, Transcript.Builder
Enums	Semester.java, Grade.java
Custom Exceptions	DuplicateEnrollmentException.java, MaxCreditLimitExceededException.java
Streams	CourseService.filterByDepartment()
Recursion	BackupService.computeDirectorySize()
Assertions	Enrollment.java (non-null, credit check)
