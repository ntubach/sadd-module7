package org.ntubach.module7;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

// Controller for the service
@RestController
public class ServiceController {
    private final List<Student> students = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
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
    // --------- STUDENTS ENDPOINTS ---------

    // GET a student based on student id
    @GetMapping(value = "/student")
    public ResponseEntity<Student> getStudent(@RequestParam(value = "id") Integer id) {
        for (Student s : students) {
            if (Objects.equals(s.getId(), id))
                return ResponseEntity.ok(s);
        }
        return ResponseEntity.notFound().build();
    }

    // POST an additional new student
    @PostMapping(value = "/student")
    public ResponseEntity<Student> addStudent(@RequestParam(value = "id")  Integer id,
                                            @RequestParam(value = "firstname", required = false) String firstname,
                                            @RequestParam(value = "lastname") String lastname,
                                            @RequestParam(value = "dateOfBirth") String dateOfBirth,
                                            @RequestParam(value = "email") String email) {
        // Validation on newCourse info as needed
        Student newStudent = new Student(id, firstname, lastname, LocalDateTime.now(), "");
        if (newStudent.setDateOfBirth(dateOfBirth) && newStudent.setEmail(email)) {
            students.add(newStudent);
            return ResponseEntity.ok(newStudent);
        }
        return ResponseEntity.badRequest().build();
    }

    // PUT and update to a specified student id
    @PutMapping(value = "/student")
    public ResponseEntity<Student> updateStudent(@RequestParam(value = "id") Integer id,
                                                 @RequestParam(value = "firstname", required = false) String firstname,
                                                 @RequestParam(value = "lastname") String lastname,
                                                 @RequestParam(value = "dateOfBirth") String dateOfBirth,
                                                 @RequestParam(value = "email") String email) {
        // Validation on updateStudent info as needed
        for (int i = 0; i < students.size(); i++) {
            if (Objects.equals(students.get(i).getId(), id)) {
                Student newStudent = new Student(id, firstname, lastname, LocalDateTime.now(), "");
                if (newStudent.setDateOfBirth(dateOfBirth) && newStudent.setEmail(email)) {
                    students.set(i, newStudent);
                    return ResponseEntity.ok(newStudent);
                }
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE student based on student id if it exists
    @DeleteMapping(value = "/student")
    public ResponseEntity<Student> deleteStudent(@RequestParam(value = "id") Integer id) {
        for (int i = 0; i < students.size(); i++) {
            if (Objects.equals(students.get(i).getId(), id)) {
                return ResponseEntity.ok(students.remove(i));
            }
        }
        return ResponseEntity.notFound().build();
    }

    // GET all students
    @GetMapping(value = "/student/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(students);
    }


    // --------- COURSES ENDPOINTS ---------

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

    // --------- REGISTRAR ENDPOINTS ---------

    void init() {

    }
}
