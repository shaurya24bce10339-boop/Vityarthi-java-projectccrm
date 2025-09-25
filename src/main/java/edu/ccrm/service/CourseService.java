package edu.ccrm.service;

import edu.ccrm.config.DataStore;
import edu.ccrm.domain.*;

import java.util.List;

/**
 * Basic CourseService with create/list/search operations.
 */
public class CourseService {
    private final DataStore ds = DataStore.getInstance();

    public Course createCourse(String code, String title, int credits, String department, Semester semester) {
        Course c = new Course.Builder(code)
                .title(title)
                .credits(credits)
                .department(department)
                .semester(semester)
                .build();
        ds.addCourse(c);
        return c;
    }

    public List<Course> listCourses() { return ds.listCourses(); }
    public List<Course> findByDepartment(String dept) { return ds.searchCoursesByDepartment(dept); }
}
