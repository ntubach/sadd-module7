package org.ntubach.module7;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

// Controller for the service
@RestController
public class ServiceController {
    private final List<Student> students = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<Registrar> registrar = new ArrayList<>();
    private final AtomicInteger counter = new AtomicInteger();

    ServiceController() {
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
    @ResponseBody
    public ResponseEntity<Student> addStudent(@RequestParam(value = "firstName", required = false) String firstname,
                                              @RequestParam(value = "lastName") String lastname,
                                              @RequestParam(value = "dateOfBirth") String dateOfBirth,
                                              @RequestParam(value = "email") String email) {
        // Validation on newStudent info as needed
        Student newStudent = new Student(counter.incrementAndGet(), firstname, lastname, LocalDate.now(), "");
        if (!newStudent.setDateOfBirth(dateOfBirth)) {
            ResponseEntity re = ResponseEntity.badRequest().body("Bad dateOfBirth format");
            return re;
        }
        if (!newStudent.setEmail(email)) {
            ResponseEntity re = ResponseEntity.badRequest().body("Bad email format");
            return re;
        }
        students.add(newStudent);
        return ResponseEntity.ok(newStudent);
    }

    // PUT and update to a specified student id
    @PutMapping(value = "/student")
    public ResponseEntity<Student> updateStudent(@RequestParam(value = "id") Integer id,
                                                 @RequestParam(value = "firstName", required = false) String firstname,
                                                 @RequestParam(value = "lastName") String lastname,
                                                 @RequestParam(value = "dateOfBirth") String dateOfBirth,
                                                 @RequestParam(value = "email") String email) {
        // Validation on updateStudent info as needed
        for (int i = 0; i < students.size(); i++) {
            if (Objects.equals(students.get(i).getId(), id)) {
                Student newStudent = new Student(id, firstname, lastname, LocalDate.now(), "");
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
    public ResponseEntity<Course> addCourse(@RequestParam(value = "courseNumber") Integer courseNumber,
                                            @RequestParam(value = "courseTitle") String courseTitle) {
        // Validation on newCourse info as needed
        for (Course c : courses) {
            if (Objects.equals(c.getCourseNumber(), courseNumber)) {
                ResponseEntity re = ResponseEntity.badRequest().body("Course with that courseNumber already exists");
                return re;
            }
        }
        Course newCourse = new Course(courseNumber, courseTitle);
        courses.add(newCourse);
        registrar.add(new Registrar(newCourse.getCourseNumber(), new ArrayList<>()));
        return ResponseEntity.ok(newCourse);
    }

    // PUT and update to a specified course number
    @PutMapping(value = "/course")
    public ResponseEntity<Course> updateCourse(@RequestParam(value = "courseNumber") Integer courseNumber,
                                               @RequestParam(value = "courseTitle") String courseTitle) {
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
        // Remove course from the registrar
        for (int i = 0; i < registrar.size(); i++) {
            if (Objects.equals(registrar.get(i).getCourseNumber(), courseNumber)) {
                // We don't care if we can't find the course in the registrar for removal at this point
                registrar.remove(i);
                break;
            }
        }
        // Remove course from our courses list
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

    // PUT an existing student into an existing course
    @PutMapping(value = "/registrar")
    public ResponseEntity<Registrar> updateRegistrar(@RequestParam(value = "courseNumber") Integer courseNumber,
                                                     @RequestParam(value = "id") Integer id) {
        // Validation on updateRegistrar info as needed
        for (int i = 0; i < registrar.size(); i++) {
            if (Objects.equals(registrar.get(i).getCourseNumber(), courseNumber)) {
                Registrar r = registrar.get(i);
                if (r.addStudentId(id)) {
                    for (Integer student : registrar.get(i).getStudentIdList()) {
                        if (Objects.equals(student, id)) {
                            // Student in the course already
                            ResponseEntity re = ResponseEntity.unprocessableEntity().body("Course " + courseNumber + " already has student " + id);
                            return re;
                        }
                    }
                    // Student added to course
                    registrar.set(i, r);
                    return ResponseEntity.ok(r);
                }
                // Too many student in the course already
                ResponseEntity re = ResponseEntity.unprocessableEntity().body("Course " + courseNumber + " is full, cannot add student " + id);
                return re;
            }
        }
        // No course found with that number in registrar
        return ResponseEntity.notFound().build();
    }

    // GET all students from a chosen course
    @GetMapping(value = "/registrar")
    public ResponseEntity<List<Student>> getRegistrarStudents(@RequestParam(value = "courseNumber") Integer courseNumber) {
        for (Registrar r : registrar) {
            if (Objects.equals(r.getCourseNumber(), courseNumber)) {
                List<Student> studentList = new ArrayList<>();
                for (Integer id : r.getStudentIdList()) {
                    for (Student s : students) {
                        if (Objects.equals(s.getId(), id))
                            studentList.add(s);
                    }
                }
                return ResponseEntity.ok(studentList);
            }
        }
        ResponseEntity re = ResponseEntity.badRequest().body("No course with courseNumber " + courseNumber + " in the registrar.");
        return re;
    }

    // DELETE a student from a chosen course
    @DeleteMapping(value = "/registrar")
    public ResponseEntity<Registrar> removeStudentFromCourse(@RequestParam(value = "courseNumber") Integer courseNumber,
                                                             @RequestParam(value = "id") Integer id) {
        for (int i = 0; i < registrar.size(); i++) {
            if (Objects.equals(registrar.get(i).getCourseNumber(), courseNumber)) {
                Registrar r = registrar.get(i);
                if (r.removeStudentId(id)) {
                    // Student removed from course
                    registrar.set(i, r);
                    return ResponseEntity.ok(r);
                }
                // No student with given id in given course
                ResponseEntity re = ResponseEntity.unprocessableEntity().body("Course " + courseNumber + " does not have a student " + id);
                return re;
            }
        }
        ResponseEntity re = ResponseEntity.badRequest().body("No course with courseNumber " + courseNumber + " in the registrar.");
        return re;
    }


    void init() {
        students.add(new Student(counter.incrementAndGet(), "John", "Doe", LocalDate.parse("1888-03-20"), "johndoe@gmail.com"));
        students.add(new Student(counter.incrementAndGet(), "Jane", "Doe", LocalDate.parse("1928-12-24"), "janedoe@gmail.com"));
        students.add(new Student(counter.incrementAndGet(), "Liandra", "Mia", LocalDate.parse("2000-01-31"), "liamia@aol.com"));
        students.add(new Student(counter.incrementAndGet(), "Carson", "Pulser", LocalDate.parse("2008-04-22"), "carpul@yahoo.com"));
        students.add(new Student(counter.incrementAndGet(), "Irina", "Iatvaz", LocalDate.parse("1970-11-26"), "irinaiatvaz@jh.edu"));
        students.add(new Student(counter.incrementAndGet(), "Adrian", "Cother", LocalDate.parse("1925-06-13"), "acother@gmail.com"));
        students.add(new Student(counter.incrementAndGet(), "Litaf", "Vancai", LocalDate.parse("1978-08-17"), "litvan123@hotmail.com"));
        students.add(new Student(counter.incrementAndGet(), "Defoje", "Fancle", LocalDate.parse("1996-05-20"), "dfancle@mil.gov"));
        students.add(new Student(counter.incrementAndGet(), "Undeta", "Opinat", LocalDate.parse("1995-08-15"), "undetao@gmail.com"));
        students.add(new Student(counter.incrementAndGet(), "Qapin", "Yuanli", LocalDate.parse("1982-09-14"), "yuanliq@cv1.com"));
        students.add(new Student(counter.incrementAndGet(), "Paul", "Jones", LocalDate.parse("1978-10-23"), "pjones@gmail.com"));
        students.add(new Student(counter.incrementAndGet(), "Gram", "Martin", LocalDate.parse("1956-10-12"), "gram.martin@avast.com"));
        students.add(new Student(counter.incrementAndGet(), "Biats", "Day", LocalDate.parse("1999-12-23"), "daybiats@outlook.com"));
        students.add(new Student(counter.incrementAndGet(), "Cave", "Traves", LocalDate.parse("1958-06-30"), "cavet@world.org"));
        students.add(new Student(counter.incrementAndGet(), "Zena", "Human", LocalDate.parse("1953-09-12"), "humanz404@gmail.com"));

        courses.add(new Course(100, "Introduction to Mathematics"));
        courses.add(new Course(501, "Cybersecurity in Developing Environs"));
        courses.add(new Course(605, "Application Hardware Infrastructure"));
        courses.add(new Course(700, "Autonomous 5G Grid Development"));

        for (int i = 0; i < 3; i++) {
            List<Integer> ids = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                ids.add(students.get((i * 5) + j).getId());
            }
            registrar.add(new Registrar(courses.get(i).getCourseNumber(), ids));
        }

        List<Integer> ids = new ArrayList<>();
        for (Student s : students) {
            ids.add(s.getId());
        }
        registrar.add(new Registrar(courses.get(3).getCourseNumber(), ids));
    }
}
