package org.ntubach.module7;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// Controller for the service
@RestController
public class ServiceController {
    private List<Student> students = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private List<Registrar> registrar = new ArrayList<>();
    private final AtomicInteger counter = new AtomicInteger();

    ServiceController() {
        // TODO init with dummy data
        init();
    }

    @GetMapping(value = "/")
    public ResponseEntity index() {
        return ResponseEntity.ok("Hello World! Niko Tubach Module7");
    }

    @PostMapping(value = "/addCourse")
    public ResponseEntity<Course> addCourse(@RequestParam(value = "courseNumber") Integer courseNumber, @RequestParam(value = "courseTitle") String courseTitle) {
        // Validation on newCourse as needed
        Course newCourse = new Course(courseNumber, courseTitle);
        courses.add(newCourse);
        registrar.add(new Registrar(newCourse.getCourseNumber(), new ArrayList<>()));
        return ResponseEntity.ok(newCourse);
    }

    @GetMapping(value = "/all_courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courses);
    }

    void init() {

    }
}
