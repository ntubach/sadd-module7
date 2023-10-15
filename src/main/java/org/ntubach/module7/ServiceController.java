package org.ntubach.module7;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    // GET a course based on courseNumber
    @GetMapping(value = "/course")
    public ResponseEntity<Course> getCourse(@RequestParam(value = "courseNumber") Integer courseNumber) {
        for (Course c : courses) {
            if (Objects.equals(c.getCourseNumber(), courseNumber))
                return ResponseEntity.ok(c);
        }
        return ResponseEntity.notFound().build();
    }

    // POST an additional new course and add it to the registrar
    @PostMapping(value = "/course")
    public ResponseEntity<Course> addCourse(@RequestParam(value = "courseNumber") Integer courseNumber, @RequestParam(value = "courseTitle") String courseTitle) {
        // Validation on newCourse info as needed
        Course newCourse = new Course(courseNumber, courseTitle);
        courses.add(newCourse);
        registrar.add(new Registrar(newCourse.getCourseNumber(), new ArrayList<>()));
        return ResponseEntity.ok(newCourse);
    }

    // PUT and update to a specified course number
    @PutMapping(value = "/course")
    public ResponseEntity<Course> updateCourse(@RequestParam(value = "courseNumber") Integer courseNumber, @RequestParam(value = "courseTitle") String courseTitle) {
        // Validation on updateCourse info as needed
        for (int i = 0; i < courses.size(); i++) {
            if (Objects.equals(courses.get(i).getCourseNumber(), courseNumber)) {
                courses.set(i, new Course(courseNumber, courseTitle));
                return ResponseEntity.ok(courses.get(i));
            }
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE course based on courseNumber if it exists
    @DeleteMapping(value = "/course")
    public ResponseEntity<Course> deleteCourse(@RequestParam(value = "courseNumber") Integer courseNumber) {
        for (int i = 0; i < courses.size(); i++) {
            if (Objects.equals(courses.get(i).getCourseNumber(), courseNumber)) {
                return ResponseEntity.ok(courses.remove(i));
            }
        }
        return ResponseEntity.notFound().build();
    }

    // GET all courses
    @GetMapping(value = "/course/all")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courses);
    }

    void init() {

    }
}
